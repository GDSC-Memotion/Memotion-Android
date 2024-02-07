package konkuk.gdsc.memotion.ui.diary.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import konkuk.gdsc.memotion.domain.entity.emotion.EmotionResult
import konkuk.gdsc.memotion.databinding.ItemEmotionPercentageBinding

class EmotionAdapter(
    private val data: List<EmotionResult>
) : RecyclerView.Adapter<EmotionAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemEmotionPercentageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EmotionResult) {
            binding.apply {
                tvItemEmotionTitle.text = item.emotion.toString()
                tvItemEmotionPercentageNumber.text = "${String.format("%.2f", item.percentage)} %"

                lpiItemEmotionPercentage.setIndicatorColor()
                lpiItemEmotionPercentage.setProgressCompat((item.percentage * 100).toInt(), true)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemEmotionPercentageBinding =
            ItemEmotionPercentageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size
}