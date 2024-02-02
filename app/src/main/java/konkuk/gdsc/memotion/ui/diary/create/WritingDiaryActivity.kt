package konkuk.gdsc.memotion.ui.diary.create

import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import com.bumptech.glide.Glide
import konkuk.gdsc.memotion.R
import konkuk.gdsc.memotion.data.Weather
import konkuk.gdsc.memotion.data.DiaryWriting
import konkuk.gdsc.memotion.databinding.ActivityWritingDiaryBinding
import konkuk.gdsc.memotion.databinding.FragmentLoadingBinding
import konkuk.gdsc.memotion.ui.diary.dialog.FragmentEmotionCheck
import konkuk.gdsc.memotion.ui.diary.dialog.FragmentEmotionSelect
import konkuk.gdsc.memotion.ui.diary.dialog.FragmentLoading
import konkuk.gdsc.memotion.util.calendarToString

class WritingDiaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWritingDiaryBinding
    private val pickMultipleMedia =
        registerForActivityResult(PickMultipleVisualMedia(3)) { uris ->
            if (uris.isNotEmpty()) {
                Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
                Glide.with(this)
                    .load(uris[0])
                    .centerCrop()
                    .into(binding.ivWritingDiaryImage)
                binding.ivWritingDiaryImage.visibility = View.VISIBLE
                binding.vBorderLine.visibility = View.VISIBLE
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritingDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            tvWritingDiaryDate.text = calendarToString(Calendar.getInstance())

            ivWritingDiaryBack.setOnClickListener {
                finish()
            }

            ivAddingImage.setOnClickListener {
                pickMultipleMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
            }

            tvWritingDiaryPost.setOnClickListener {
                val data = DiaryWriting(
                    tvWritingDiaryDate.text.toString(),
                    Weather.SUN,
                    null,
                    etWritingDiaryContent.text.toString()
                )
//                finish()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_writing_diary_cover, FragmentLoading())
                    .commit()
                /*Handler(Looper.getMainLooper()).postDelayed({
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_writing_diary_cover, FragmentEmotionCheck())
                        .commit()
                }, 5000)
                Handler(Looper.getMainLooper()).postDelayed({
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_writing_diary_cover, FragmentEmotionSelect())
                        .commit()
                }, 10000)*/
            }
        }

    }
}