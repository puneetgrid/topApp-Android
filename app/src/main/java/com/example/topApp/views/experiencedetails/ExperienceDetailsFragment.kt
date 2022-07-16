package com.example.topApp.views.experiencedetails

import androidx.navigation.fragment.findNavController
import com.example.topApp.R
import com.example.topApp.databinding.ExperienceDetailsFragmentBinding
import com.example.topApp.interfaces.DialogCallback
import com.example.topApp.utils.Utility
import com.example.topApp.views.base.BaseFragment


class ExperienceDetailsFragment :
    BaseFragment<ExperienceDetailsFragmentBinding, ExperienceDetailsVM>() {

    private var experienceRVAdapter: ExperienceRVAdapter? = null

    override fun setLayout() = R.layout.experience_details_fragment

    override fun setViewModel() = ExperienceDetailsVM::class.java

    override fun bindViews(viewModel: ExperienceDetailsVM) {
        binding.viewModel = viewModel
        initView()
        viewModel.isProgressShow.set(true)
        viewModel.getExperience()
        bindObserver()
        val bundle = arguments ?: return
        val args = ExperienceDetailsFragmentArgs.fromBundle(bundle)
        viewModel.isAddMoreShow.set(args.fromProfile ?: false)

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvAddMore.setOnClickListener {
            navigate(
                R.id.experienceDetailsFragment,
                destinationId = R.id.action_experienceDetailsFragment_to_addExperienceFragment
            )

        }

    }

    private fun initView() {
        experienceRVAdapter = ExperienceRVAdapter()
        binding.expRecyclerView.adapter = experienceRVAdapter
    }

    private fun bindObserver() {

        viewModel.experienceLiveData.observe(viewLifecycleOwner) {
            viewModel.isProgressShow.set(false)
            if (it != null) {
                experienceRVAdapter?.submitList(it)
                experienceRVAdapter?.notifyDataSetChanged()
                viewModel.experienceLiveData.postValue(null)
            }
        }

        viewModel.networkErrorLiveData.observe(viewLifecycleOwner) {
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
                                        viewModel.getExperience()
                                    }
                                }
                            }
                        )
                    }
                }
                viewModel.networkErrorLiveData.postValue(null)
            }

        }
    }
}