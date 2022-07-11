package com.example.topApp.views.slots

import com.example.topApp.R
import com.example.topApp.databinding.SlotsFragmentBinding
import com.example.topApp.databinding.SplashFragmentBinding
import com.example.topApp.views.base.BaseFragment


class SlotsFragment : BaseFragment<SlotsFragmentBinding, SlotsVM>() {

    override fun setLayout() = R.layout.slots_fragment

    override fun setViewModel() = SlotsVM::class.java

    override fun bindViews(viewModel: SlotsVM) {
        binding.viewModel = viewModel

    }
}