package com.example.topApp.interfaces

import android.view.View
import com.example.topApp.views.modal.CategoryData

interface ClickCallbackListener {
    fun click(any: Any?) {}
    fun categoryClick(view: View, categoryData: CategoryData) {}
}