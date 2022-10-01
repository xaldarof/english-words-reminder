package xaldarof.dictionary.english

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.unical.programm.workmanager.worker.MainWorker
import xaldarof.dictionary.english.data.AppDatabase
import xaldarof.dictionary.english.databinding.ActivityMainBinding
import xaldarof.dictionary.english.domain.WordEntity
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val workManager = WorkManager.getInstance(this)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        val db = AppDatabase.getDatabase(this@MainActivity).getWordsDao()

        lifecycleScope.launch {
            binding.start.isGone = db.getCount() > 0L
            binding.active.isVisible = db.getCount() > 0L
        }


        binding.start.setOnClickListener {
            binding.progress.visibility = View.VISIBLE
            binding.start.visibility = View.GONE
            Thread {
                val fileName = "eng-rus.txt"
                application.assets.open(fileName).bufferedReader().use {
                    it.lines().forEach { line ->
                        CoroutineScope(Dispatchers.IO).launch {
                            if (line.contains("]") || line.contains("[")) {
                                val startIndex = line.indexOf("[")
                                val endIndex = line.indexOf("]")

                                val withoutBreak = line.replaceRange(
                                    startIndex,
                                    endIndex + 1,
                                    " - "
                                )

                                val withoutN = withoutBreak.replace("_n.", "").replace(">", ": ")
                                    .replace("_фр.", "").replace("_а.", "").replace("_a.", "")
                                    .replaceFirst("_бот.", "")
                                    .replaceFirst("1.", "")
                                    .replace("_жд.", "")

                                db.insertWord(
                                    WordEntity(
                                        withoutN
                                    )
                                )
                            }
                        }
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.progress.visibility = View.GONE
                        binding.start.visibility = View.GONE
                        binding.active.isVisible = true
                        initWorker()
                    }
                }
            }.start()
        }
    }


    private fun initWorker() {
        val work = PeriodicWorkRequest.Builder(MainWorker::class.java, 30, TimeUnit.MINUTES)
            .build()

        workManager.enqueueUniquePeriodicWork("tag", ExistingPeriodicWorkPolicy.KEEP, work)
    }
}