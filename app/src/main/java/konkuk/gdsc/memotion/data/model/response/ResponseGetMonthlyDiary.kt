package konkuk.gdsc.memotion.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseGetMonthlyDiary(
    @SerialName("result")
    val result: MonthlyDiary,
) {
    @Serializable
    data class MonthlyDiary(
        @SerialName("emotions")
        val emotions: List<String>,
    )
}