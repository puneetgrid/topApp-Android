package com.example.topApp.views.addexperience

import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.topApp.R
import com.example.topApp.databinding.AddExperienceFragmentBinding
import com.example.topApp.interfaces.ClickCallbackListener
import com.example.topApp.interfaces.DialogCallback
import com.example.topApp.utils.Utility
import com.example.topApp.views.base.BaseFragment
import com.example.topApp.views.modal.CategoryData


class AddExperienceFragment : BaseFragment<AddExperienceFragmentBinding, AddExperienceVM>() {

    var categoryRVAdapter: CategoryRVAdapter? = null

    override fun setLayout() = R.layout.add_experience_fragment

    override fun setViewModel() = AddExperienceVM::class.java

    override fun bindViews(viewModel: AddExperienceVM) {
        binding.viewModel = viewModel
        initView()
        viewModel.isProgressShow.set(true)
        viewModel.getCategoryData()
        bindObserver()
        viewModel.years.set("")
        viewModel.area.set("")
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.submitButton.setOnClickListener {
            viewModel.isProgressShow.set(true)
            viewModel.saveExperienceData()
        }
    }

    private fun initView() {
        categoryRVAdapter = CategoryRVAdapter(
            object : ClickCallbackListener {
                override fun categoryClick(view: View, categoryData: CategoryData) {
                    super.categoryClick(view, categoryData)

                    viewModel.categoryList.forEach {
                        it.isSelected = it.id == categoryData.id
                    }
                    categoryRVAdapter?.notifyDataSetChanged()
                    viewModel.categoryId = categoryData.id ?: ""
                    viewModel.categoryName = categoryData.title ?: ""
                }
            })
        binding.recyclerView.adapter = categoryRVAdapter
    }

    private fun bindObserver() {

        viewModel.successsLiveData.observe(viewLifecycleOwner) {
            viewModel.isProgressShow.set(false)
            if (it != null) {
                if (it == true) {
                    findNavController().navigateUp()
                }
                viewModel.successsLiveData.postValue(null)
            }
        }

        viewModel.categoriesLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.isProgressShow.set(false)
            if (!it.isNullOrEmpty()) {
                categoryRVAdapter?.submitList(it)
                categoryRVAdapter?.notifyDataSetChanged()
                viewModel.categoriesLiveData.postValue(null)
            }
        })

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
                                        viewModel.saveExperienceData()
                                    }
                                }
                            }
                        )
                    }
                }
                viewModel.networkErrorLiveData.postValue(null)
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
                                        viewModel.isProgressShow.set(true)
                                        viewModel.getCategoryData()
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