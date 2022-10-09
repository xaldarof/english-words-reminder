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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xaldarof.dictionary.english.databinding.ActivityMainBinding
import xaldarof.dictionary.english.domain.repositories.WordsRepository
import xaldarof.dictionary.english.service.Worker
import xaldarof.dictionary.english.tools.clearTrash
import xaldarof.dictionary.english.tools.fileName
import xaldarof.dictionary.english.tools.workerTag
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val workManager = WorkManager.getInstance(this)

    @Inject
    lateinit var repository: WordsRepository

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        checkAppState()
        handleClicks()

    }

    private fun checkAppState() {
        lifecycleScope.launch {
            binding.start.isGone = repository.getCount() > 0L
            binding.active.isVisible = repository.getCount() > 0L
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun handleClicks() {
        binding.start.setOnClickListener {
            hideViews()
            startCaching()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun startCaching() {
        Thread {
            application.assets.open(fileName).bufferedReader().use {
                it.lines().forEach { line ->
                    CoroutineScope(Dispatchers.IO).launch {
                        line.clearTrash { word ->
                            repository.insertWord(word)
                        }
                    }
                }
            }
            updateViews()
        }.start()
    }

    private fun hideViews() {
        binding.progress.visibility = View.VISIBLE
        binding.start.visibility = View.GONE
    }

    private fun updateViews() {
        lifecycleScope.launch {
            binding.progress.visibility = View.GONE
            binding.start.visibility = View.GONE
            binding.active.isVisible = true
            initWorker()
        }
    }

    private fun initWorker() {
        val work = PeriodicWorkRequest.Builder(Worker::class.java, 30, TimeUnit.MINUTES)
            .build()

        workManager.enqueueUniquePeriodicWork(workerTag,
            ExistingPeriodicWorkPolicy.KEEP,
            work)
    }
}