package konkuk.gdsc.memotion.domain.entity.diary

import android.icu.util.Calendar
import konkuk.gdsc.memotion.domain.entity.emotion.EmotionResult
import konkuk.gdsc.memotion.util.calendarToString

data class DiaryDetail(
    val diaryId: Long,
    val date: String = Calendar.getInstance().toString(),
    val imageUrls: List<String> = listOf(),
    val content: String,
    val emotions: List<EmotionResult>,
    val youtubeUrl: String,
    val youtubeMusicUrl: String,
) {
    companion object {
        val sample = DiaryDetail(
            1,
            calendarToString(Calendar.getInstance()),
            imageUrls = listOf(),
            "안녕하세요",
            EmotionResult.sample,
            "youtube.com/watch?v=DA-vauI8hMI",
            "youtube.com/watch?v=DA-vauI8hMI"
        )
    }
}
