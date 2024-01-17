package konkuk.gdsc.memotion.braodcast

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import konkuk.gdsc.memotion.util.LIFECYCLE

class AlertReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        Log.d(LIFECYCLE, "AlertReceiver - onReceive() called")

        val notificationHelper = NotificationHelper(context)
        val nb: NotificationCompat.Builder = notificationHelper.getNotificationChannel()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        notificationHelper.getManager().notify(NotificationHelper.NOTIFICATION_ID, nb.build())
    }

}