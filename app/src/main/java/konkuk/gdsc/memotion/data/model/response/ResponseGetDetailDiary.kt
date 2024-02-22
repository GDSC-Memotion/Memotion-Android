package konkuk.gdsc.memotion.data.model.response

import android.icu.util.Calendar
import konkuk.gdsc.memotion.domain.entity.diary.DiaryDetail
import konkuk.gdsc.memotion.domain.entity.diary.DiaryWriting
import konkuk.gdsc.memotion.domain.entity.emotion.Emotion
import konkuk.gdsc.memotion.domain.entity.emotion.EmotionResult
import konkuk.gdsc.memotion.util.calendarToString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
data class ResponseGetDetailDiary(
    @SerialName("result")
    val result: DetailDiary,
) {
    @Serializable
    data class DetailDiary(
        @SerialName("diaryId")
        val diaryId: Long,
        @SerialName("imageUris")
        val imageUris: List<String>,
        @SerialName("description")
        val description: String,
        @SerialName("analysisResult")
        val analysisResult: AnalysisResult,
        @SerialName("youtubeUri")
        val youtubeUri: String,
        @SerialName("youtubeMusicUri")
        val youtubeMusicUri: String,
        @SerialName("emotion")
        val emotion: String,
        @SerialName("createdAt")
        val createdAt: String
    ) {
        @Serializable
        data class AnalysisResult(
            @SerialName("joy")
            val joy: Float,
            @SerialName("neutral")
            val neutral: Float,
            @SerialName("sadness")
            val sadness: Float,
            @SerialName("surprise")
            val surprise: Float,
            @SerialName("anger")
            val anger: Float,
            @SerialName("fear")
            val fear: Float,
            @SerialName("disgust")
            val disgust: Float,
        ) {
            fun asEmotionResult(): List<EmotionResult> {
                return EmotionResult.sample
            }
        }

        fun asDiaryDetail(): DiaryDetail {
            val titleEmotion = when (emotion) {
                Emotion.ANGER.toString().lowercase(Locale.getDefault()) -> Emotion.ANGER
                Emotion.DISGUST.toString().lowercase(Locale.getDefault()) -> Emotion.DISGUST
                Emotion.FEAR.toString().lowercase(Locale.getDefault()) -> Emotion.FEAR
                Emotion.JOY.toString().lowercase(Locale.getDefault()) -> Emotion.JOY
                Emotion.NEUTRAL.toString().lowercase(Locale.getDefault()) -> Emotion.NEUTRAL
                Emotion.SADNESS.toString().lowercase(Locale.getDefault()) -> Emotion.SADNESS
                Emotion.SURPRISE.toString().lowercase(Locale.getDefault()) -> Emotion.SURPRISE
                else -> Emotion.ANGER
            }

            return DiaryDetail(
                diaryId = diaryId,
                date = createdAt,
                imageUrls = imageUris,
                content = description,
                titleEmotion = titleEmotion,
                emotions = analysisResult.asEmotionResult(),
                youtubeUrl = youtubeUri,
                youtubeMusicUrl = youtubeMusicUri,
            )
        }

        fun asDiaryWriting(): DiaryWriting {
            return DiaryWriting(
                date = createdAt,
                imageUrls = imageUris,
                content = description
            )
        }
    }
}
