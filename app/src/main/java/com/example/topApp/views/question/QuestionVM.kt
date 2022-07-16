package com.example.topApp.views.question

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.topApp.extentionClasses.ResourceProvider
import com.example.topApp.repo.AppRepository
import com.example.topApp.utils.FirebaseUtils
import com.example.topApp.utils.NetworkLiveData
import com.example.topApp.utils.Utility
import com.example.topApp.views.base.BaseViewModel
import com.example.topApp.views.modal.CategoryData
import com.example.topApp.views.modal.QuestionData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionVM @Inject constructor(
    networkLiveData: NetworkLiveData,
    val resourceProvider: ResourceProvider,
    val repository: AppRepository,
    val firebaseUtils: FirebaseUtils
) : BaseViewModel(networkLiveData) {
    var titleField = ObservableField("")
    var isProgressShow = ObservableBoolean(false)
    var errorLiveData = MutableLiveData<String>()
    var questionLiveData = MutableLiveData<List<QuestionData?>?>()

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (mNetworkErrorData.value?.status == false) {
                errorLiveData.postValue(Utility.networkError)
                return@launch
            }
            val questionList: ArrayList<QuestionData> = ArrayList()
            questionList.clear()
            firebaseUtils.fireStoreDatabase.collection("questions").get()
                .addOnSuccessListener { querySnapshot ->
                    querySnapshot.forEach { document ->
                        Utility.printLog("question data", "documents ${document.data}")

                        questionList.add(
                            QuestionData(
                                id = document.id,
                                title = document.getString("title"),
                                answer1 = document.getString("answer1"),
                                answer2 = document.getString("answer2")
                            )
                        )
                    }
                    questionLiveData.postValue(questionList)
                }

                .addOnFailureListener { exception ->
                    Utility.printLog("question error", "Error getting documents $exception")
                }


        }

    }

}