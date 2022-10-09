package xaldarof.dictionary.english.service

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xaldarof.dictionary.english.domain.repositories.WordsRepository
import javax.inject.Inject


abstract class DaggerBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {}
}

@AndroidEntryPoint
class NotificationActionBroadcastReceiver : DaggerBroadcastReceiver() {

    @Inject
    lateinit var repository: WordsRepository

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.getStringExtra("seen") == "seen") {
            CoroutineScope(Dispatchers.IO).launch {
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(123)
                repository.clearUnSeenWords()
            }
        }

        if (intent.getStringExtra("know") == "know") {
            CoroutineScope(Dispatchers.IO).launch {
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(123)
                repository.updateKnow(intent.getStringExtra("body")!!)
                repository.clearUnSeenWords()

            }
        }

    }
}