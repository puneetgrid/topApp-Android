package com.example.topApp.views.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.topApp.databinding.InflateCategoryItemBinding
import com.example.topApp.interfaces.ClickCallbackListener
import com.example.topApp.views.modal.CategoryData


class HomeRVAdapter(
    val clickCallbackListener: ClickCallbackListener
) :
    RecyclerView.Adapter<HomeRVAdapter.ViewHolder>() {

    private val mDiffer: AsyncListDiffer<CategoryData> =
        AsyncListDiffer(this, DiffCallback())

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun submitList(list: List<CategoryData?>?) {
        mDiffer.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            mDiffer.currentList[position],
            clickCallbackListener
        )
    }

    class ViewHolder private constructor(private val inflateCategoryItemBinding: InflateCategoryItemBinding) :
        RecyclerView.ViewHolder(inflateCategoryItemBinding.root) {
        fun bind(
            categoryData: CategoryData,
            clickCallbackListener: ClickCallbackListener
        ) {
            inflateCategoryItemBinding.categoryData = categoryData
            inflateCategoryItemBinding.clickCallBackListener = clickCallbackListener
            inflateCategoryItemBinding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    InflateCategoryItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<CategoryData>() {

        override fun areItemsTheSame(
            oldItem: CategoryData,
            newItem: CategoryData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CategoryData,
            newItem: CategoryData
        ): Boolean {
            return oldItem == newItem
        }
    }


}
