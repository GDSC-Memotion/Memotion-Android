package konkuk.gdsc.memotion.ui.diary.create

import android.content.Context
import android.graphics.Rect
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import konkuk.gdsc.memotion.MainActivity
import konkuk.gdsc.memotion.R
import konkuk.gdsc.memotion.domain.entity.diary.DiaryWriting
import konkuk.gdsc.memotion.databinding.ActivityWritingDiaryBinding
import konkuk.gdsc.memotion.domain.entity.emotion.Emotion
import konkuk.gdsc.memotion.ui.diary.dialog.FragmentEmotionCheck
import konkuk.gdsc.memotion.ui.diary.dialog.FragmentEmotionSelect
import konkuk.gdsc.memotion.ui.diary.dialog.FragmentLoading
import konkuk.gdsc.memotion.util.TAG
import konkuk.gdsc.memotion.util.calendarToStringWithoutTime

@AndroidEntryPoint
class WritingDiaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWritingDiaryBinding
    private var urls: MutableList<String> = mutableListOf(
        "data:image/jpeg;base64,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",
        ""
    )
    private val version: Int by lazy { intent.getIntExtra(MainActivity.INTENT_VERSION, 0) }
    private val viewmodel: WritingDiaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritingDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            tvWritingDiaryDate.text = calendarToStringWithoutTime(Calendar.getInstance())

            ivWritingDiaryBack.setOnClickListener {
                finish()
            }

            rvWritingDiaryImages.adapter = ImageAdapter(this@WritingDiaryActivity, urls)

            btnWritingDiaryPost.setOnClickListener {
                val data = DiaryWriting(
                    tvWritingDiaryDate.text.toString(),
                    listOf(),
                    etWritingDiaryContent.text.toString()
                )

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_writing_diary_cover, FragmentLoading(motionLoading))
                    .commit()
            }

            etWritingDiaryContent.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    btnWritingDiaryPost.isEnabled = !etWritingDiaryContent.text.isNullOrBlank()
                }

            })

            btnWritingDiaryPost.text =
                if (version == 1) this@WritingDiaryActivity.getString(R.string.edit) else this@WritingDiaryActivity.getString(
                    R.string.post
                )
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
        it
        supportFragmentManager.beginTransaction()
            .remove(supportFragmentManager.fragments[0])
            .commit()
        finish()
    }

}