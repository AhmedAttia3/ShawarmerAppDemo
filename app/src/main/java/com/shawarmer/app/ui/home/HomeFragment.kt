package com.shawarmer.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.shawarmer.app.MainActivity
import com.shawarmer.app.R
import com.shawarmer.app.databinding.HomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(requireActivity(), R.id.container_fragment)
        setupBottomNavWithNavController(binding.bottomNav, navController)
        (requireActivity() as MainActivity).whiteBackground()
    }

    /*
    this function created to synchronize
    custom bottom navigation with navigation Controller
    */
    private fun setupBottomNavWithNavController(
        bottomNav: BubbleNavigationLinearView,
        navController: NavController
    ) {
        bottomNav.setNavigationChangeListener { view, position ->
            val inclusive = navController.currentDestination!!.id != R.id.subHomeFragment
            if (view.id != navController.currentDestination!!.id)
                when (view.id) {
                    R.id.subHomeFragment -> {
                        navController.navigateUp()
                    }
                    R.id.cartFragment -> {
                        navController.navigate(
                            R.id.cartFragment, null,
                            NavOptions.Builder()
                                .setPopUpTo(navController.currentDestination!!.id, inclusive)
                                .build()
                        )
                    }
                    R.id.profileFragment -> {
                        navController.navigate(
                            R.id.profileFragment, null,
                            NavOptions.Builder()
                                .setPopUpTo(navController.currentDestination!!.id, inclusive)
                                .build()
                        )
                    }
                }
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.subHomeFragment -> {
                    bottomNav.setCurrentActiveItem(0)
                }
                R.id.cartFragment -> {
                    bottomNav.setCurrentActiveItem(1)
                }
                R.id.profileFragment -> {
                    bottomNav.setCurrentActiveItem(2)
                }
            }
        }
    }


}