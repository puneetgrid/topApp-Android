package com.example.topApp.views.slots

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.topApp.databinding.MonthPickerItemsBinding
import com.example.topApp.interfaces.ClickCallbackListener
import com.example.topApp.views.modal.MonthPickerDataClass
import java.text.SimpleDateFormat
import java.util.*

class MonthPickerAdapter(
    val clickCallbackListener: ClickCallbackListener
) :
    RecyclerView.Adapter<MonthPickerAdapter.ViewHolder>() {

    private val mDiffer: AsyncListDiffer<MonthPickerDataClass> =
        AsyncListDiffer(this, MonthPickerAdapter.DiffCallback())

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun getItems(): List<MonthPickerDataClass> {
        return mDiffer.currentList
    }

    fun submitList(list: List<MonthPickerDataClass?>?) {
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

    class ViewHolder private constructor(private val monthPickerItemsBinding: MonthPickerItemsBinding) :
        RecyclerView.ViewHolder(monthPickerItemsBinding.root) {
        fun bind(
            monthData: MonthPickerDataClass,
            clickCallbackListener: ClickCallbackListener
        ) {
            monthPickerItemsBinding.month = monthData
            monthPickerItemsBinding.clickCallBackListener = clickCallbackListener
            monthPickerItemsBinding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MonthPickerItemsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }

            fun getMonthName(month: Int): String {
                val monthFormat = SimpleDateFormat("LLLL", Locale.getDefault())
                val calendar = Calendar.getInstance()
                calendar[Calendar.DAY_OF_MONTH] = 1
                calendar[Calendar.MONTH] = month
                return monthFormat.format(calendar.time)
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            }
        }

    }

    private class DiffCallback : DiffUtil.ItemCallback<MonthPickerDataClass>() {

        override fun areItemsTheSame(
            oldItem: MonthPickerDataClass,
            newItem: MonthPickerDataClass
        ): Boolean {
            return oldItem.month == newItem.month
        }

        override fun areContentsTheSame(
            oldItem: MonthPickerDataClass,
            newItem: MonthPickerDataClass
        ): Boolean {
            return oldItem == newItem
        }
    }
}
