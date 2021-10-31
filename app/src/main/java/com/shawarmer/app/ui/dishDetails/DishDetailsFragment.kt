package com.shawarmer.app.ui.dishDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shawarmer.app.MainNavGraphDirections
import com.shawarmer.app.data.model.DishModel
import com.shawarmer.app.databinding.DishDetailsFragmentBinding
import com.shawarmer.app.utils.roundOffDecimal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DishDetailsFragment : Fragment(), DisDetailsFragmentCallback {

    companion object {
        fun newInstance() = DishDetailsFragment()
    }

    private val viewModel: DishDetailsViewModel by viewModels()
    private lateinit var binding: DishDetailsFragmentBinding

    private val args: DishDetailsFragmentArgs by navArgs()
    private lateinit var dishModel: DishModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DishDetailsFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dishModel = args.dish
        viewModel.dishModel.value = dishModel
        dishModel.quantity = 1
        binding.also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.callback = this
            it.dish = dishModel
        }
    }

    private fun setTotalPrice() {
        binding.totalPrice.text =
            (dishModel.price * dishModel.quantity).roundOffDecimal().toString()
        binding.CartQuantity.text = dishModel.quantity.toString()
    }

    override fun plusCartQuantityListener() {
        dishModel.quantity = dishModel.quantity + 1
        setTotalPrice()
    }

    override fun minusCartQuantityListener() {
        if (dishModel.quantity > 1) {
            dishModel.quantity = dishModel.quantity - 1
            setTotalPrice()
        }
    }

    override fun addToCartClickListener() {
        viewModel.updateItemCartQuantity(dishModel)
        backBtnClickListener()
    }

    override fun goToCartClickListener() {
        findNavController().navigate(MainNavGraphDirections.globalActionToCartFragment(false))
    }

    override fun backBtnClickListener() {
        findNavController().navigateUp()
    }

}

