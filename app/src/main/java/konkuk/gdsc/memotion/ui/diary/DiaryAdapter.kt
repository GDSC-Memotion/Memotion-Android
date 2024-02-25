package konkuk.gdsc.memotion.ui.diary

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
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
import com.bumptech.glide.Glide
import konkuk.gdsc.memotion.domain.entity.diary.DiarySimple
import konkuk.gdsc.memotion.R
import konkuk.gdsc.memotion.databinding.ItemCalendarBinding
import konkuk.gdsc.memotion.databinding.ItemDiaryBinding
import konkuk.gdsc.memotion.domain.entity.emotion.Emotion
import konkuk.gdsc.memotion.ui.diary.detail.DiaryDetailActivity
import konkuk.gdsc.memotion.util.dpToPx
import java.time.LocalDate
import java.util.Locale

class DiaryAdapter(
    private val context: Context,
    private var data: List<DiarySimple>,
    private val fragment: Fragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _listener: DeleteDiaryListener? = null
    private val listener: DeleteDiaryListener
        get() = requireNotNull(_listener) { "DiaryAdapter's DeleteDiaryListener is null" }

    private var emotionList1 = listOf("","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","")

    class DiaryViewHolder(
        val binding: ItemDiaryBinding,
        private val context: Context,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DiarySimple) {
            binding.llcDiaryNull.visibility = View.GONE
            binding.clDiaryItem.visibility = View.VISIBLE

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
                intent.putExtra(INTENT_DIARY_ID, item.diaryId)
                context.startActivity(intent)
            }

            binding.ivItemDiaryEmotion.setImageResource(item.emotion.getResource())
            binding.ivItemDiaryImage.layoutParams.height =
                (context.resources.displayMetrics.widthPixels - dpToPx(context, 51f)).toInt()

            if (item.imageUrl.isNotEmpty()) {
                binding.ivItemDiaryImage.visibility = View.VISIBLE

                Glide.with(context)
                    .load(item.imageUrl[0])
                    .centerCrop()
                    .into(binding.ivItemDiaryImage)
            } else {
                binding.ivItemDiaryImage.visibility = View.GONE
            }
        }

        fun bindEmpty() {
            binding.apply {
                clDiaryItem.visibility = View.GONE
                llcDiaryNull.visibility = View.VISIBLE
            }
        }

        companion object {
            const val INTENT_DIARY_ID = "diaryId"
        }
    }

    class CalendarViewHolder(
        private val binding: ItemCalendarBinding,
        private val fragment: Fragment,
    ) : RecyclerView.ViewHolder(binding.root) {

        private var _listener: DateChangeListener? = null
        private val listener: DateChangeListener
            get() = requireNotNull(_listener) { "CalendarViewHolder's DateChangeListener is null" }
        private var currentDate = LocalDate.now()


        fun bind(
            emotionList: List<String>
        ) {

            Log.d("로그", "bind: $emotionList")
            binding.customCalendar.setContent {
                CalendarCustom(
                    currentDate,
                    changedDate = {
                        currentDate = it
                    },
                    emotionList,
                )
            }
            setDateChangeListener(fragment as DiaryFragment)
        }

        @Composable
        fun CalendarCustom(
            currentDate: LocalDate,
            changedDate: (LocalDate) -> Unit,
            emotionList: List<String>
        ) {
            Log.d("로그", "CalendarCustom: $emotionList")
//            var currentDate by remember { mutableStateOf(LocalDate.now()) } //현재 보여줄 달력 날짜의 기준
            var isMonthDialogOpen by remember { mutableStateOf(false) }
            var changedDate by remember { mutableStateOf(currentDate) }
            Column(
                modifier = Modifier
                    .padding(16.dp, 0.dp),
            ) {
                YearHeader(changedDate, onSelect = {
                    changedDate = it
                    changedDate(it)
                    listener.dateChanged(
                        changedDate.year,
                        changedDate.monthValue,
                        changedDate.dayOfMonth
                    )
                    listener.monthChanged(
                        changedDate.year,
                        changedDate.monthValue,
                        changedDate.dayOfMonth
                    )
                })
                MonthHeader(changedDate, onSelect = {
                    isMonthDialogOpen = true
                })
                DayHeader()
                CalendarPage(changedDate, emotionList, onSelect = {
                    changedDate = it
                })

                if (isMonthDialogOpen) {
                    MonthDialog(
                        currentDate = currentDate,
                        changedDate = changedDate,
                        onMonthSelected = {
                            listener.dateChanged(changedDate.year, it, changedDate.dayOfMonth)
                            changedDate = LocalDate.of(changedDate.year, it, changedDate.dayOfMonth)
                            isMonthDialogOpen = false
                        },
                        onDismissRequest = { isMonthDialogOpen = false },
                    )
                }
            }
        }

        //Custom Calendar
        @Composable
        fun YearHeader(
            currentDate: LocalDate,
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
                            onSelect(currentDate.minusYears(1))
                        },
                    painter = painterResource(id = R.drawable.chevron_left),
                    contentDescription = "previous",
                )
                Text(
                    text = "${currentDate.year}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    modifier = Modifier
                        .padding(4.dp, 0.dp)
                        .size(20.dp)
                        .clickable {
                            onSelect(currentDate.plusYears(1))
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
            emotionList: List<String>,
            onSelect: (LocalDate) -> Unit,
        ) {
            Log.d("로그", "CalendarPage: $emotionList")
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
                                            LocalDate.of(
                                                currentDate.year,
                                                currentDate.month,
                                                it
                                            )
                                        onSelect(checkDate)
                                        listener.dateChanged(
                                            checkDate.year,
                                            checkDate.monthValue,
                                            checkDate.dayOfMonth
                                        )
                                    },
                                    emotion = null,
                                )
                            } else if (date > lastDay - 1) { //마지막 날 이후 빈칸
                                DayContainer(
                                    dayOfWeek = null,
                                    date = -1,
                                    currentDate,
                                    checkDate.dayOfMonth,
                                    onCheck = {
                                        checkDate =
                                            LocalDate.of(
                                                currentDate.year,
                                                currentDate.month,
                                                it
                                            )
                                        onSelect(checkDate)
                                        listener.dateChanged(
                                            checkDate.year,
                                            checkDate.monthValue,
                                            checkDate.dayOfMonth
                                        )
                                    },
                                    emotion = null,
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
                                            LocalDate.of(
                                                currentDate.year,
                                                currentDate.month,
                                                it
                                            )
                                        onSelect(checkDate)
                                        listener.dateChanged(
                                            checkDate.year,
                                            checkDate.monthValue,
                                            it
                                        )
                                    },
                                    emotion = emotionList[date - 1],
                                )
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
            emotion: String?,
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
                var painter: Int

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


                    if (emotion != null) {
                        if (emotion == "") {
                            painter = R.drawable.ic_empty_circle
                        } else {
                            painter = getEmotionOfDay(emotion).getResource()
                        }
                    } else {
                        Log.d("diaryAdapter", "DayContainer: emotion==null")
                        painter = R.drawable.ic_empty_circle
                    }
                    Image(
                        modifier = Modifier
                            .size(36.dp),
                        painter = painterResource(id = painter),
                        contentDescription = "empty",
                    )
                }
            }
        }

        private fun getEmotionOfDay(emotionString: String): Emotion {
            val emotion = when (emotionString) {
                Emotion.ANGER.toString().lowercase(Locale.getDefault()) -> Emotion.ANGER
                Emotion.DISGUST.toString().lowercase(Locale.getDefault()) -> Emotion.DISGUST
                Emotion.FEAR.toString().lowercase(Locale.getDefault()) -> Emotion.FEAR
                Emotion.JOY.toString().lowercase(Locale.getDefault()) -> Emotion.JOY
                Emotion.NEUTRAL.toString().lowercase(Locale.getDefault()) -> Emotion.NEUTRAL
                Emotion.SADNESS.toString().lowercase(Locale.getDefault()) -> Emotion.SADNESS
                Emotion.SURPRISE.toString().lowercase(Locale.getDefault()) -> Emotion.SURPRISE
                else -> Emotion.ANGER
            }
            return emotion
        }

        interface DateChangeListener {
            fun dateChanged(year: Int, month: Int, dayOfMonth: Int)
            fun monthChanged(year: Int, month: Int, dayOfMonth: Int)
        }

        private fun setDateChangeListener(listener: DateChangeListener) {
            this._listener = listener
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
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

    override fun getItemCount() = if (data.isNotEmpty()) data.size + 1 else 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (position) {
            0 -> {
                val calendarViewHolder = holder as CalendarViewHolder
                Log.d("로그", "1 onBindViewHolder: $emotionList1")
                calendarViewHolder.bind(emotionList1)
            }

            else -> {
                setDeleteDiaryListener(fragment as DiaryFragment)
                val diaryViewHolder = holder as DiaryViewHolder
                if (data.isNotEmpty()) {
                    diaryViewHolder.bind(data[position - 1])
                    diaryViewHolder.binding.llItemDiaryTrash.setOnClickListener {
                        listener.deleteDiary(data[position - 1].diaryId)
                        removeData(holder.layoutPosition)
//                        notifyDataSetChanged()
                    }
                } else {
                    diaryViewHolder.bindEmpty()
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
        val dataSize = if (data.size > newData.size) data.size else newData.size
        data = newData
        notifyItemRangeChanged(1, dataSize)
    }

    fun updateMonth(emotions: List<String>) {
        Log.d("로그", "updateMonth 전: $emotionList1 , $emotions")
        emotionList1 = emotions
        Log.d("로그", "updateMonth 후: $emotionList1 , $emotions")
    }

    interface DeleteDiaryListener {
        fun deleteDiary(diaryId: Long)
    }

    private fun setDeleteDiaryListener(listener: DeleteDiaryListener) {
        this._listener = listener
    }
}