package com.example.topApp.interfaces

import android.view.View
import com.example.topApp.views.modal.CategoryData
import com.example.topApp.views.modal.SlotsData

interface ClickCallbackListener {
    fun click(any: Any?) {}
    fun monthPickerClick(view: View, month: Int) {}
    fun categoryClick(view: View, categoryData: CategoryData) {}
    fun slotsClick(view: View, slotsData: SlotsData) {}
}