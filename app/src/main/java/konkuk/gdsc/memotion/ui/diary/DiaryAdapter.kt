package konkuk.gdsc.memotion.ui.diary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import konkuk.gdsc.memotion.R
import konkuk.gdsc.memotion.domain.entity.diary.DiarySimple
import konkuk.gdsc.memotion.databinding.ItemCalendarBinding
import konkuk.gdsc.memotion.databinding.ItemDiaryBinding
import konkuk.gdsc.memotion.ui.diary.detail.DiaryDetailActivity
import java.time.LocalDate
import java.util.Locale

class DiaryAdapter(
    private val context: Context,
    private var data: List<DiarySimple>,
    private val fragment: Fragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class DiaryViewHolder(
        val binding: ItemDiaryBinding,
        private val context: Context,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DiarySimple) {
            if (this.layoutPosition == 1) {
                binding.vFirst.visibility = View.VISIBLE
                binding.vSecond.visibility = View.INVISIBLE
            } else {
                binding.vFirst.visibility = View.INVISIBLE
                binding.vSecond.visibility = View.VISIBLE
            }

            binding.diaryContent = item

            binding.clItemDiary.setOnClickListener {
                val intent = Intent(context, DiaryDetailActivity::class.java)
                context.startActivity(intent)
            }

            binding.ivItemDiaryEmotion.setImageResource(item.emotion.getResource())

            /*if(item.imageUrl?.isNotBlank() == true) {
                binding.ivItemDiaryImage.layoutParams.height =
                    (context.resources.displayMetrics.widthPixels - dpToPx(context, 51f)).toInt()
                binding.ivItemDiaryImage.visibility = View.VISIBLE
            } else {
                binding.ivItemDiaryImage.visibility = View.GONE
            }*/
        }
    }

    class CalendarViewHolder(
        private val binding: ItemCalendarBinding,
        private val fragment: Fragment,
    ): RecyclerView.ViewHolder(binding.root) {

        private var _listener: DateChangeListener? = null
        private val listener: DateChangeListener
            get() = requireNotNull(_listener) { "CalendarViewHolder's DateChangeListener is null" }

        fun bind() {

            binding.customCalendar.setContent {
                CalendarCustom()
            }

            setDateChangeListener(fragment as DiaryFragment)

//            binding.calendarDiary.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
//                listener.dateChanged(year, month, dayOfMonth)
//            }
        }

        @Composable
        fun CalendarCustom() {
            var currentDate by remember { mutableStateOf(LocalDate.now()) } //현재 보여줄 달력 날짜의 기준
            var isMonthDialogOpen by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .padding(16.dp, 0.dp),
            ) {
                YearHeader(currentDate, onSelect = {
                    currentDate = it
                })
                MonthHeader(currentDate, onSelect = {
                    isMonthDialogOpen = true
                })
                DayHeader()
                CalendarPage(currentDate)

                if (isMonthDialogOpen) {
                    MonthDialog(
                        currentDate = currentDate,
                        onMonthSelected = {
                            currentDate =
                                LocalDate.of(currentDate.year, it, currentDate.dayOfMonth)
                            isMonthDialogOpen = false
                        },
                        onDismissRequest = { isMonthDialogOpen = false }
                    )
                }
            }
        }

        @Composable
        fun YearHeader(
            selectedDate: LocalDate,
            onSelect: (LocalDate) -> Unit
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
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
                    .padding(top = 4.dp),
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
                        modifier = Modifier.padding(top = 4.dp),
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
                var checkDate by remember { mutableStateOf(currentDate) } //선택된 날짜 넘김
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
                            var firstDayOfWeek =
                                LocalDate.of(currentDate.year, currentDate.monthValue, 1)
                            if ((rowCnt == 0) and (firstDayOfWeek.dayOfWeek.value > i)) { //시작일 전에 빈칸
                                DayContainer(
                                    dayOfWeek = null,
                                    date = -1,
                                    currentDate,
                                    checkDate.dayOfMonth,
                                    onCheck = {
                                        checkDate =
                                            LocalDate.of(currentDate.year, currentDate.month, it)
                                    })
                            } else if (date > lastDay - 1) { //마지막 날 이후 빈칸
                                DayContainer(
                                    dayOfWeek = null,
                                    date = -1,
                                    currentDate,
                                    checkDate.dayOfMonth,
                                    onCheck = {
                                        checkDate =
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
                                    dayOfWeek = i + 1,
                                    date = date,
                                    currentDate,
                                    checkDate.dayOfMonth,
                                    onCheck = {
                                        checkDate =
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
                var drawColor = 0x00FFFFFF
                var drawCornerRadius = 0.dp
                var horizontalPadding = 0.dp
                var verticalPadding = 0.dp
                var borderColor = 0x00FFFFFF
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
                            drawColor = (0xFF555555).toInt()
                            drawCornerRadius = 10.dp
                            horizontalPadding = 10.dp
                            verticalPadding = 0.dp
                            textColor = Color.White
                        } else {
                            drawColor = (0xFF555555).toInt()
                            drawCornerRadius = 10.dp
                            horizontalPadding = 8.dp
                            verticalPadding = 0.dp
                            textColor = Color.White
                        }
                    } else if ((checkDate == date) and (checkDate != LocalDate.now().dayOfMonth)) {  //선택된날 테두리 표시
                        if (date < 10) {
                            drawColor = (0xFFFFFFFF).toInt()
                            drawCornerRadius = 20.dp
                            horizontalPadding = 10.dp
                            verticalPadding = 0.dp
                            borderColor = 0xFF555555.toInt()
                        } else {
                            drawColor = (0xFFFFFFFF).toInt()
                            drawCornerRadius = 20.dp
                            horizontalPadding = 8.dp
                            borderColor = 0xFF555555.toInt()
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
                                    Color(drawColor),
                                    cornerRadius = CornerRadius(drawCornerRadius.toPx()),
                                )
                            }
                            .border(2.dp, Color(borderColor), shape = CircleShape)
                            .padding(horizontalPadding, verticalPadding)
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Icon(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(id = R.drawable.ic_empty_circle),
                        contentDescription = "empty",
                        tint = Color(0xFFD9D9D9)
                    )
                }
            }
        }

        interface DateChangeListener {
            fun dateChanged(year:Int, month: Int, dayOfMonth: Int)
        }

        private fun setDateChangeListener(listener: DateChangeListener) {
            this._listener = listener
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            0 ->{
                val calendarBinding: ItemCalendarBinding =
                    ItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CalendarViewHolder(calendarBinding, fragment)
            }

            else -> {
                val diaryBinding: ItemDiaryBinding =
                    ItemDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DiaryViewHolder(diaryBinding, context)
            }
        }
    }

    override fun getItemCount() = data.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(position) {
            0 -> {
                val calendarViewHolder = holder as CalendarViewHolder
                calendarViewHolder.bind()
            }
            else -> {
                val diaryViewHolder = holder as DiaryViewHolder
                diaryViewHolder.bind(data[position-1])
                diaryViewHolder.binding.llItemDiaryTrash.setOnClickListener {
                    removeData(holder.layoutPosition)
                }
            }
        }
    }

    private fun removeData(position: Int) {
        val dataPosition = position - 1
        val beforeData = data.toMutableList()
        beforeData.removeAt(dataPosition)
        data = beforeData.toList()
        notifyItemRemoved(position)
    }

    fun updateData(newData: List<DiarySimple>) {
        data = newData
        notifyDataSetChanged()
    }
}