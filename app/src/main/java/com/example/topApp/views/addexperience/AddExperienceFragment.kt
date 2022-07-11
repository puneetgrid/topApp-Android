package com.example.topApp.views.addexperience

import com.example.topApp.R
import com.example.topApp.databinding.AddExperienceFragmentBinding
import com.example.topApp.views.base.BaseFragment
import com.example.topApp.views.splash.SplashVM


class AddExperienceFragment : BaseFragment<AddExperienceFragmentBinding, AddExperienceVM>() {

    override fun setLayout() = R.layout.add_experience_fragment

    override fun setViewModel() = AddExperienceVM::class.java

    override fun bindViews(viewModel: AddExperienceVM) {
        binding.viewModel = viewModel
    }


}