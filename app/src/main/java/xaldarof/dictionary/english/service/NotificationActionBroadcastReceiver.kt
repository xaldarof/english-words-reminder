package xaldarof.dictionary.english.service

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xaldarof.dictionary.english.data.database.AppDatabase
import xaldarof.dictionary.english.domain.repositories.WordsRepository
import javax.inject.Inject


@AndroidEntryPoint
class NotificationActionBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: WordsRepository

    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1?.getStringExtra("seen") == "seen") {
            if (p0 != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    repository.clearUnSeenWords()
                    val notificationManager =
                        p0.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.cancel(123)
                }
            }
        }

        if (p1?.getStringExtra("know") == "know") {
            if (p0 != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    repository.updateKnow(p1.getStringExtra("body")!!)
                    repository.clearUnSeenWords()
                    val notificationManager =
                        p0.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.cancel(123)
                }
            }
        }

    }
}