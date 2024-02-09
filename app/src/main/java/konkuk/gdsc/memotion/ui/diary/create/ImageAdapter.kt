package konkuk.gdsc.memotion.ui.diary.create

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import konkuk.gdsc.memotion.databinding.ItemAddPhotoBinding
import konkuk.gdsc.memotion.databinding.ItemPhotoBinding
import konkuk.gdsc.memotion.util.view.setOnSingleClickListener

class ImageAdapter(
    private val context: Context,
    private val data: MutableList<String>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val pickMultipleMedia = (context as AppCompatActivity).registerForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(3)
    ) { uris ->
        if (uris.isNotEmpty()) {
            updateData(uris)
        } else {
        }
    }

    class EmptyViewHolder(
        val binding: ItemAddPhotoBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    class ImageViewHolder(
        val binding: ItemPhotoBinding,
        private val context: Context,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            Glide.with(context)
                .load(item)
                .centerCrop()
                .into(binding.ivItemPhoto)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].isNullOrBlank()) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val addingBinding: ItemAddPhotoBinding =
                    ItemAddPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EmptyViewHolder(addingBinding)
            }

            else -> {
                val photoBinding: ItemPhotoBinding =
                    ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ImageViewHolder(photoBinding, context)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (data[position].isNullOrBlank()) {
            val emptyViewHolder = holder as EmptyViewHolder
            emptyViewHolder.binding.llcAddPhoto.setOnSingleClickListener {
                pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

        } else {
            val imageViewHolder = holder as ImageViewHolder
            imageViewHolder.bind(data[position])
            imageViewHolder.binding.ivItemPhotoClose.setOnClickListener {
                data.removeAt(position)
                notifyItemRemoved(position)
                if (data.last().isNotBlank()) {
                    data.add("")
                    notifyItemInserted(data.size - 1)
                }
            }
        }
    }

    override fun getItemCount() = data.size

    private fun updateData(urls: List<Uri>) {   // 새로운 조건이 필요함
        if (validateUrls(urls)) {
            if (data.last().isNullOrBlank()) {
                data.removeLast()
            }
            addData(urls)
            if(!(data.size > MAX_IMAGE)) data.add(" ")
            notifyDataSetChanged()
        } else {
            errorData()
        }
    }

    private fun addData(urls: List<Uri>) {
        for (url in urls) {
            data.add(url.toString())
        }
    }

    private fun errorData() {
        Toast.makeText(context, "사진의 갯수가 잘 못되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun validateUrls(urls: List<Uri>): Boolean {
        val dataSize = if (data.last().isNullOrBlank()) data.size - 1 else data.size
        val urlSize = urls.size
        if (dataSize > MAX_IMAGE) return false
        if (dataSize + urlSize > MAX_IMAGE) return false
        return true
    }

    companion object {
        const val MAX_IMAGE = 3
    }
}