package konkuk.gdsc.memotion.ui.diary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import konkuk.gdsc.memotion.domain.entity.diary.DiarySimple
import konkuk.gdsc.memotion.databinding.ItemCalendarBinding
import konkuk.gdsc.memotion.databinding.ItemDiaryBinding
import konkuk.gdsc.memotion.ui.diary.detail.DiaryDetailActivity
import konkuk.gdsc.memotion.util.dpToPx

class DiaryAdapter(
    private val context: Context,
    private var data: List<DiarySimple>,
    private val fragment: Fragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _listener: DeleteDiaryListener? = null
    private val listener: DeleteDiaryListener
        get() = requireNotNull(_listener) { "DiaryAdapter's DeleteDiaryListener is null" }

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

        fun bind() {

            setDateChangeListener(fragment as DiaryFragment)

            binding.calendarDiary.setOnDateChangeListener { _, year, month, dayOfMonth ->
                listener.dateChanged(year, month, dayOfMonth)
            }
        }

        interface DateChangeListener {
            fun dateChanged(year: Int, month: Int, dayOfMonth: Int)
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
                calendarViewHolder.bind()
            }

            else -> {
                setDeleteDiaryListener(fragment as DiaryFragment)
                val diaryViewHolder = holder as DiaryViewHolder
                if (data.isNotEmpty()) {
                    diaryViewHolder.bind(data[position - 1])
                    diaryViewHolder.binding.llItemDiaryTrash.setOnClickListener {
                        removeData(holder.layoutPosition)
                        listener.deleteDiary(data[position - 1].diaryId)
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
        data = newData
        notifyDataSetChanged()
    }

    interface DeleteDiaryListener {
        fun deleteDiary(diaryId: Long)
    }

    private fun setDeleteDiaryListener(listener: DeleteDiaryListener) {
        this._listener = listener
    }

}