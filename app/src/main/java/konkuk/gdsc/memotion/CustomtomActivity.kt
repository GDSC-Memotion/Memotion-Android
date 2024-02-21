package konkuk.gdsc.memotion

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import konkuk.gdsc.memotion.databinding.ActivityCustomtomBinding
import konkuk.gdsc.memotion.ui.diary.MonthDialog
import konkuk.gdsc.memotion.util.TAG
import java.time.LocalDate
import java.util.Locale
import kotlin.math.log

class CustomtomActivity : AppCompatActivity() {
    lateinit var binding: ActivityCustomtomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomtomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.customCalendar.setContent {
            CalendarCustom()
            Log.d(TAG, "onCreate: ")
        }


    }

    @Composable
    fun CalendarCustom() {
        Log.d(TAG, "CalendarCustom: ")
        val currentDate = remember { mutableStateOf(LocalDate.now()) } //현재 보여줄 달력 날짜의 기준
        val isMonthDialogOpen = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .padding(16.dp),
        ) {
            YearHeader(currentDate.value, onSelect = {
                currentDate.value = it
            })
            MonthHeader(currentDate.value, onSelect = {
                isMonthDialogOpen.value = true
            })
            DayHeader()
            CalendarPage(currentDate.value)

            if (isMonthDialogOpen.value) {
                MonthDialog(
                    currentDate = currentDate.value,
                    onMonthSelected = {
                        currentDate.value =
                            LocalDate.of(currentDate.value.year, it, currentDate.value.dayOfMonth)
                        isMonthDialogOpen.value = false
                    },
                    onDismissRequest = { isMonthDialogOpen.value = false }
                )
            }
        }
    }

    //Custom Calendar
    @Composable
    fun YearHeader(
        selectedDate: LocalDate,
        onSelect: (LocalDate) -> Unit
    ) {
        Log.d(TAG, "YearHeader: ")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, top = 20.dp, end = 0.dp, bottom = 0.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier
                    .padding(4.dp, 0.dp)
                    .size(20.dp)
                    .clickable {
                        onSelect(selectedDate.minusYears(1))
                    },
                painter = painterResource(id = R.drawable.chevron_left),
                contentDescription = "previous",
            )
            Text(
                text = "${selectedDate.year}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                modifier = Modifier
                    .padding(4.dp, 0.dp)
                    .size(20.dp)
                    .clickable {
                        onSelect(selectedDate.plusYears(1))
                    },
                painter = painterResource(id = R.drawable.chevron_right),
                contentDescription = "next"
            )
        }
    }

    @Composable
    fun MonthHeader(
        currentDate: LocalDate,
        onSelect: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, top = 4.dp, end = 0.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            val capitalizedMonth =
                currentDate.month.toString().substring(0, 1).uppercase(Locale.ROOT) +
                        currentDate.month.toString().substring(1).lowercase(Locale.ROOT)
            Row(modifier = Modifier.clickable { onSelect() }) {
                Text(
                    text = capitalizedMonth,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Icon(
                    modifier = Modifier.padding(start = 0.dp, top = 4.dp, end = 0.dp, bottom = 0.dp),
                    painter = painterResource(id = R.drawable.chevron_down),
                    contentDescription = "down"
                )
            }
        }
    }

    @Composable
    fun DayHeader() { //요일
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val dayOfWeek = arrayOf("sun", "mon", "tue", "wed", "thr", "fri", "sat")
            var textColor = Color.Red

            for (i in dayOfWeek) {
                if (i != "sun") textColor = Color.Black
                Text(
                    text = i,
                    color = textColor,
                    modifier = Modifier.width(36.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }

    @Composable
    fun CalendarPage(
        currentDate: LocalDate,
    ) {
        Column() {
            var checkDate = remember { mutableStateOf(currentDate) } //선택된 날짜 넘김
            var rowCnt = 0 //행수
            var date = 0
            var end = false

            val lastDayOfMonth =
                LocalDate.of(currentDate.year, currentDate.monthValue, 1).plusMonths(1)
                    .minusDays(1) //달의 마지막 구하기
            val lastDay = lastDayOfMonth.dayOfMonth

            while (!end) {
                Row(
                    modifier = Modifier
                        .padding(12.dp, 0.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (i in 0..6) {
                        var firstDayOfWeek = LocalDate.of(currentDate.year, currentDate.monthValue, 1)
                        if ((rowCnt == 0) and (firstDayOfWeek.dayOfWeek.value > i)) { //시작일 전에 빈칸
                            DayContainer(
                                dayOfWeek = null,
                                date = -1,
                                currentDate,
                                checkDate.value.dayOfMonth,
                                onCheck = {
                                    checkDate.value =
                                        LocalDate.of(currentDate.year, currentDate.month, it)
                                })
                        } else if (date > lastDay - 1) { //마지막 날 이후 빈칸
                            DayContainer(
                                dayOfWeek = null,
                                date = -1,
                                currentDate,
                                checkDate.value.dayOfMonth,
                                onCheck = {
                                    checkDate.value =
                                        LocalDate.of(currentDate.year, currentDate.month, it)
                                }
                            )
                            if (i == 6) {
                                end = true
                                break
                            }
                        } else {
                            date += 1
                            DayContainer(
                                dayOfWeek = i+1,
                                date = date,
                                currentDate,
                                checkDate.value.dayOfMonth,
                                onCheck = {
                                    checkDate.value =
                                        LocalDate.of(currentDate.year, currentDate.month, it)
                                })
                        }
                    }
                }
                rowCnt++
            }
        }
    }

    @Composable
    fun DayContainer(
        dayOfWeek: Int?,
        date: Int,
        currentDate: LocalDate,
        checkDate: Int,
        onCheck: (Int) -> Unit,
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp, 8.dp)
                .clickable { onCheck(date) },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var drawList = intArrayOf(0, 0) //drawBehind의 color과 cornerRadius값
            var paddingList = intArrayOf(0, 0) //padding의 hor과 ver 값
            var borderList = intArrayOf(0, 0) //border 굵기와 color
            var textColor = Color.Black

            if (dayOfWeek == null) { //빈칸용
                Text(text = "", fontSize = 12.sp)
                Spacer(modifier = Modifier.size(4.dp))
                Icon(
                    modifier = Modifier.size(36.dp),
                    painter = painterResource(id = R.drawable.ic_empty_circle_null),
                    contentDescription = "empty"
                )
            } else { //1~말일까지
                if (LocalDate.now() == LocalDate.of(
                        currentDate.year,
                        currentDate.month,
                        date
                    )
                ) { //오늘날짜 표시
                    if (date < 10) {
                        drawList = intArrayOf(0xFF555555.toInt(), 10)
                        paddingList = intArrayOf(10, 0)
                        textColor = Color.White
                    } else {
                        drawList = intArrayOf(0xFF555555.toInt(), 10)
                        paddingList = intArrayOf(8, 0)
                        textColor = Color.White
                    }
                } else if ((checkDate == date) and (checkDate != LocalDate.now().dayOfMonth)) {  //선택된날 테두리 표시
                    if (date < 10) {
                        drawList = intArrayOf(0xFFFFFFFF.toInt(), 20)
                        paddingList = intArrayOf(10, 0)
                        borderList = intArrayOf(3, 0xFF555555.toInt())
                    } else {
                        drawList = intArrayOf(0xFFFFFFFF.toInt(), 20)
                        paddingList = intArrayOf(8, 0)
                        borderList = intArrayOf(3, 0xFF555555.toInt())
                    }
                    if (dayOfWeek == 1) textColor = Color.Red
                } else if (dayOfWeek == 1) textColor = Color.Red //일요일은 붉은색

                Text(
                    text = date.toString(),
                    fontSize = 12.sp,
                    color = textColor,
                    modifier = Modifier
                        .drawBehind {
                            drawRoundRect(
                                Color(drawList[0]),
                                cornerRadius = CornerRadius(drawList[1].dp.toPx()),
                            )
                        }
                        .border(2.dp, Color(borderList[1]), shape = CircleShape)
                        .padding(paddingList[0].dp, paddingList[1].dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Icon(
                    modifier = Modifier.size(36.dp),
                    painter = painterResource(id = R.drawable.ic_empty_circle),
                    contentDescription = "empty"
                )
            }
        }
    }
}