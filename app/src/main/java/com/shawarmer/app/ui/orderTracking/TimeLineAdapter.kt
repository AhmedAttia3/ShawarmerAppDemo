package com.shawarmer.app.ui.orderTracking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import com.shawarmer.app.R
import com.shawarmer.app.databinding.ItemTimelineBinding
import java.util.*


class TimeLineAdapter:
    RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {

    private var mList: List<OrderTrackingFragment.TimeLineModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        return TimeLineViewHolder(
            ItemTimelineBinding.inflate(LayoutInflater.from(parent.context)), viewType)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        val timeLineModel = mList[position]
        holder.binding.textTimelineTitle.text = timeLineModel.name
        when {
            timeLineModel.status == OrderTrackingFragment.OrderStatus.INACTIVE -> {
                holder.binding.timeline.marker = ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_un_check_circle_outline_darck)

                holder.binding.timelineBackground.strokeWidth = 0
                holder.binding.timelineBackground.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.off_white))
                holder.binding.textTimelineTitle.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            }
            timeLineModel.status == OrderTrackingFragment.OrderStatus.ACTIVE -> {
                holder.binding.timeline.marker = ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_marker_active)
                holder.binding.timelineBackground.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.off_white))
                holder.binding.textTimelineTitle.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
                holder.binding.timelineBackground.strokeColor = (ContextCompat.getColor(holder.itemView.context, R.color.primaryColor))
                holder.binding.timelineBackground.strokeWidth = 5
            }
            else -> {
                holder.binding.timeline.setMarker(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_checkmark_circle_outline_darck))

                holder.binding.timelineBackground.strokeWidth = 0
                holder.binding.timelineBackground.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.primaryColor))
                holder.binding.textTimelineTitle.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setList(mList: List<OrderTrackingFragment.TimeLineModel>) {
        this.mList = mList
        notifyDataSetChanged()
    }

    inner class TimeLineViewHolder(val binding:ItemTimelineBinding, viewType: Int) :RecyclerView.ViewHolder(binding.root) {
        init {
            binding.timeline.initLine(viewType)
        }
    }
}