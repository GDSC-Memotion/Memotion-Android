package konkuk.gdsc.memotion.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPutDiary(
    @SerialName("description")
    val description: String,
    @SerialName("imageUris")
    val imageUris: List<String>,
)
