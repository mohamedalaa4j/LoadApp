package com.mohamedalaa4j.loadapp

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.mohamedalaa4j.loadapp.databinding.ActivityMainBinding
import com.mohamedalaa4j.loadapp.utilities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var repoUrl: String
    private lateinit var fileName: String
    private var downloadID: Long = 0
    private var downloading = false
    private var downloadStatus = "Failed"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.customButton.setOnClickListener {

            if (binding.rbNd940C3.isChecked || binding.rbRetrofit.isChecked || binding.rbGlide.isChecked) {
                lifecycleScope.launch(Dispatchers.IO) {
                    download()
                }
                binding.customButton.buttonState = ButtonState.Clicked
            } else {
                Toast.makeText(this, getString(R.string.choose_a_repository), Toast.LENGTH_SHORT).show()
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                binding.rbNd940C3.id -> {
                    repoUrl = ND940U_URL
                    binding.customButton.buttonState = ButtonState.Reset
                    fileName = "nd940u"
                }
                binding.rbRetrofit.id -> {
                    repoUrl = RETROFIT_URL
                    binding.customButton.buttonState = ButtonState.Reset
                    fileName = "Retrofit"
                }
                binding.rbGlide.id -> {
                    repoUrl = GLIDE_URL
                    binding.customButton.buttonState = ButtonState.Reset
                    fileName = "Glide"
                }

            }
        }

        createNotificationChannel()
    }

    private fun download() {
        val request = DownloadManager.Request(Uri.parse(repoUrl))
            .setTitle(getString(R.string.app_name))
            .setDescription(getString(R.string.app_description))
            .setRequiresCharging(false)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        downloadID = downloadManager.enqueue(request)
        downloading = true

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        //region Download progress analysing
        while (downloading) {
            val query = DownloadManager.Query().setFilterById(downloadID)
            val cursor = downloadManager.query(query)

            try {
                cursor.moveToFirst()

                val bytesDownloaded = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                val bytesTotal = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                val progress = (bytesDownloaded * 100L) / bytesTotal

                if (progress >= 0L) {

                    // Update download progress
                    binding.customButton.progressPercentage = progress.toDouble() / 100
                    binding.customButton.buttonState = ButtonState.Loading
                }

                if (cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                    downloadStatus = "Successful"
                    binding.customButton.buttonState = ButtonState.Completed
                } else {
                    downloadStatus = "Failed"
                }

            } catch (e: Exception) { // Handel download canceling
                downloading = false
            }
        }
        //endregion
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (id == downloadID) {
                binding.customButton.buttonState = ButtonState.Completed
                sendDownloadFinishedNotification(fileName, downloadStatus)
            }
        }
    }

    private fun createNotificationChannel() {

        // Create channel for notification if the android version is Oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(DOWNLOAD_NOTIFICATION_CHANNEL, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendDownloadFinishedNotification(filename: String, status: String) {

        // NotificationManager instance
        val notificationManager = ContextCompat.getSystemService(
            this@MainActivity,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(getString(R.string.download_finished), this@MainActivity, filename, status)
    }

}