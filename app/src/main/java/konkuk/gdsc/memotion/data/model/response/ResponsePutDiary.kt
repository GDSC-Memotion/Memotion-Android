package konkuk.gdsc.memotion.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePutDiary(
    @SerialName("result")
    val result: PutDiary,
) {
    @Serializable
    data class PutDiary(
        @SerialName("diaryId")
        val diaryId: Long,
//        @SerialName("emotion")
//        val emotion: String,
    )
}
