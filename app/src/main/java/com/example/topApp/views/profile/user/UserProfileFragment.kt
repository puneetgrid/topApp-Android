package com.example.topApp.views.profile.user

import com.example.topApp.R
import com.example.topApp.databinding.UserProfileFragmentBinding
import com.example.topApp.interfaces.DialogCallback
import com.example.topApp.utils.Utility
import com.example.topApp.views.base.BaseFragment


class UserProfileFragment : BaseFragment<UserProfileFragmentBinding, UserProfileVM>() {

    override fun setLayout() = R.layout.user_profile_fragment

    override fun setViewModel() = UserProfileVM::class.java

    override fun bindViews(viewModel: UserProfileVM) {
        binding.viewModel = viewModel

        viewModel.getUserData()
        binding.ivArrow.setOnClickListener {
            navigate(
                R.id.userProfileFragment,
                destinationId = R.id.action_userProfileFragment_to_editProfileFragment
            )
        }

        binding.tvPartnerView.setOnClickListener {

            val directions =
                UserProfileFragmentDirections.actionUserProfileFragmentToExperienceDetailsFragment(
                    true
                )
            navigate(R.id.userProfileFragment, directions = directions)

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