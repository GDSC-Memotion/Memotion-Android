package konkuk.gdsc.memotion.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPostDiary(
    @SerialName("description")
    val description: String,
    @SerialName("time")
    val time: String,   // 2024.02.08 MON 21:28:24  형식
)
