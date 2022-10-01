package uz.unical.programm.workmanager.worker

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xaldarof.dictionary.english.R
import xaldarof.dictionary.english.data.AppDatabase

class WordWidget : AppWidgetProvider() {
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UnspecifiedImmutableFlag")

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        for (widgetId in appWidgetIds) {
            val remoteViews = RemoteViews(context.packageName, R.layout.quran_widget)

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val db = AppDatabase.getDatabase(context).getWordsDao().getRandomWord()
                    remoteViews.setTextViewText(R.id.body, db.body)
                    remoteViews.setTextViewText(R.id.title, db.body.split(" ")[0])

                } catch (e: Exception) {
                    remoteViews.setTextViewText(R.id.title_text, "")
                    remoteViews.setTextViewText(
                        R.id.body,
                        "Кэш приложения пуст или поврежден)"
                    )
                }
                val intent = Intent(context, WordWidget::class.java)
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                remoteViews.setOnClickPendingIntent(R.id.container, pendingIntent)
                appWidgetManager.updateAppWidget(widgetId, remoteViews)
            }
        }
    }
}
