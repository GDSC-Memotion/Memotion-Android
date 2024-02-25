package konkuk.gdsc.memotion.ui.diary.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import konkuk.gdsc.memotion.data.model.response.ResponseGetDetailDiary
import konkuk.gdsc.memotion.domain.entity.diary.DiaryDetail
import konkuk.gdsc.memotion.domain.repository.DiaryRepository
import konkuk.gdsc.memotion.util.NETWORK
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryDetailViewModel @Inject constructor(
    val diaryRepository: DiaryRepository
) : ViewModel() {

    private val _diary = MutableLiveData<DiaryDetail>()
    val diary: LiveData<DiaryDetail> = _diary

    fun getDiaryData(diaryId: Long) {
        viewModelScope.launch {
            diaryRepository.getDetailDiary(diaryId)
                .onSuccess {
                    Log.d(NETWORK, "DiaryDetailViewModel - getDiaryData() called\nsuccess")
                    _diary.value = it.result.asDiaryDetail()
                }
                .onFailure {
                    Log.d(NETWORK, "DiaryDetailViewModel - getDiaryData() called\nfailed")
                }
        }
    }

    fun deleteDiary() {
        viewModelScope.launch {
            diaryRepository.deleteDiary(diary.value?.diaryId ?: 0)
                .onSuccess {
                    Log.d(NETWORK, "DiaryDetailViewModel - deleteDiary() called\nsuccess")
                }
                .onFailure {
                    Log.d(NETWORK, "DiaryDetailViewModel - deleteDiary() called\nfailed\nbecause${it}")
                }
        }
    }
}