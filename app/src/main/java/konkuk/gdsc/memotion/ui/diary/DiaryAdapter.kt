package konkuk.gdsc.memotion.ui.diary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import konkuk.gdsc.memotion.domain.entity.diary.DiarySimple
import konkuk.gdsc.memotion.databinding.ItemCalendarBinding
import konkuk.gdsc.memotion.databinding.ItemDiaryBinding
import konkuk.gdsc.memotion.ui.diary.detail.DiaryDetailActivity

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

            setDateChangeListener(fragment as DiaryFragment)

            binding.calendarDiary.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
                listener.dateChanged(year, month, dayOfMonth)
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