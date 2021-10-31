package com.shawarmer.app.ui.home.subHome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.shawarmer.app.utils.UiStates
import com.shawarmer.app.R
import com.shawarmer.app.data.model.DishModel
import com.shawarmer.app.databinding.SubHomeFragmentBinding
import com.shawarmer.app.ui.home.HomeFragmentDirections
import com.shawarmer.app.utils.snackBar
import com.shawarmer.app.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import java.util.ArrayList


@AndroidEntryPoint
class SubHomeFragment : Fragment(), DishesAdapter.Callback {


    private val viewModel: SubHomeFragmentViewModel by viewModels()
    private lateinit var binding: SubHomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SubHomeFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMotionLayout()
        binding.dishesRv.adapter = dishesAdapter
        viewModel.getDishesUiState.observe(viewLifecycleOwner, { onGetDishesResponse(it) })

        viewModel.cartItems.observe(viewLifecycleOwner, {})
        viewModel.getDishes()
        setupCarousel()
    }

    private fun onGetDishesResponse(uiState: UiStates?) {
        when (uiState) {
            UiStates.NoConnection -> requireContext().toast(resources.getString(R.string.no_internet_connection))

            UiStates.Loading -> loading()

            is UiStates.Success<*> -> success(viewModel.dishesList)

            is UiStates.ItemUpdated<*> ->
                dishesAdapter.updateItem(uiState.position, uiState.data as DishModel)

            is UiStates.Error -> binding.root.snackBar(uiState.message)

            UiStates.Empty -> emptyList()

        }
    }


    private fun loading() {
        binding.emptyViewLayout.visibility = View.GONE
        binding.dishesRv.visibility = View.GONE
        binding.loadingProgress.visibility = View.VISIBLE
    }

    private fun emptyList() {
        binding.dishesRv.visibility = View.GONE
        binding.loadingProgress.visibility = View.GONE
        binding.emptyViewLayout.visibility = View.VISIBLE
    }

    private fun success(dishesList: List<DishModel>) {
        binding.dishesRv.visibility = View.VISIBLE
        binding.loadingProgress.visibility = View.GONE
        dishesAdapter.setList(dishesList as ArrayList<DishModel>)
    }


    private val dishesAdapter = DishesAdapter().also { it.callback = this }

    override fun onItemClickListener(model: DishModel, position: Int) {
        Navigation.findNavController(requireActivity(), R.id.mainNavHost)
            .navigate(HomeFragmentDirections.actionHomeFragmentToDishDetailsFragment(model))
    }

    override fun onAddToCartClickListener(model: DishModel, position: Int) {
        viewModel.addToCart(model)
    }

    private fun setupCarousel() {
        binding.carousel.setData(carouselImagesList)
        binding.carousel.registerLifecycle(lifecycle)
    }

    private val carouselImagesList :List<CarouselItem> = listOf(
        CarouselItem(imageDrawable = R.drawable.carousel_img_4),
        CarouselItem(imageDrawable = R.drawable.carousel_img_3),
        CarouselItem(imageDrawable = R.drawable.carousel_img_2),
        CarouselItem(imageDrawable = R.drawable.carousel_img_1),
    )


    private fun setupMotionLayout() {
        if(viewModel.currentMotionState!=0)
        binding.motionLayout.jumpToState(viewModel.currentMotionState)
        binding.motionLayout.setTransitionListener(transitionListener)
    }

    private val transitionListener: MotionLayout.TransitionListener =
        object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout, startId: Int, endId: Int) {
            }
            override fun onTransitionChange(
                motionLayout: MotionLayout,startId: Int,endId: Int,progress: Float
            ) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout, currentID: Int) {

                viewModel.currentMotionState = currentID
            }

            override fun onTransitionTrigger(motionLayout: MotionLayout,i: Int,b: Boolean,v: Float
            ) {
            }
        }
}