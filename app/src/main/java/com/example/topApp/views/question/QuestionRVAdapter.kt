package com.example.topApp.views.question

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.topApp.databinding.InflateQuestionItemBinding
import com.example.topApp.interfaces.ClickCallbackListener
import com.example.topApp.views.modal.QuestionData


class QuestionRVAdapter(
    val clickCallbackListener: ClickCallbackListener
) :
    RecyclerView.Adapter<QuestionRVAdapter.ViewHolder>() {

    private val mDiffer: AsyncListDiffer<QuestionData> =
        AsyncListDiffer(this, DiffCallback())

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun submitList(list: List<QuestionData?>?) {
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

    class ViewHolder private constructor(private val inflateQuestionItemBinding: InflateQuestionItemBinding) :
        RecyclerView.ViewHolder(inflateQuestionItemBinding.root) {
        fun bind(
            questionData: QuestionData,
            clickCallbackListener: ClickCallbackListener
        ) {
            inflateQuestionItemBinding.questionData = questionData
            inflateQuestionItemBinding.clickCallBackListener = clickCallbackListener
            inflateQuestionItemBinding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    InflateQuestionItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<QuestionData>() {

        override fun areItemsTheSame(
            oldItem: QuestionData,
            newItem: QuestionData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: QuestionData,
            newItem: QuestionData
        ): Boolean {
            return oldItem == newItem
        }
    }


}
