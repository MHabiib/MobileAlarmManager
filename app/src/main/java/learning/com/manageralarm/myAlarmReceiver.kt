package learning.com.manageralarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat

class MyAlarmReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context, intent: Intent) {
        val mBuilder = NotificationCompat.Builder(context!!)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Alarm Manager")
            .setContentText(intent.getStringExtra(EXTRA_PESAN))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val mnotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val id = 30103;
        mnotificationManager.notify(id, mBuilder.build())
    }
}