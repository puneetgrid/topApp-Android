package com.example.topApp.views.question

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.topApp.R
import com.example.topApp.databinding.QuestionFragmentBinding
import com.example.topApp.interfaces.ClickCallbackListener
import com.example.topApp.interfaces.DialogCallback
import com.example.topApp.utils.Utility
import com.example.topApp.views.base.BaseFragment


class QuestionFragment : BaseFragment<QuestionFragmentBinding, QuestionVM>() {
    var questionRVAdapter: QuestionRVAdapter? = null

    override fun setLayout() = R.layout.question_fragment

    override fun setViewModel() = QuestionVM::class.java

    override fun bindViews(viewModel: QuestionVM) {
        binding.viewModel = viewModel
        val bundle = arguments ?: return
        val args = QuestionFragmentArgs.fromBundle(bundle)
        viewModel.titleField.set(args.title ?: "")
        viewModel.isProgressShow.set(true)
        viewModel.getData()
        initView()
        bindObserver()
    }


    private fun initView() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        questionRVAdapter = QuestionRVAdapter(object : ClickCallbackListener {})
        binding.recyclerView.adapter = questionRVAdapter
    }

    fun bindObserver() {
        viewModel.questionLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.isProgressShow.set(false)
            if (!it.isNullOrEmpty()) {
                questionRVAdapter?.submitList(it)
                questionRVAdapter?.notifyDataSetChanged()
                viewModel.questionLiveData.postValue(null)
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