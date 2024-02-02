package konkuk.gdsc.memotion.braodcast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import konkuk.gdsc.memotion.R
import konkuk.gdsc.memotion.ui.diary.create.WritingDiaryActivity

class NotificationHelper(private val context: Context) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    private fun createChannel() {
        val name = "일기 시간 설정"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val descriptionText = "미리 설정한 시간에 맞춰 일기 알람을 보내준다."

        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
            lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC     // 잠금화면에서 어떻게 나오는지 설정
        }

        val notificationManager = getManager()
        notificationManager.createNotificationChannel(channel)              // notificaiton channel 생성
    }

    fun getManager() = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun getNotificationChannel(): NotificationCompat.Builder {
        val resultIntent = Intent(context, WritingDiaryActivity::class.java)

        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("textTitle")
            .setContentText("textContent")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)

        return builder
    }

    companion object {
        const val NOTIFICATION_ID = 3
        const val CHANNEL_ID = "시간 알림 설정"
    }
}