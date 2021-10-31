package com.shawarmer.app.ui.orderTracking

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shawarmer.app.databinding.OrderTrackingFragmentBinding

class OrderTrackingFragment : Fragment() {

    companion object {
        fun newInstance() = OrderTrackingFragment()
    }

    private lateinit var binding: OrderTrackingFragmentBinding
    val adapter = TimeLineAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OrderTrackingFragmentBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtnClick = {
            findNavController().navigateUp()
        }

        adapter.setList(list)
        binding.recyclerView.adapter = adapter
        countDownTimer.start()
    }

    private val countDownTimer = object :CountDownTimer(20000, 4000){
        override fun onTick(p0: Long) {
            var active = false
            var inactive = false
            list.map {
                if (it.status == OrderStatus.ACTIVE &&!active){
                    it.status = OrderStatus.COMPLETED
                    active = true
                }
                if (it.status == OrderStatus.INACTIVE &&!inactive){
                    it.status = OrderStatus.ACTIVE
                    inactive = true
                }
            }
            adapter.setList(list)
        }

        override fun onFinish() {

        }

    }

    val list = listOf(
        TimeLineModel("Ordered", OrderStatus.INACTIVE),
        TimeLineModel("Shipped", OrderStatus.INACTIVE),
        TimeLineModel("Out for delivery", OrderStatus.INACTIVE),
        TimeLineModel("Arriving", OrderStatus.INACTIVE),
    )

    data class TimeLineModel(
        val name:String,
        var status: OrderStatus
    )
    enum class OrderStatus {
        COMPLETED,
        ACTIVE,
        INACTIVE
    }
}