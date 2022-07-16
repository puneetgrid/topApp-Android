package com.example.topApp.views.expertdetails

import com.example.topApp.R
import com.example.topApp.databinding.ExpertDetailsFragmentBinding
import com.example.topApp.views.base.BaseFragment


class ExpertDetailsFragment : BaseFragment<ExpertDetailsFragmentBinding, ExpertsDetailsVM>() {

    override fun setLayout() = R.layout.expert_details_fragment

    override fun setViewModel() = ExpertsDetailsVM::class.java

    override fun bindViews(viewModel: ExpertsDetailsVM) {
        binding.viewModel = viewModel
    }
}