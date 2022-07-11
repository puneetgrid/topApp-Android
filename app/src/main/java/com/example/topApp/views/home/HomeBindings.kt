package com.example.topApp.views.home

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


@BindingAdapter("addIcon")
fun ImageView.addIcon(url:String?) {
    if (url.isNullOrEmpty().not()) {
        Glide.with(this).load(url)
            .into(this)
    }
}
