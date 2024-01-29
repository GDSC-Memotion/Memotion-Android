package konkuk.gdsc.memotion.util

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.TypedValue

fun dpToPx(context: Context, dp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
}

val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd EEE HH:mm:ss") // 2024.01.01 MON 00:00:00의 형태

fun calendarToString(date: Calendar): String {
    return simpleDateFormat.format(date)
}