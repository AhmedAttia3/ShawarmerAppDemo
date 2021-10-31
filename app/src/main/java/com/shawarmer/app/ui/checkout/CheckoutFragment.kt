package com.shawarmer.app.ui.checkout

import android.app.Dialog
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.WalletConstants
import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity
import com.oppwa.mobile.connect.checkout.dialog.GooglePayHelper
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings
import com.oppwa.mobile.connect.checkout.meta.CheckoutSkipCVVMode
import com.oppwa.mobile.connect.exception.PaymentError
import com.oppwa.mobile.connect.provider.Connect
import com.oppwa.mobile.connect.provider.Transaction
import com.oppwa.mobile.connect.provider.TransactionType
import com.shawarmer.app.MainActivity
import com.shawarmer.app.R
import com.shawarmer.app.databinding.CheckoutFragmentBinding
import com.shawarmer.app.databinding.DialogSuccessPaymentLayoutBinding
import com.shawarmer.app.receiver.CheckoutBroadcastReceiver
import com.shawarmer.app.utils.Constants
import com.shawarmer.app.utils.UiStates
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutFragment : Fragment(), CheckoutFragmentCallback {

    companion object {
        fun newInstance() = CheckoutFragment()
        private const val STATE_RESOURCE_PATH = "STATE_RESOURCE_PATH"
    }
     val viewModel: CheckoutViewModel by viewModels()
    private val args: CheckoutFragmentArgs by navArgs()
    private var amount:String = "10"

    lateinit var binding:CheckoutFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CheckoutFragmentBinding.inflate(inflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        amount = args.amount
        viewModel.getCheckoutIdUiState.observe(viewLifecycleOwner){onCheckoutIdResponse(it)}
        viewModel.getPaymentStatusUiState.observe(viewLifecycleOwner){onPaymentStatusIdResponse(it)}
        binding.also {
            it.callback = this
            it.amount = amount
        }
    }

    private fun onCheckoutIdResponse(state: UiStates) {
        when(state){
            UiStates.NoConnection -> showAlertDialog(R.string.no_internet_connection)
            UiStates.Loading -> showProgressDialog()
            is UiStates.Error -> showAlertDialog(R.string.error_message)
            is UiStates.Success<*> -> openCheckoutUI(state.data as String)
        }
    }

    private fun onPaymentStatusIdResponse(state: UiStates) {
        when(state){
            UiStates.NoConnection -> showAlertDialog(R.string.no_internet_connection)
            UiStates.Loading -> showProgressDialog()
            is UiStates.Error -> showAlertDialog(R.string.error_message)
            is UiStates.Success<*> -> onPaymentStatSuccess(state.data as Boolean)
        }
    }

    private fun showProgressDialog() {
        (requireActivity() as MainActivity).showProgressDialog()
    }

    private fun dismissProgressDialog() {
        (requireActivity() as MainActivity).dismissProgressDialog()
    }

    private fun showAlertDialog(message: String) {
        (requireActivity() as MainActivity).showAlertDialog(message)
    }
    private fun showAlertDialog(messageId: Int) {
        showAlertDialog(getString(messageId))
    }

    private var resourcePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            resourcePath = savedInstanceState.getString(STATE_RESOURCE_PATH)
        }
    }
    private fun openCheckoutUI(checkoutId: String) {
        dismissProgressDialog()
        val checkoutSettings = createCheckoutSettings(checkoutId, getString(R.string.callback_scheme))

        /* Set componentName if you want to receive callbacks from the checkout */
        val componentName = ComponentName(requireActivity().packageName, CheckoutBroadcastReceiver::class.java.name)

        /* Set up the Intent and start the checkout activity */
        val intent = checkoutSettings.createCheckoutActivityIntent(requireActivity(), componentName)
        startForResult.launch(intent)
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        val data = result.data
        when (result.resultCode) {
            CheckoutActivity.RESULT_OK -> {
                /* Transaction completed */
                val transaction: Transaction = data!!.getParcelableExtra(
                    CheckoutActivity.CHECKOUT_RESULT_TRANSACTION)!!

                resourcePath = data.getStringExtra(CheckoutActivity.CHECKOUT_RESULT_RESOURCE_PATH)

                /* Check the transaction type */
                if (transaction.transactionType == TransactionType.SYNC) {
                    /* Check the status of synchronous transaction */
                    viewModel.requestPaymentStatus(resourcePath!!)
                } else {
                    /* Asynchronous transaction is processed in the onNewIntent() */
                    dismissProgressDialog()
                }
            }
            CheckoutActivity.RESULT_CANCELED -> {
                showAlertDialog(R.string.place_order_canceled)
                dismissProgressDialog()
            }
            CheckoutActivity.RESULT_ERROR -> {
                dismissProgressDialog()
                val error: PaymentError = data!!.getParcelableExtra(
                    CheckoutActivity.CHECKOUT_RESULT_ERROR)!!
                showAlertDialog(error.errorMessage)
            }
        }

    }



    private fun createCheckoutSettings(checkoutId: String, callbackScheme: String): CheckoutSettings {
        return CheckoutSettings(checkoutId, Constants.PaymentConfig.PAYMENT_BRANDS, Connect.ProviderMode.TEST)
            .setSkipCVVMode(CheckoutSkipCVVMode.FOR_STORED_CARDS)
            .setShopperResultUrl("$callbackScheme://callback")
            .setGooglePayPaymentDataRequest(getGooglePayRequest())
    }

    private fun onPaymentStatSuccess(isSuccess:Boolean) {
        dismissProgressDialog()
        if(isSuccess){
            showSuccessPaymentDialog()
        }else{
            showAlertDialog(R.string.message_unsuccessful_payment)
        }
    }

    private fun getGooglePayRequest(): PaymentDataRequest {
        return GooglePayHelper.preparePaymentDataRequestBuilder(
            amount,
            Constants.PaymentConfig.CURRENCY,
            Constants.PaymentConfig.MERCHANT_ID,
            getPaymentMethodsForGooglePay(),
            getDefaultCardNetworksForGooglePay()
        ).build()
    }

    private fun getPaymentMethodsForGooglePay(): Array<Int> {
        return arrayOf(
            WalletConstants.PAYMENT_METHOD_CARD,
            WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD
        )
    }

    private fun getDefaultCardNetworksForGooglePay(): Array<Int> {
        return arrayOf(
            WalletConstants.CARD_NETWORK_VISA,
            WalletConstants.CARD_NETWORK_MASTERCARD,
            WalletConstants.CARD_NETWORK_AMEX
        )
    }



    fun onNewIntent(intent: Intent) {
        /* Check of the intent contains the callback scheme */
        if (resourcePath != null && hasCallBackScheme(intent)) {
            viewModel.requestPaymentStatus(resourcePath!!)
        }
    }

    private fun hasCallBackScheme(intent: Intent): Boolean {
        return intent.scheme == getString(R.string.callback_scheme) ||
                intent.scheme == getString(R.string.payment_button_callback_scheme) ||
                intent.scheme == getString(R.string.custom_ui_callback_scheme)
    }
    fun showSuccessPaymentDialog(){
        val binding = DialogSuccessPaymentLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        val progressDialog = Dialog(requireContext())
        progressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog.setContentView(binding.root)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setOnShowListener {
            binding.trackOrderBtn.setOnClickListener {
                findNavController().navigate(CheckoutFragmentDirections.actionCheckoutFragmentToOrderTrackingFragment())
                progressDialog.dismiss()
            }
        }
        progressDialog.show()
    }
    override fun backBtnClickListener() {
        findNavController().navigateUp()
    }

    override fun addDeliveryAddressBtnListener() {

    }

    override fun itemsBtnClickListener() {

    }

    override fun addPromoCodeBtnListener() {

    }

    override fun placeOrderBtnListener() {
        viewModel.requestCheckoutId(amount)
    }
}