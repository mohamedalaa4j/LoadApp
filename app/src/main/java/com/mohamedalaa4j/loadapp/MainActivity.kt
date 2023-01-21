package com.mohamedalaa4j.loadapp

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mohamedalaa4j.loadapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var repoUrl: String
    private var downloadID: Long = 0
    private var downloading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.customButton.setOnClickListener {

            if (binding.rbNd940C3.isChecked || binding.rbRetrofit.isChecked || binding.rbGlide.isChecked) {
                lifecycleScope.launch(Dispatchers.IO) {
                    download()
                }
            } else {
                Toast.makeText(this, getString(R.string.choose_a_repository), Toast.LENGTH_SHORT).show()
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rbNd940_c3 -> {
                    repoUrl = ND940U_URL
                    binding.customButton.buttonState = ButtonState.AnotherDownload
                }
                R.id.rbRetrofit -> {
                    repoUrl = RETROFIT_URL
                    binding.customButton.buttonState = ButtonState.AnotherDownload
                }
                R.id.rbGlide -> {
                    repoUrl = GLIDE_URL
                    binding.customButton.buttonState = ButtonState.AnotherDownload
                }

            }
        }


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

        // Progress
        while (downloading) {
            val query = DownloadManager.Query().setFilterById(downloadID)
            val cursor = downloadManager.query(query)

            cursor.moveToFirst()

            val bytesDownloaded = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
            val bytesTotal = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

            val progress = (bytesDownloaded * 100L) / bytesTotal

            if (progress >= 0L) {
                binding.customButton.progressPercentage = progress.toDouble() / 100
                binding.customButton.buttonState = ButtonState.Loading
            }

            if (cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                downloading = false
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (id == downloadID) {
                binding.customButton.buttonState = ButtonState.Completed
            }
        }
    }

}