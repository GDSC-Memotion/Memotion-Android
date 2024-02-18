package konkuk.gdsc.memotion.ui.diary.create

import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import konkuk.gdsc.memotion.domain.entity.diary.DiaryWriting
import konkuk.gdsc.memotion.domain.entity.user.User
import konkuk.gdsc.memotion.domain.repository.DiaryRepository
import javax.inject.Inject

@HiltViewModel
class WritingDiaryViewModel @Inject constructor(
    val diaryRepository: DiaryRepository
) : ViewModel() {
    private val _version = MutableLiveData<DiaryVersion>()
    val version: LiveData<DiaryVersion> = _version

    private val _diaryData = MutableLiveData<DiaryWriting>()
    val diaryData: LiveData<DiaryWriting> = _diaryData
}