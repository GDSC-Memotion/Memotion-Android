package konkuk.gdsc.memotion.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePostDiary(
    @SerialName("result")
    val result: ResultPostDiary
) {
    @Serializable
    data class ResultPostDiary(
        @SerialName("id")
        val id: Long,
        @SerialName("emotion")
        val emotion: String,
    )
}
