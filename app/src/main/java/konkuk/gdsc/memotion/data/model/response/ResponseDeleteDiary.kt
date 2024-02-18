package konkuk.gdsc.memotion.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDeleteDiary(
    @SerialName("result")
    val result: DeleteDiary,
) {
    @Serializable
    data class DeleteDiary(
        @SerialName("diaryId")
        val diaryId: Long,
    )
}
