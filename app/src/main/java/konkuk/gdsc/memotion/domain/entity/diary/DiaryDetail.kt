package konkuk.gdsc.memotion.domain.entity.diary

import android.icu.util.Calendar
import konkuk.gdsc.memotion.domain.entity.emotion.EmotionResult
import konkuk.gdsc.memotion.util.calendarToString

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
