package konkuk.gdsc.memotion.ui.diary

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import konkuk.gdsc.memotion.data.DiarySimple
import konkuk.gdsc.memotion.databinding.ItemDiaryBinding
import konkuk.gdsc.memotion.ui.diary.detail.DiaryDetailActivity

class DiaryAdapter(
    private val context: Context,
    private var data: List<DiarySimple>,
) : RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemDiaryBinding,
        private val context: Context,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DiarySimple) {
            if (this.layoutPosition == 0) {
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDiaryBinding =
            ItemDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, context)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])

        holder.binding.llItemDiaryTrash.setOnClickListener {
            removeData(holder.layoutPosition)
        }
    }

    private fun removeData(position: Int) {
        val beforeData = data.toMutableList()
        beforeData.removeAt(position)
        data = beforeData.toList()
        notifyItemRemoved(position)
    }
}