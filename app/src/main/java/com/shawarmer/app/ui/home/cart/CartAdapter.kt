package com.shawarmer.app.ui.home.cart

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shawarmer.app.data.model.DishModel
import com.shawarmer.app.databinding.CartDishListItemBinding
import com.shawarmer.app.utils.roundOffDecimal
import java.util.*


class CartAdapter :
    RecyclerView.Adapter<CartAdapter.CartItemViewHolder>() {


    var callback: Callback? = null
    private var mList: ArrayList<DishModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        return CartItemViewHolder(
            CartDishListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val dish = mList[position]
        holder.binding.also {
            it.dish = dish
            it.position = position
            callback?.let { callback -> holder.binding.callback = callback }
        }

        setTotalPrice(holder, dish)
    }

    private fun setTotalPrice(holder: CartItemViewHolder, dish: DishModel) {
        holder.binding.totalPrice.text = (dish.price * dish.quantity).roundOffDecimal().toString()
        holder.binding.CartQuantity.text = dish.quantity.toString()
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
        if (mList.size > position)
            mList[position] = item
        notifyItemChanged(position, item)
    }

    fun removeItem(position: Int) {
        mList.drop(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount - position);
    }

    inner class CartItemViewHolder(val binding: CartDishListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface Callback {
        fun plusCartQuantityListener(model: DishModel, position: Int)
        fun minusCartQuantityListener(model: DishModel, position: Int)
    }
}