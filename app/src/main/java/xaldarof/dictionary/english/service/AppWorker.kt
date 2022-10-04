package xaldarof.dictionary.english.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.Notification.FLAG_AUTO_CANCEL
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
import xaldarof.dictionary.english.domain.UnSeenWordEntity

class AppWorker(private val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {


    @RequiresApi(Build.VERSION_CODES.M)
    override fun doWork(): Result {
        return try {
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(context)

                if (db.getWordsDao().getCount() > 0) {
                    if (db.getWordsDao().getUnSeenWordsCount() > 0) {
                        showNotification(db.getWordsDao().getRandomUnSeedWord().body)
                    } else {
                        db.getWordsDao().insertToUnRead(UnSeenWordEntity(db.getWordsDao().getRandomWord().body))
                        showNotification(db.getWordsDao().getRandomUnSeedWord().body)
                    }
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
        val broadCastIntent = Intent(context, ActivityReceiver::class.java)
        broadCastIntent.putExtra("seen", "seen")

        val deletePendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            broadCastIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE or FLAG_AUTO_CANCEL
        )

        val broadCastIntentKnow = Intent(context, ActivityReceiver::class.java)
        broadCastIntentKnow.putExtra("know", "know")
        broadCastIntentKnow.putExtra("body", body)

        val deletePendingIntentKnow = PendingIntent.getBroadcast(
            context,
            1,
            broadCastIntentKnow,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE or FLAG_AUTO_CANCEL
        )

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
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE or FLAG_AUTO_CANCEL
        )

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_baseline_emoji_emotions_24)
            .setContentTitle(body.split(" ")[0])
            .setContentText(body)
            .addAction(android.R.drawable.sym_def_app_icon, "Я увидел", deletePendingIntent)
            .addAction(android.R.drawable.sym_def_app_icon, "Я это знаю", deletePendingIntentKnow)
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