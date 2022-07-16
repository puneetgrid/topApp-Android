package com.example.topApp.views.experiencedetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.topApp.databinding.InflateExperienceItemBinding
import com.example.topApp.views.modal.ExperienceResponse


class ExperienceRVAdapter() :
    RecyclerView.Adapter<ExperienceRVAdapter.ViewHolder>() {

    private val mDiffer: AsyncListDiffer<ExperienceResponse> =
        AsyncListDiffer(this, DiffCallback())

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun submitList(list: List<ExperienceResponse?>?) {
        mDiffer.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            mDiffer.currentList[position],
        )
    }

    class ViewHolder private constructor(private val inflateExperienceItemBinding: InflateExperienceItemBinding) :
        RecyclerView.ViewHolder(inflateExperienceItemBinding.root) {
        fun bind(
            experienceResponse: ExperienceResponse
        ) {
            inflateExperienceItemBinding.experienceResponse = experienceResponse
            inflateExperienceItemBinding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    InflateExperienceItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ExperienceResponse>() {

        override fun areItemsTheSame(
            oldItem: ExperienceResponse,
            newItem: ExperienceResponse
        ): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(
            oldItem: ExperienceResponse,
            newItem: ExperienceResponse
        ): Boolean {
            return oldItem == newItem
        }
    }


}
