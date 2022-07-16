package com.example.topApp.views.profile.expertdetails

import androidx.navigation.fragment.findNavController
import com.example.topApp.R
import com.example.topApp.databinding.ExpertDetailsFragmentBinding
import com.example.topApp.views.base.BaseFragment


class ExpertDetailsFragment : BaseFragment<ExpertDetailsFragmentBinding, ExpertsDetailsVM>() {

    override fun setLayout() = R.layout.expert_details_fragment

    override fun setViewModel() = ExpertsDetailsVM::class.java

    override fun bindViews(viewModel: ExpertsDetailsVM) {
        binding.viewModel = viewModel

        binding.tvExperienceDetails.setOnClickListener {
            navigate(
                R.id.expertDetailsFragment,
                destinationId = R.id.action_expertDetailsFragment_to_experienceDetailsFragment
            )
        }
        binding.chooseSlotButton.setOnClickListener {
            navigate(
                R.id.expertDetailsFragment,
                destinationId = R.id.action_expertDetailsFragment_to_slotsFragment
            )
        }
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}