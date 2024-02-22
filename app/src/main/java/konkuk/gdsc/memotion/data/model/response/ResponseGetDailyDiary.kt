package konkuk.gdsc.memotion.data.model.response

import konkuk.gdsc.memotion.domain.entity.diary.DiarySimple
import konkuk.gdsc.memotion.domain.entity.emotion.Emotion
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Locale

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
        val mood: String,
        @SerialName("createdAt")
        val createdAt: String,
        @SerialName("imageUris")
        val imageUris: List<String>,
    ) {
        fun asDiarySimple(): DiarySimple {
            val emotion = when (mood) {
                Emotion.ANGER.toString().lowercase(Locale.getDefault()) -> Emotion.ANGER
                Emotion.DISGUST.toString().lowercase(Locale.getDefault()) -> Emotion.DISGUST
                Emotion.FEAR.toString().lowercase(Locale.getDefault()) -> Emotion.FEAR
                Emotion.JOY.toString().lowercase(Locale.getDefault()) -> Emotion.JOY
                Emotion.NEUTRAL.toString().lowercase(Locale.getDefault()) -> Emotion.NEUTRAL
                Emotion.SADNESS.toString().lowercase(Locale.getDefault()) -> Emotion.SADNESS
                Emotion.SURPRISE.toString().lowercase(Locale.getDefault()) -> Emotion.SURPRISE
                else -> Emotion.ANGER
            }
            return DiarySimple(
                diaryId = id,
                date = createdAt,
                emotion = emotion,
                content = description,
                imageUrl = imageUris
            )
        }
    }
}