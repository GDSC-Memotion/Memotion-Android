package konkuk.gdsc.memotion.domain.entity.diary

import android.icu.util.Calendar
import konkuk.gdsc.memotion.util.calendarToString

data class DiaryWriting(
    val date: String,
    val imageUrls: List<String> = listOf(),
    val content: String,
) {
    companion object {
        val sample = DiaryWriting(
            calendarToString(Calendar.getInstance()),
            listOf(),
            "안녕하세요"
        )
    }
}
