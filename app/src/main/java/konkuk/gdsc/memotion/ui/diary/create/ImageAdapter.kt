package konkuk.gdsc.memotion.ui.diary.create

import android.content.Context
import android.net.Uri
import android.util.Log
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
import konkuk.gdsc.memotion.domain.entity.image.Image
import konkuk.gdsc.memotion.domain.entity.image.ImageType
import konkuk.gdsc.memotion.util.TAG
import konkuk.gdsc.memotion.util.view.setOnSingleClickListener

class ImageAdapter(
    private val context: Context,
    val data: MutableList<Image>,
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
        fun bind(item: Image) {
            Glide.with(context)
                .load(item.url)
                .centerCrop()
                .into(binding.ivItemPhoto)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
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
        if (position == 0) {
            val emptyViewHolder = holder as EmptyViewHolder
            emptyViewHolder.binding.llcAddPhoto.setOnSingleClickListener {
                pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

        } else {
            val imageViewHolder = holder as ImageViewHolder
            imageViewHolder.bind(data[position - 1])
            imageViewHolder.binding.ivItemPhotoClose.setOnClickListener {
                data.removeAt(position - 1)
//                notifyItemRemoved(position) // -> 이상한 오류가 존재 중간에 것들을 삭제한 경우 position이 제대로 바뀌지 않는 오류 발견
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount() = data.size + 1

    private fun updateData(urls: List<Uri>) {   // 새로운 조건이 필요함
        if (validateUrls(urls)) {
            addData(urls)
        } else {
            errorData()
        }
    }

    private fun addData(urls: List<Uri>) {
        for (url in urls) {
            data.add(
                Image(
                    url = url.toString(),
                    type = ImageType.NEW,
                )
            )
        }
        notifyItemInserted(data.size + 1)
    }

    private fun errorData() {
        Toast.makeText(context, "The number of photos is incorrect.", Toast.LENGTH_SHORT).show()
    }

    private fun validateUrls(urls: List<Uri>): Boolean {
        val dataSize = data.size
        val urlSize = urls.size
        if (dataSize > MAX_IMAGE) return false
        return dataSize + urlSize <= MAX_IMAGE
    }

    fun setData(newData: MutableList<String>) {
        val new = newData.map {
            Image(
                url = it,
                type = ImageType.EXISTING
            )
        }
        data.addAll(new)
        notifyDataSetChanged()
    }

    companion object {
        const val MAX_IMAGE = 3
    }
}