package konkuk.gdsc.memotion.util

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.TypedValue

fun dpToPx(context: Context, dp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
}

val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd EEE HH:mm:ss") // 2024.01.01 MON 00:00:00의 형태
val simpleDateFormatDate = SimpleDateFormat("yyyy.MM.dd") // 2024.01.01의 형태
val simpleDateFormatTime = SimpleDateFormat("HH:mm:ss") // 00:00:00의 형태
val simpleDateFormatNoti = SimpleDateFormat("HH:mm") // 00:00의 형태

fun calendarToString(date: Calendar): String {
    return simpleDateFormat.format(date)
}

fun calendarToStringWithoutTime(cal: Calendar): String {
    val result = simpleDateFormatDate.format(cal)
    val day = when(cal.get(Calendar.DAY_OF_WEEK)) {
        1 -> "SUN"
        2 -> "MON"
        3 -> "THU"
        4 -> "WED"
        5 -> "THR"
        6 -> "FRI"
        7 -> "SAT"
        else -> "error"
    }

    return "$result $day"
}

fun calendarToStringTime(cal: Calendar): String {
    return simpleDateFormatTime.format(cal)
}

fun calendarToStringNoti(cal: Calendar): String {
    return simpleDateFormatNoti.format(cal)
}