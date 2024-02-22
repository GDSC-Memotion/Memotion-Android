package konkuk.gdsc.memotion.ui.diary.create

import android.content.Context
import android.graphics.Rect
import android.icu.util.Calendar
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import konkuk.gdsc.memotion.MainActivity
import konkuk.gdsc.memotion.R
import konkuk.gdsc.memotion.domain.entity.diary.DiaryWriting
import konkuk.gdsc.memotion.databinding.ActivityWritingDiaryBinding
import konkuk.gdsc.memotion.domain.entity.emotion.Emotion
import konkuk.gdsc.memotion.domain.entity.image.ImageType
import konkuk.gdsc.memotion.ui.diary.detail.DiaryDetailActivity
import konkuk.gdsc.memotion.ui.diary.dialog.FragmentEmotionCheck
import konkuk.gdsc.memotion.ui.diary.dialog.FragmentEmotionSelect
import konkuk.gdsc.memotion.ui.diary.dialog.FragmentLoading
import konkuk.gdsc.memotion.util.TAG
import konkuk.gdsc.memotion.util.calendarToStringTime
import konkuk.gdsc.memotion.util.calendarToStringWithoutTime
import konkuk.gdsc.memotion.util.image.ContentUriRequestBody
import okhttp3.MultipartBody

@AndroidEntryPoint
class WritingDiaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWritingDiaryBinding
    private val viewModel: WritingDiaryViewModel by viewModels()
    private val imageAdapter = ImageAdapter(this@WritingDiaryActivity, mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritingDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val versionObserver = Observer<DiaryVersion> {
            when (it) {
                DiaryVersion.WRITING -> setWritingDiary()
                DiaryVersion.EDITING -> viewModel.setEditingDiaryData(
                    intent.getLongExtra(
                        DiaryDetailActivity.INTENT_DIARY_ID,
                        1
                    )
                )
            }
        }

        val diaryDataObserver = Observer<DiaryWriting> {
            if (viewModel.version.value == DiaryVersion.EDITING) {
                binding.apply {
                    tvWritingDiaryDate.text = it.date

                    imageAdapter.setData(it.imageUrls.toMutableList())

                    etWritingDiaryContent.setText(it.content)

                    btnWritingDiaryPost.text = this@WritingDiaryActivity.getString(
                        R.string.edit
                    )
                }
            }
        }

        viewModel.version.observe(this, versionObserver)
        viewModel.diaryData.observe(this, diaryDataObserver)

        viewModel.setVersion(intent.getIntExtra(MainActivity.INTENT_VERSION, 0))

        binding.apply {
            ivWritingDiaryBack.setOnClickListener {
                finish()
            }

            rvWritingDiaryImages.adapter = imageAdapter

            btnWritingDiaryPost.setOnClickListener {
                val existingImages = imageAdapter.data
                    .filter { it.type == ImageType.EXISTING }
                    .map { it.url }
                val newImages = imageAdapter.data
                    .filter { it.type == ImageType.NEW }
                    .map { it.url }

                val data = DiaryWriting(
                    "${tvWritingDiaryDate.text} ${calendarToStringTime(Calendar.getInstance())}",
                    existingImages,
                    etWritingDiaryContent.text.toString()
                )

                val images: List<MultipartBody.Part> =
                    newImages.map {
                        val imagePath = Uri.parse(it)
                        val requestBody =
                            ContentUriRequestBody(this@WritingDiaryActivity, imagePath)
                        requestBody.toFormData("images")
                    }

                viewModel.setDiaryData(data)
                viewModel.updateImages(images)

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_writing_diary_cover, FragmentLoading(motionLoading))
                    .commit()
            }

            etWritingDiaryContent.doAfterTextChanged {
                btnWritingDiaryPost.isEnabled = !etWritingDiaryContent.text.isNullOrBlank()
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action === MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private val motionLoading: () -> Unit = {
        Log.d(TAG, "WritingDiaryActivity - motionLoading() called")
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fl_writing_diary_cover, FragmentEmotionCheck(
                    btnNo = motionCheckNo,
                    btnYes = motionCheckYes,
                )
            )
            .commit()
    }

    private val motionCheckNo: () -> Unit = {
        Log.d(TAG, "WritingDiaryActivity - motionCheckNo() called")
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_writing_diary_cover, FragmentEmotionSelect { motionSelect(it) })
            .commit()
    }

    private val motionCheckYes: () -> Unit = {
        Log.d(TAG, "WritingDiaryActivity - motionCheckYes() called")
        supportFragmentManager.beginTransaction()
            .remove(supportFragmentManager.fragments[0])
            .commit()
        finish()
    }

    private val motionSelect: (emotion: Emotion) -> Unit = {
        Log.d(TAG, "WritingDiaryActivity - motionSelect() called")

        supportFragmentManager.beginTransaction()
            .remove(supportFragmentManager.fragments[0])
            .commit()
        finish()
    }

    private fun setWritingDiary() {
        binding.apply {
            tvWritingDiaryDate.text = calendarToStringWithoutTime(Calendar.getInstance())

            btnWritingDiaryPost.text = this@WritingDiaryActivity.getString(R.string.post)
        }

    }

}