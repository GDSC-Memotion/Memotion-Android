package konkuk.gdsc.memotion.ui.diary

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
//import androidx.compose.material3.LocalTextStyle
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.LocalDate

@Composable
fun MonthDialog(
    currentDate: LocalDate,
    onMonthSelected: (Int) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(),
    ) {
        val monthList = arrayOf(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(size = 16.dp),
                ),
            verticalArrangement = Arrangement.Center
        ) {
            for (month in 1..10 step (3)) {
                Row(
                    modifier = Modifier.padding(2.dp)
                ) {
                    for (i in 0..2) {
                        Column(
                            modifier = Modifier.padding(3.dp)
                        ) {

                            if (currentDate.monthValue == month + i) {
                                FilledButton(
                                    month = monthList[month + i - 1],
                                    onClick = {
                                        onMonthSelected(month + i)
                                        Log.d("monthDialog", "달력 클릭 감지 : ${month+i}")
                                    })
                            } else {
                                OutlinedButton(
                                    month = monthList[month + i - 1],
                                    onClick = {
                                        onMonthSelected(month + i)
                                        Log.d("monthDialog", "달력 클릭 감지 : ${month+i}")
                                    })
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun FilledButton(
    month: String,
    onClick: () -> Unit
) {
//    Button(
//        modifier = Modifier.requiredWidth(100.dp),
//        onClick = { onClick() }
//    ) {
//        Text(
//            text = month,
//            fontSize = 10.sp,
//            fontWeight = FontWeight.Normal,
//            textAlign = TextAlign.Center,
//            color = Color.White
//        )
//    }
    Text(
        text = month,
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        color = Color.White,
        modifier = Modifier
            .drawBehind {
                drawRoundRect(
                    Color(0xFFF49B4D),
                    cornerRadius = CornerRadius(16.dp.toPx())
                )
            }
            .padding(top = 8.dp)
            .size(100.dp, 26.dp)
            .clickable { onClick() }
    )
}

@Composable
fun OutlinedButton(
    month: String,
    onClick: () -> Unit
) {
//    OutlinedButton(
//        modifier = Modifier.requiredWidth(100.dp),
//        onClick = { onClick() }
//    ) {
//        Text(
//            text = month,
//            fontSize = 10.sp,
//            fontWeight = FontWeight.Normal,
//            textAlign = TextAlign.Center,
//            color = Color.Black
//        )
//    }
    Text(
        text = month,
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        style = LocalTextStyle.current.merge(
            TextStyle(
                lineHeight = 2.5.em,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.None
                )
            )
        ),
        modifier = Modifier
            .size(100.dp, 35.dp)
            .border(border = BorderStroke(1.dp, Color(0xff8b8b8b)), shape = CircleShape)
            .clickable { onClick() }
    )
}


