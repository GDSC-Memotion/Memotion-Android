package konkuk.gdsc.memotion.ui.diary.detail

import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import dagger.hilt.android.AndroidEntryPoint
import konkuk.gdsc.memotion.MainActivity
import konkuk.gdsc.memotion.R
import konkuk.gdsc.memotion.domain.entity.diary.DiaryDetail
import konkuk.gdsc.memotion.databinding.ActivityDiaryDetailBinding
import konkuk.gdsc.memotion.ui.diary.DiaryAdapter
import konkuk.gdsc.memotion.ui.diary.create.WritingDiaryActivity
import konkuk.gdsc.memotion.util.TAG
import konkuk.gdsc.memotion.util.calendarToString
import konkuk.gdsc.memotion.util.dpToPx

@AndroidEntryPoint
class DiaryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryDetailBinding
    private lateinit var balloon: Balloon
    private var emotionState = false
    private val viewModel: DiaryDetailViewModel by viewModels()
    private lateinit var youtubeIntent: Intent
    private lateinit var youtubeMusicIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val diaryObserver = Observer<DiaryDetail> {
            val titleEmotion =
                requireNotNull(it.emotions.find { it.isTitle }) { "emotion data is null" }
            binding.apply {
                tvDiaryDetailDate.text = it.date
                if (it.imageUrls.isNotEmpty()) {
                    Glide.with(this@DiaryDetailActivity)
                        .load(it.imageUrls[0])
                        .centerCrop()
                        .into(ivDiaryDetailImage)
                    ivDiaryDetailImage.visibility = View.VISIBLE
                    ivDiaryDetailImage.layoutParams.height = resources.displayMetrics.widthPixels
                }
                tvDiaryDetailContent.text = it.content

                tvDiaryDetailEmotion.text = titleEmotion.emotion.toString()
                tvHiddenEmotion.text = titleEmotion.emotion.toString()

                ivDiaryDetailEmotion.setImageResource(
                    titleEmotion.emotion.getResource()
                )
                pvDiaryDetailPercentage.progress = (titleEmotion.percentage * 100)
                tvDiaryDetailPercentageNumber.text =
                    "${String.format("%.2f", titleEmotion.percentage)} %"

                rvDiaryDetailEmotionList.adapter = EmotionAdapter(it.emotions)
            }
            youtubeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.youtubeUrl))
            youtubeMusicIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.youtubeUrl))
        }
        viewModel.diary.observe(this, diaryObserver)

        balloon = setBalloon()

        setDetailVisibility()

        binding.apply {
            ivDiaryDetailBack.setOnClickListener {
                finish()
            }

            ivDiaryDetailMenu.setOnClickListener {
                balloon.showAlignBottom(binding.ivDiaryDetailMenu)
            }

            cvDiaryDetailYoutube.setOnClickListener {
                startActivity(youtubeIntent)
            }
            cvDiaryDetailYoutubeMusic.setOnClickListener {
                startActivity(youtubeMusicIntent)
            }

            tvDiaryDetailEmotionViewmore.setOnClickListener {
                emotionState = !emotionState
                setDetailVisibility()
            }

        }

        setPopupButton()
        viewModel.getDiaryData(intent.getLongExtra(DiaryAdapter.DiaryViewHolder.INTENT_DIARY_ID, 1))
    }

    private fun setPopupButton() {
        val editButton: LinearLayoutCompat =
            balloon.getContentView().findViewById(R.id.ll_popup_edit)
        val trashButton: LinearLayoutCompat =
            balloon.getContentView().findViewById(R.id.ll_popup_trash)


        editButton.setOnClickListener {
            val intent =
                Intent(this@DiaryDetailActivity, WritingDiaryActivity::class.java)
            intent.putExtra(INTENT_DIARY_ID, viewModel.diary.value?.diaryId ?: 1)
            intent.putExtra(MainActivity.INTENT_VERSION, 1)
            startActivity(intent)
            balloon.dismiss()
        }

        trashButton.setOnClickListener {
            balloon.dismiss()
            finish()
        }
    }

    private fun setDetailVisibility() {
        if (emotionState) {
            binding.tvDiaryDetailEmotionViewmore.text =
                this@DiaryDetailActivity.getString(R.string.view_less)
            binding.llcDiaryDetailEmotionDetail.visibility = View.VISIBLE
            binding.tvHiddenEmotion.visibility = View.VISIBLE
            binding.llDiaryDetailEmotion.visibility = View.GONE
        } else {
            binding.tvDiaryDetailEmotionViewmore.text =
                this@DiaryDetailActivity.getString(R.string.view_more)
            binding.llcDiaryDetailEmotionDetail.visibility = View.GONE
            binding.tvHiddenEmotion.visibility = View.GONE
            binding.llDiaryDetailEmotion.visibility = View.VISIBLE
        }
    }

    private fun setBalloon(): Balloon = Balloon.Builder(this)
        .setLayout(R.layout.popup_diary_detail)
        .setArrowSize(0)
        .setArrowOrientation(ArrowOrientation.TOP)
        .setCornerRadius(4f)
        .setMarginRight(20)
        .setMarginTop(8)
        .setBackgroundColor(
            ContextCompat.getColor(
                this@DiaryDetailActivity,
                R.color.white
            )
        )
        .setBalloonAnimation(BalloonAnimation.ELASTIC)
        .setLifecycleOwner(this@DiaryDetailActivity)
        .build()

    companion object {
        const val INTENT_DIARY_ID = "diaryId"
    }
}