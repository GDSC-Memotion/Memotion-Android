package konkuk.gdsc.memotion.ui.diary.detail

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.createBalloon
import konkuk.gdsc.memotion.R
import konkuk.gdsc.memotion.data.DiaryDetail
import konkuk.gdsc.memotion.databinding.ActivityDiaryDetailBinding
import konkuk.gdsc.memotion.ui.diary.create.WritingDiaryActivity

class DiaryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryDetailBinding
    private val data = DiaryDetail.sample
    private val balloon = createBalloon(this@DiaryDetailActivity) {
        setLayout(R.layout.popup_diary_detail)
        setArrowSize(0)
        setArrowOrientation(ArrowOrientation.TOP)
        setCornerRadius(4f)
        setMarginRight(20)
        setMarginTop(8)
        setBackgroundColor(
            ContextCompat.getColor(
                this@DiaryDetailActivity,
                R.color.white
            )
        )
        setBalloonAnimation(BalloonAnimation.ELASTIC)
        setLifecycleOwner(this@DiaryDetailActivity)
        build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val titleEmotion = data.emotions.find { it.isTitle }

        binding.apply {
            ivDiaryDetailBack.setOnClickListener {
                finish()
            }

            ivDiaryDetailMenu.setOnClickListener {
                balloon.showAlignBottom(binding.ivDiaryDetailMenu)
            }

            tvDiaryDetailDate.text = data.date

            ivDiaryDetailWeather.setImageResource(data.weather.getResource())

            tvDiaryDetailContent.text = data.content

            ivDiaryDetailEmotion.setImageResource(
                titleEmotion?.emotion?.getResource() ?: R.drawable.icon_anger
            )

            tvDiaryDetailEmotion.text = titleEmotion?.emotion.toString()
            lpiDiaryDetailPercentage.setProgressCompat(
                (data.emotions[0].percentage * 100).toInt(),
                true
            )
            tvDiaryDetailPercentageNumber.text = titleEmotion?.percentage.toString()

            rvDiaryDetailEmotionList.adapter = EmotionAdapter(data.emotions)
            rvDiaryDetailEmotionList.layoutManager = LinearLayoutManager(this@DiaryDetailActivity)

            cvDiaryDetailYoutube.setOnClickListener {
                val implicitIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www." + data.youtubeUrl))
                startActivity(implicitIntent)
            }
            cvDiaryDetailYoutubeMusic.setOnClickListener {
                val implicitIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://music." + data.youtubeUrl))
                startActivity(implicitIntent)
            }
        }

        setPopupButton()
    }

    private fun setPopupButton() {
        val editButton: LinearLayoutCompat =
            balloon.getContentView().findViewById(R.id.ll_popup_edit)
        val trashButton: LinearLayoutCompat =
            balloon.getContentView().findViewById(R.id.ll_popup_trash)


        editButton.setOnClickListener {
            val intent =
                Intent(this@DiaryDetailActivity, WritingDiaryActivity::class.java)
            startActivity(intent)
            balloon.dismiss()
        }

        trashButton.setOnClickListener {
            balloon.dismiss()
            finish()
        }
    }
}