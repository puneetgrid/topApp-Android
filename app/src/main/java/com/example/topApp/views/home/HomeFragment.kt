package com.example.topApp.views.home

import android.view.View
import androidx.lifecycle.Observer
import com.example.topApp.R
import com.example.topApp.databinding.HomeFragmentBinding
import com.example.topApp.interfaces.ClickCallbackListener
import com.example.topApp.interfaces.DialogCallback
import com.example.topApp.utils.Utility
import com.example.topApp.views.base.BaseFragment
import com.example.topApp.views.login.LoginFragmentDirections
import com.example.topApp.views.modal.CategoryData

class HomeFragment : BaseFragment<HomeFragmentBinding, HomeVM>() {
    var homeRVAdapter: HomeRVAdapter? = null

    override fun setLayout() = R.layout.home_fragment

    override fun setViewModel() = HomeVM::class.java

    override fun bindViews(viewModel: HomeVM) {
        binding.viewModel = viewModel
        initView()
        viewModel.isProgressShow.set(true)
        viewModel.getData()
        viewModel.getUserData()
        bindObserver()
        Utility.printLog("Auth data", " ${viewModel.mAuth?.uid.toString()}")
    }

    private fun initView() {
        homeRVAdapter = HomeRVAdapter(
            object : ClickCallbackListener {
                override fun categoryClick(view: View, categoryData: CategoryData) {
                    super.categoryClick(view, categoryData)
                    val directions = HomeFragmentDirections.actionHomeFragmentToQuestionFragment(title = categoryData.title)
                    navigate(R.id.homeFragment, directions = directions)
                }
            })
        binding.recyclerView.adapter = homeRVAdapter
    }

    fun bindObserver(){
        viewModel.categoriesLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.isProgressShow.set(false)
            if (!it.isNullOrEmpty()){
                homeRVAdapter?.submitList(it)
                homeRVAdapter?.notifyDataSetChanged()
                viewModel.categoriesLiveData.postValue(null)
            }
        })

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
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
                                        viewModel.getData()
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
