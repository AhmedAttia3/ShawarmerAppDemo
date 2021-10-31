package com.shawarmer.app.ui.home.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.shawarmer.app.MainNavGraphDirections
import com.shawarmer.app.R
import com.shawarmer.app.data.model.DishModel
import com.shawarmer.app.databinding.CartFragmentBinding
import com.shawarmer.app.utils.UiStates
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CartFragment : Fragment(), CartAdapter.Callback {

    private val viewModel: CartViewModel by viewModels()
    private lateinit var binding: CartFragmentBinding
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CartFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartAdapter = CartAdapter().also { it.callback = this }
        binding.also {
            it.dishsRV.adapter = cartAdapter
            it.appBar.isBackBtnDisabled = arguments?.getBoolean("isBackBtnDisabled")?:true
            it.backBtnClick = {
                findNavController().navigateUp()
            }
        }


        viewModel.getCartItemsUiState.observe(viewLifecycleOwner, { onCartItemResponse(it) })
        viewModel.getCartItems()


        binding.checkoutBtn.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.mainNavHost)
                .navigate(MainNavGraphDirections.globalActionToCheckoutFragment(binding.cartTotalPrice.text.toString()))
        }

        binding.goToShopping.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun onCartItemResponse(state: UiStates?) {
        when (state) {
            UiStates.Loading -> loading()
            is UiStates.Success<*> -> onSuccess(viewModel.cartItemsList)
            is UiStates.ItemUpdated<*> -> {
                cartAdapter.updateItem(state.position, state.data as DishModel)
                calculateCartTotalPrice()
            }
            is UiStates.ItemDeleted -> {
                cartAdapter.removeItem(state.position)
                calculateCartTotalPrice()
            }
            UiStates.Empty -> onEmptyCart()
        }
    }

    private fun loading() {
        binding.chickOutLayout.visibility = View.GONE
        binding.emptyViewLayout.visibility = View.GONE
        binding.dishsRV.visibility = View.GONE
        binding.loadingProgress.visibility = View.VISIBLE
    }

    private fun onSuccess(list: List<DishModel>) {
        binding.dishsRV.visibility = View.VISIBLE
        binding.loadingProgress.visibility = View.GONE
        cartAdapter.setList(list as ArrayList<DishModel>)
        binding.chickOutLayout.visibility = View.VISIBLE
        calculateCartTotalPrice()
        viewModel.cartItemsLiveData.observe(viewLifecycleOwner, {})
    }

    private fun onEmptyCart() {
        binding.dishsRV.visibility = View.GONE
        binding.loadingProgress.visibility = View.GONE
        binding.emptyViewLayout.visibility = View.VISIBLE
        binding.chickOutLayout.visibility = View.GONE
    }

    private fun calculateCartTotalPrice() {
        var total = 0L
        viewModel.cartItemsList.map { model ->
            total += (model.price.toLong() * model.quantity)
        }
        binding.cartTotalPrice.text = total.toString()
    }

    override fun plusCartQuantityListener(model: DishModel, position: Int) {
        viewModel.plusCartQuantity(model)
    }

    override fun minusCartQuantityListener(model: DishModel, position: Int) {
        viewModel.minusCartQuantity(model, position)
    }

}