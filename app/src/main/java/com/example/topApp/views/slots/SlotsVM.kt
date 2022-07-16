package com.example.topApp.views.slots

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.topApp.extentionClasses.ResourceProvider
import com.example.topApp.repo.AppRepository
import com.example.topApp.utils.DatePickerClass
import com.example.topApp.utils.FirebaseUtils
import com.example.topApp.utils.NetworkLiveData
import com.example.topApp.utils.Utility
import com.example.topApp.views.base.BaseViewModel
import com.example.topApp.views.modal.DayDataClass
import com.example.topApp.views.modal.SlotsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SlotsVM @Inject constructor(
    networkLiveData: NetworkLiveData,
    val resourceProvider: ResourceProvider,
    val repository: AppRepository,
    val firebaseUtils: FirebaseUtils
) : BaseViewModel(networkLiveData) {
    var slotsTimeRVAdapter: SlotsTimeRVAdapter? = null
    var slotsDayRVAdapter: SlotsDayRVAdapter? = null
    var isMorning = ObservableBoolean(true)
    var isProgressShow = ObservableBoolean(false)
    var errorLiveData = MutableLiveData<String>()
    var slotsLiveData = MutableLiveData<List<SlotsData?>?>()
    var appointmentType = "Morning"
    var slotsList: ArrayList<SlotsData> = ArrayList()
    var monthPickerAdapter: MonthPickerAdapter? = null

    val months = listOf(
        Calendar.JANUARY,
        Calendar.FEBRUARY,
        Calendar.MARCH,
        Calendar.APRIL,
        Calendar.MAY,
        Calendar.JUNE,
        Calendar.JULY,
        Calendar.AUGUST,
        Calendar.SEPTEMBER,
        Calendar.OCTOBER,
        Calendar.NOVEMBER,
        Calendar.DECEMBER
    )

    fun getSlotsData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (mNetworkErrorData.value?.status == false) {
                errorLiveData.postValue(Utility.networkError)
                return@launch
            }
            slotsList.clear()
            firebaseUtils.fireStoreDatabase.collection("slots").get()
                .addOnSuccessListener { querySnapshot ->
                    querySnapshot.forEach { document ->
                        Utility.printLog("slots data", "documents ${document.data}")
                        if (document.getString("appointmentType") == appointmentType) {
                            slotsList.add(
                                SlotsData(
                                    id = document.getString("id"),
                                    title = document.getString("title")
                                )
                            )
                        }
                        if (slotsList.size > 1) {
                            slotsList.sortWith(Comparator { o1, o2 -> o1.id!!.compareTo(o2.id!!) })
                        }
                        slotsLiveData.postValue(slotsList)
                    }
                }
                .addOnFailureListener { exception ->
                    Utility.printLog("slots error", "Error getting documents $exception")
                }
        }

    }

    var daysLiveData = MutableLiveData<List<DayDataClass?>?>()
    var daysList: ArrayList<DayDataClass> = ArrayList()
    fun getAllDateOfCurrentMonth(year: Int, month: Int) {
        val yearMonth = YearMonth.of(year, month)
        val firstDayOfTheMonth = yearMonth.atDay(1)
        val datesOfThisMonth = mutableListOf<LocalDate>()
        for (daysNo in 0 until yearMonth.lengthOfMonth()) {
            datesOfThisMonth.add(firstDayOfTheMonth.plusDays(daysNo.toLong()))
        }
        daysList.clear()
        datesOfThisMonth.forEach {
            val d = DatePickerClass().getParsedDate(
                DatePickerClass().formatter("yyyy-MM-dd"), it.toString(),
                DatePickerClass().formatter("dd")
            )
            val day = DatePickerClass().getParsedDate(
                DatePickerClass().formatter("yyyy-MM-dd"), it.toString(),
                DatePickerClass().formatter("EEE")
            )
            daysList.add(DayDataClass(dayTitle = day, dayNumber = d))
        }
        daysLiveData.postValue(daysList)
    }

}