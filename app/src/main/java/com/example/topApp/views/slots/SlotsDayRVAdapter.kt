package com.example.topApp.views.slots

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.topApp.databinding.InflateDayItemBinding
import com.example.topApp.interfaces.ClickCallbackListener
import com.example.topApp.views.modal.DayDataClass


class SlotsDayRVAdapter(
    val clickCallbackListener: ClickCallbackListener
) :
    RecyclerView.Adapter<SlotsDayRVAdapter.ViewHolder>() {

    private val mDiffer: AsyncListDiffer<DayDataClass> =
        AsyncListDiffer(this, DiffCallback())

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun submitList(list: List<DayDataClass?>?) {
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

    class ViewHolder private constructor(private val inflateDayItemBinding: InflateDayItemBinding) :
        RecyclerView.ViewHolder(inflateDayItemBinding.root) {
        fun bind(
            dayDataClass: DayDataClass,
            clickCallbackListener: ClickCallbackListener
        ) {
            inflateDayItemBinding.dayDataClass = dayDataClass
            inflateDayItemBinding.clickCallBackListener = clickCallbackListener
            inflateDayItemBinding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    InflateDayItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<DayDataClass>() {

        override fun areItemsTheSame(
            oldItem: DayDataClass,
            newItem: DayDataClass
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: DayDataClass,
            newItem: DayDataClass
        ): Boolean {
            return oldItem == newItem
        }
    }


}
