package com.example.topApp.views.slots

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.topApp.databinding.InflateSlotsItemBinding
import com.example.topApp.interfaces.ClickCallbackListener
import com.example.topApp.views.modal.SlotsData


class SlotsTimeRVAdapter(
    val clickCallbackListener: ClickCallbackListener
) :
    RecyclerView.Adapter<SlotsTimeRVAdapter.ViewHolder>() {

    private val mDiffer: AsyncListDiffer<SlotsData> =
        AsyncListDiffer(this, DiffCallback())

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun submitList(list: List<SlotsData?>?) {
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

    class ViewHolder private constructor(private val inflateSlotsItemBinding: InflateSlotsItemBinding) :
        RecyclerView.ViewHolder(inflateSlotsItemBinding.root) {
        fun bind(
            slotsData: SlotsData,
            clickCallbackListener: ClickCallbackListener
        ) {
            inflateSlotsItemBinding.slotsData = slotsData
            inflateSlotsItemBinding.clickCallBackListener = clickCallbackListener
            inflateSlotsItemBinding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    InflateSlotsItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<SlotsData>() {

        override fun areItemsTheSame(
            oldItem: SlotsData,
            newItem: SlotsData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SlotsData,
            newItem: SlotsData
        ): Boolean {
            return oldItem == newItem
        }
    }


}
