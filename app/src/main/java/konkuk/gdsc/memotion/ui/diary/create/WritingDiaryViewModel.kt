package konkuk.gdsc.memotion.ui.diary.create

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import konkuk.gdsc.memotion.domain.entity.diary.DiaryWithImage
import konkuk.gdsc.memotion.domain.entity.diary.DiaryWriting
import konkuk.gdsc.memotion.domain.entity.emotion.Emotion
import konkuk.gdsc.memotion.domain.repository.DiaryRepository
import konkuk.gdsc.memotion.util.NETWORK
import konkuk.gdsc.memotion.util.calendarToString
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WritingDiaryViewModel @Inject constructor(
    val diaryRepository: DiaryRepository
) : ViewModel() {
    private val _version = MutableLiveData<DiaryVersion>()
    val version: LiveData<DiaryVersion> = _version

    private val _diaryData = MutableLiveData<DiaryWriting>()
    val diaryData: LiveData<DiaryWriting> = _diaryData

    private val _newImages = MutableLiveData<MutableList<MultipartBody.Part>>()
    val newImages: LiveData<MutableList<MultipartBody.Part>> = _newImages

    private val _emotionResult = MutableLiveData<Emotion>()
    val emotionResult: LiveData<Emotion> = _emotionResult

    private val _diaryId = MutableLiveData<Long>()
    val diaryId: LiveData<Long> = _diaryId

    init {
        _version.value = DiaryVersion.WRITING
        _diaryData.value = DiaryWriting(
            date = calendarToString(Calendar.getInstance()),
            content = "",
            imageUrls = listOf()
        )
        _newImages.value = mutableListOf()
    }

    fun postDiary() {
        viewModelScope.launch {
            val diary = diaryData.value?.asDiaryWithoutImages()
                ?: DiaryWriting.sample.asDiaryWithoutImages()
            diaryRepository.postDiary(diary, newImages.value ?: listOf())
                .onSuccess {
                    Log.d(NETWORK, "WritingDiaryViewModel - postDiary() called\nsuccess")
                    _emotionResult.value = when (it.result.emotion.uppercase(Locale.getDefault())) {
                        "ANGER" -> Emotion.ANGER
                        "DISGUST" -> Emotion.DISGUST
                        "FEAR" -> Emotion.FEAR
                        "JOY" -> Emotion.JOY
                        "NEUTRAL" -> Emotion.NEUTRAL
                        "SADNESS" -> Emotion.SADNESS
                        "SURPRISE" -> Emotion.SURPRISE
                        else -> Emotion.ANGER
                    }
                }
                .onFailure {
                    Log.d(NETWORK, "WritingDiaryViewModel - postDiary() called\nfailed\nbecause:$it")
                }
        }
    }

    fun editDiary() {
        viewModelScope.launch {
            diaryRepository.putDiary(
                diaryId = diaryId.value ?: 0,
                diary = diaryData.value?.asDiaryWithoutDate() ?: DiaryWithImage("", "", listOf()),
                newImages = newImages.value?.toList() ?: listOf()
                )
                .onSuccess {
                    Log.d(NETWORK, "WritingDiaryViewModel - putDiary() called\nsuccess")
                    _emotionResult.value = Emotion.ANGER
                        /*when (it.result.emotion.uppercase(Locale.getDefault())) {
                        "ANGER" -> Emotion.ANGER
                        "DISGUST" -> Emotion.DISGUST
                        "FEAR" -> Emotion.FEAR
                        "JOY" -> Emotion.JOY
                        "NEUTRAL" -> Emotion.NEUTRAL
                        "SADNESS" -> Emotion.SADNESS
                        "SURPRISE" -> Emotion.SURPRISE
                        else -> Emotion.ANGER
                    }*/
                }
                .onFailure {
                    Log.d(NETWORK, "WritingDiaryViewModel - putDiary() called\nfailed\nbecause:$it")
                }
        }
    }

    fun setVersion(version: Int) {
        _version.value = when (version) {
            1 -> DiaryVersion.EDITING
            else -> DiaryVersion.WRITING
        }
    }

    fun setDiaryData(data: DiaryWriting) {
        _diaryData.value = data
    }

    fun setEmotionResult(emotion: Emotion) {
        _emotionResult.value = emotion
    }

    fun updateImages(images: List<MultipartBody.Part>) {
        _newImages.value = images.toMutableList()
    }

    fun setEditingDiaryData(diaryId: Long) {
        _diaryId.value = diaryId
        viewModelScope.launch {
            diaryRepository.getDetailDiary(diaryId)
                .onSuccess {
                    Log.d(NETWORK, "WritingDiaryViewModel - setEditingDiaryData() called\nsuccess")
                    _diaryData.value = it.result.asDiaryWriting()
                }
                .onFailure {
                    Log.d(NETWORK, "WritingDiaryViewModel - setEditingDiaryData() called\nfailed\nbecause${it}")
                }
        }
    }
}