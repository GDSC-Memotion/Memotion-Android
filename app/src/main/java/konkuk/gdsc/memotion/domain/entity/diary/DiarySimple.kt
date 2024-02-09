package konkuk.gdsc.memotion.domain.entity.diary

import android.icu.util.Calendar
import konkuk.gdsc.memotion.domain.entity.emotion.Emotion
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