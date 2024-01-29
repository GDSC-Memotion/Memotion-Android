package konkuk.gdsc.memotion.ui.diary

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import konkuk.gdsc.memotion.MemotionApp
import konkuk.gdsc.memotion.R
import konkuk.gdsc.memotion.databinding.ActivityWritingDiaryBinding

class WritingDiaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWritingDiaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritingDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {

        }

    }
}