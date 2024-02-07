package konkuk.gdsc.memotion.data

import android.icu.util.Calendar
import konkuk.gdsc.memotion.util.calendarToString

data class DiarySimple(
    val date: String,
    val emotion: Emotion,
    val content: String,
    val imageUrl: String?,
) {
    companion object {
        val sample: List<DiarySimple> = listOf(
            DiarySimple(calendarToString(Calendar.getInstance()), Emotion.ANGER, "안녕하세요1", ""),
            DiarySimple(calendarToString(Calendar.getInstance()), Emotion.ANGER, "안녕하세요2", ""),
            DiarySimple(calendarToString(Calendar.getInstance()), Emotion.ANGER, "안녕하세요3", ""),
            DiarySimple(calendarToString(Calendar.getInstance()), Emotion.ANGER, "안녕하세요4", ""),
        )
    }
}

data class DiaryDetail(
    val date: String,
    val imageUrls: List<String>?,
    val content: String,
    val emotions: List<EmotionResult>,
    val youtubeUrl: String,
) {
    companion object {
        val sample = DiaryDetail(
            calendarToString(Calendar.getInstance()),
            null,
            "안녕하세요",
            EmotionResult.sample,
            "youtube.com/watch?v=DA-vauI8hMI"
        )
    }
}

data class DiaryWriting(
    val date: String,
    val imageUrls: List<String>?,
    val content: String,
) {
    companion object {
        val sample = DiaryWriting(
            calendarToString(Calendar.getInstance()),
            null,
            "안녕하세요"
        )
    }
}

data class DiaryMonth(
    val date: Int,
    val emotion: Emotion,
) {

}