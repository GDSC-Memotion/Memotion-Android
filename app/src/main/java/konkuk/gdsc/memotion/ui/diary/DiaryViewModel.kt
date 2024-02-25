package konkuk.gdsc.memotion.ui.diary

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import konkuk.gdsc.memotion.domain.entity.diary.DiarySimple
import konkuk.gdsc.memotion.domain.repository.DiaryRepository
import konkuk.gdsc.memotion.util.NETWORK
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository
) : ViewModel() {

    private val _currentDay = MutableLiveData<Calendar>()
    val currentDay: LiveData<Calendar> = _currentDay

    private val _dailyDiary = MutableLiveData<List<DiarySimple>>()
    val dailyDiary: LiveData<List<DiarySimple>> = _dailyDiary

    private val _monthlyDiary = MutableLiveData<List<String>>()
    val monthlyDiary: LiveData<List<String>> = _monthlyDiary

    init {
        _currentDay.value = Calendar.getInstance()
        _dailyDiary.value = listOf()
        _monthlyDiary.value = listOf()
    }

    fun changeCurrentDay(cal: Calendar) {
        _currentDay.value = cal
    }

    fun getDailyDiary(date: String) {
//        _dailyDiary.value = DiarySimple.sample    // for test
        viewModelScope.launch {
            diaryRepository.getDailyDiary(date)
                .onSuccess {
                    _dailyDiary.value = it.result.map { data ->
                        Log.d(NETWORK, "DiaryViewModel - getDailyDiary() called\nsuccess")
                        data.asDiarySimple()
                    }
                }
                .onFailure {
                    Log.d(NETWORK, "DiaryViewModel - getDailyDiary() called\nfailed")
                }
        }
    }

    fun getMonthlyDiary(period: String) {
        viewModelScope.launch {
            diaryRepository.getMonthlyDiary(period)
                .onSuccess {
                    Log.d(NETWORK, "DiaryViewModel - getMonthlyDiary() called\nsuccess")
                    _monthlyDiary.value = it.result.emotions
                }
                .onFailure {
                    Log.d(NETWORK, "DiaryViewModel - getMonthlyDiary() called\nfailed")
                }
        }
    }

    fun deleteDiary(diaryId: Long) {
        viewModelScope.launch {
            diaryRepository.deleteDiary(diaryId)
                .onSuccess {
                    Log.d(NETWORK, "DiaryViewModel - deleteDiary() called\nsuccess")
                }
                .onFailure {
                    Log.d(NETWORK, "DiaryViewModel - deleteDiary() called\nfailed\nbecause${it}")
                }
        }
    }


}
