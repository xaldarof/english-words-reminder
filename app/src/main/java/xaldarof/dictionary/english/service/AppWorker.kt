package uz.unical.programm.workmanager.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xaldarof.dictionary.english.MainActivity
import xaldarof.dictionary.english.R
import xaldarof.dictionary.english.data.AppDatabase

class MainWorker(private val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {


    @RequiresApi(Build.VERSION_CODES.M)
    override fun doWork(): Result {
        return try {
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(context)

                if (db.getWordsDao().getCount() > 0) {
                    showNotification(db.getWordsDao().getRandomWord().body)
                }
            }

            Result.success()
        } catch (e: Throwable) {
            Result.retry()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("WrongConstant")
    private fun showNotification(body: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "all_notifications"
            val mChannel = NotificationChannel(
                channelId,
                "General Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            mChannel.description = "This is default channel used for all other notifications"

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        val channelId = "all_notifications"
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_baseline_emoji_emotions_24)
            .setContentTitle(body.split(" ")[0])
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        builder.setContentIntent(pendingIntent).setAutoCancel(true)
        val mNotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        with(mNotificationManager) {
            notify(123, builder.build())
        }
    }
}