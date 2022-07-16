package com.example.topApp.views.slots

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.topApp.R
import com.example.topApp.databinding.MonthPickerDialogBinding
import com.example.topApp.databinding.SlotsFragmentBinding
import com.example.topApp.interfaces.ClickCallbackListener
import com.example.topApp.interfaces.DialogCallback
import com.example.topApp.utils.Utility
import com.example.topApp.views.base.BaseFragment
import com.example.topApp.views.modal.MonthPickerDataClass
import com.example.topApp.views.modal.SlotsData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.YearMonth


class SlotsFragment : BaseFragment<SlotsFragmentBinding, SlotsVM>() {

    override fun setLayout() = R.layout.slots_fragment

    override fun setViewModel() = SlotsVM::class.java

    override fun bindViews(viewModel: SlotsVM) {
        binding.viewModel = viewModel
        initView()
        viewModel.isProgressShow.set(true)
        viewModel.appointmentType = "Morning"
        viewModel.getSlotsData()
        bindObserver()

        binding.tvMonth.text = YearMonth.now().month.name
        binding.tvMonth.setOnClickListener {
            customMonthPickerDialog()
        }
        binding.morningLayout.setOnClickListener {
            viewModel.isMorning.set(true)
            viewModel.appointmentType = "Morning"
            viewModel.getSlotsData()
        }
        binding.eveningLayout.setOnClickListener {
            viewModel.isMorning.set(false)
            viewModel.appointmentType = "Evening"
            viewModel.getSlotsData()
        }

        viewModel.getAllDateOfCurrentMonth(YearMonth.now().year, YearMonth.now().monthValue)
    }

    private fun initView() {
        viewModel.slotsTimeRVAdapter = SlotsTimeRVAdapter(
            object : ClickCallbackListener {
                override fun slotsClick(view: View, slotsData: SlotsData) {
                    super.slotsClick(view, slotsData)
                    viewModel.slotsList.forEach {
                        it.isSelected = it.id == slotsData.id
                    }
                    viewModel.slotsTimeRVAdapter?.notifyDataSetChanged()
                }
            })
        binding.recyclerView.adapter = viewModel.slotsTimeRVAdapter


        viewModel.slotsDayRVAdapter = SlotsDayRVAdapter(
            object : ClickCallbackListener {})
        binding.monthRecyclerView.adapter = viewModel.slotsDayRVAdapter
    }

    fun bindObserver() {
        viewModel.daysLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.isProgressShow.set(false)
            if (!it.isNullOrEmpty()) {
                viewModel.slotsDayRVAdapter?.submitList(it)
                viewModel.slotsDayRVAdapter?.notifyDataSetChanged()
                viewModel.daysLiveData.postValue(null)
            }
        })
        viewModel.slotsLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.isProgressShow.set(false)
            if (!it.isNullOrEmpty()) {
                viewModel.slotsTimeRVAdapter?.submitList(it)
                viewModel.slotsTimeRVAdapter?.notifyDataSetChanged()
                viewModel.slotsLiveData.postValue(null)
            }
        })

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            viewModel.isProgressShow.set(false)
            if (it != null) {
                when (it) {
                    Utility.networkError -> {
                        Utility.networkErrorDialog(
                            requireContext(),
                            viewModel.resourceProvider.getString(R.string.app_name),
                            viewModel.resourceProvider.getString(R.string.check_internet),
                            viewModel.resourceProvider.getString(R.string.error_retry),
                            viewModel.resourceProvider.getString(R.string.cancel),
                            false,
                            object : DialogCallback {
                                override fun callback(any: Any) {
                                    if (any == 1) {
                                        viewModel.isProgressShow.set(true)
                                        viewModel.getSlotsData()
                                    }
                                }
                            }
                        )
                    }
                }
                viewModel.errorLiveData.postValue(null)
            }

        }

    }

    private fun customMonthPickerDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val monthPickerDialogBinding: MonthPickerDialogBinding = DataBindingUtil.inflate(
            dialog.layoutInflater,
            R.layout.month_picker_dialog,
            null,
            false
        )
        dialog.setContentView(monthPickerDialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        monthPickerDialogBinding.tvYear.text = YearMonth.now().year.toString()
        var monthSelected = YearMonth.now().monthValue - 1
        val monthDataList = ArrayList<MonthPickerDataClass>()

        viewModel.monthPickerAdapter =
            MonthPickerAdapter(
                object : ClickCallbackListener {
                    override fun monthPickerClick(view: View, month: Int) {
                        super.monthPickerClick(view, month)
                        viewModel.monthPickerAdapter?.getItems()
                            ?.forEachIndexed { index, monthPickerDataClass ->
                                monthPickerDataClass.isSelected =
                                    monthPickerDataClass.month == month
                                viewModel.monthPickerAdapter?.notifyItemChanged(index)
                            }
                        lifecycleScope.launch(Dispatchers.Main) {
                            delay(1000)
                            dialog.dismiss()

                            binding.tvMonth.text = MonthPickerAdapter.ViewHolder.getMonthName(month)
                            monthSelected = month
                            viewModel.getAllDateOfCurrentMonth(YearMonth.now().year, month + 1)

                        }
                    }
                })

        viewModel.months.forEach {
            if (it == monthSelected) {
                monthDataList.add(
                    MonthPickerDataClass(
                        it,
                        MonthPickerAdapter.ViewHolder.getMonthName(it),
                        true
                    )
                )
            } else {
                monthDataList.add(
                    MonthPickerDataClass(
                        it,
                        MonthPickerAdapter.ViewHolder.getMonthName(it),
                        false
                    )
                )
            }
        }
        viewModel.monthPickerAdapter?.submitList(monthDataList)

        monthPickerDialogBinding.recyclerView.adapter =
            viewModel.monthPickerAdapter

        dialog.show()
    }


}