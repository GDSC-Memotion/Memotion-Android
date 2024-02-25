package konkuk.gdsc.memotion.domain.entity.diary

import konkuk.gdsc.memotion.data.model.request.RequestPostDiary
import konkuk.gdsc.memotion.data.model.request.RequestPutDiary

data class DiaryWithoutImage(
    val date: String,
    val content: String,
) {
    fun asRequestPostDiary(): RequestPostDiary {
        return RequestPostDiary(
            description = content,
            time = date,
        )
    }
}

data class DiaryWithImage(
    val date: String,
    val content: String,
    var imageUris: List<String>
) {
    fun asRequestPutDiary(): RequestPutDiary {
        return RequestPutDiary(
            description = content,
//            imageUris = imageUris
        )
    }
}