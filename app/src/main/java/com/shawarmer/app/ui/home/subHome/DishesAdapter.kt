package com.shawarmer.app.ui.home.subHome

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shawarmer.app.data.model.DishModel
import com.shawarmer.app.databinding.DishListItemBinding
import java.util.*


class DishesAdapter :
    RecyclerView.Adapter<DishesAdapter.DishViewHolder>() {

    var mList: ArrayList<DishModel> = ArrayList()
    var callback:Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        return DishViewHolder( DishListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val dish = mList[position]
        holder.binding.dish = dish
        holder.binding.position = position
        callback?.let { holder.binding.callback = it }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(mList: ArrayList<DishModel>) {
        this.mList = mList
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, item: DishModel) {
        mList[position] = item
        notifyItemChanged(position, item)
    }

    inner class DishViewHolder(val binding: DishListItemBinding) : RecyclerView.ViewHolder(binding.root)

    interface Callback{
        fun onItemClickListener(model: DishModel, position: Int)
        fun onAddToCartClickListener(model: DishModel, position: Int)
    }
}