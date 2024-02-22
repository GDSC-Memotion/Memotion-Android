package konkuk.gdsc.memotion.ui.diary.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import konkuk.gdsc.memotion.databinding.ItemImageBinding
import konkuk.gdsc.memotion.util.dpToPx

class ImageAdapter(
    private val context: Context,
    private val data: List<String>,
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: ItemImageBinding,
        private val context: Context,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            Glide.with(context)
                .load(item)
                .centerCrop()
                .into(binding.ivDiaryImage)
            binding.ivDiaryImage.layoutParams.height = context.resources.displayMetrics.widthPixels - dpToPx(context, 40f).toInt()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val imageBinding: ItemImageBinding =
            ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(imageBinding, context)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

}

