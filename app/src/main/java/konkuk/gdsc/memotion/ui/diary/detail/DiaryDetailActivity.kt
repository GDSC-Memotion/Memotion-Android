package konkuk.gdsc.memotion.ui.diary.detail

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import konkuk.gdsc.memotion.R
import konkuk.gdsc.memotion.data.DiaryDetail
import konkuk.gdsc.memotion.databinding.ActivityDiaryDetailBinding
import konkuk.gdsc.memotion.ui.diary.create.WritingDiaryActivity

class DiaryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryDetailBinding
    val data = DiaryDetail.sample

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
                val popup = PopupMenu(this@DiaryDetailActivity, ivDiaryDetailMenu)
                popup.inflate(R.menu.menu_popup)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.popup_item_edit -> {
                            val intent =
                                Intent(this@DiaryDetailActivity, WritingDiaryActivity::class.java)
                            startActivity(intent)
                        }

                        R.id.popup_item_delete -> {

                        }

                        else -> false
                    }
                    true
                }
                popup.show()
            }

            tvDiaryDetailDate.text = data.date

            ivDiaryDetailWeather.setImageResource(data.weather.getResource())

            tvDiaryDetailContent.text = data.content

            ivDiaryDetailEmotion.setImageResource(
                titleEmotion?.emotion?.getResource() ?: R.drawable.icon_anger
            )

            tvDiaryDetailEmotion.text = titleEmotion?.emotion.toString()

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
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.music." + data.youtubeUrl))
                startActivity(implicitIntent)
            }
        }
    }
}