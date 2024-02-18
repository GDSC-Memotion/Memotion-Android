package konkuk.gdsc.memotion.data.model.response

import android.icu.util.Calendar
import konkuk.gdsc.memotion.domain.entity.diary.DiaryDetail
import konkuk.gdsc.memotion.domain.entity.emotion.Emotion
import konkuk.gdsc.memotion.domain.entity.emotion.EmotionResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    ) {
        @Serializable
        data class AnalysisResult(
            @SerialName("emotion")
            val emotion: String,
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
            return DiaryDetail(
                diaryId = diaryId,
                date = Calendar.getInstance().toString(),
                imageUrls = imageUris,
                content = description,
                emotions = analysisResult.asEmotionResult(),
                youtubeUrl = youtubeUri,
                youtubeMusicUrl = youtubeMusicUri,
            )
        }
    }
}
