package com.example.topApp.views.profile.edit

import androidx.navigation.fragment.findNavController
import com.example.topApp.R
import com.example.topApp.databinding.EditProfileFragmentBinding
import com.example.topApp.interfaces.DialogCallback
import com.example.topApp.utils.Utility
import com.example.topApp.views.base.BaseFragment


class EditProfileFragment : BaseFragment<EditProfileFragmentBinding, EditProfileVM>() {

    override fun setLayout() = R.layout.edit_profile_fragment

    override fun setViewModel() = EditProfileVM::class.java

    override fun bindViews(viewModel: EditProfileVM) {
        binding.viewModel = viewModel

        viewModel.getUserData()
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        bindObserver()
    }

    private fun bindObserver() {
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            viewModel.isProgressShow.set(false)
            if (it != null) {
                when (it) {
                    Utility.errorMessage -> {
                        Utility.showDialog(requireContext(),
                            viewModel.resourceProvider.getString(R.string.app_name),
                            viewModel.errorMessage,
                            viewModel.resourceProvider.getString(R.string.okay),
                            false,
                            object : DialogCallback {
                                override fun callback(any: Any) {}
                            }
                        )
                    }
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
                                        viewModel.getUserData()
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
}