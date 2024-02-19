package konkuk.gdsc.memotion.data.model.response

import konkuk.gdsc.memotion.domain.entity.diary.DiarySimple
import konkuk.gdsc.memotion.domain.entity.emotion.Emotion
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGetDailyDiary(
    @SerialName("result")
    val result: List<DailyDiary>,
) {
    @Serializable
    data class DailyDiary(
        @SerialName("id")
        val id: Long,
        @SerialName("description")
        val description: String,
        @SerialName("memberId")
        val memberId: Long,
        @SerialName("mood")
        val mood: Int,
        @SerialName("imageUris")
        val imageUris: List<String>,
    ) {
        fun asDiarySimple(): DiarySimple {
            return DiarySimple(
                diaryId = id,
                date = description,
                emotion = Emotion.values()[mood],
                content = description,
                imageUrl = imageUris
            )
        }
    }
}