package com.example.topApp.views.userinfo

import com.example.topApp.R
import com.example.topApp.databinding.UserInfoFragmentBinding
import com.example.topApp.interfaces.DialogCallback
import com.example.topApp.utils.Utility
import com.example.topApp.views.base.BaseFragment


class UserInfoFragment : BaseFragment<UserInfoFragmentBinding, UserInfoVM>() {

    override fun setLayout() = R.layout.user_info_fragment

    override fun setViewModel() = UserInfoVM::class.java

    override fun bindViews(viewModel: UserInfoVM) {
        binding.viewModel = viewModel

        val bundle = arguments ?: return
        val args = UserInfoFragmentArgs.fromBundle(bundle)
        viewModel.uid = args.uid ?: ""

        binding.submitButton.setOnClickListener {
            viewModel.isProgressShow.set(true)
            viewModel.createUSer()
        }
        bindObserver()
    }

    private fun bindObserver() {
        viewModel.successsLiveData.observe(viewLifecycleOwner) {
            viewModel.isProgressShow.set(false)
            if (it != null) {
                if (it == true) {
                    navigate(
                        R.id.userInfoFragment,
                        destinationId = R.id.action_userInfoFragment_to_homeFragment
                    )
                }
                viewModel.successsLiveData.postValue(null)
            }
        }

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
                                        viewModel.createUSer()
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