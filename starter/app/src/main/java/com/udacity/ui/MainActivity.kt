package com.udacity.ui

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import com.udacity.NotificationBuilder
import com.udacity.R
import com.udacity.Util
import com.udacity.customview.ButtonState
import com.udacity.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var urlSelected: String
    private var fileName: String = ""
    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.contentMainIncluded.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.glide_radioBtn -> {
                    fileName = getString(R.string.glide_radio_title)
                    urlSelected = Util.URL_GLIDE
                }
                R.id.load_app_radioBtn -> {
                    fileName = getString(R.string.load_app_radio_title)
                    urlSelected = Util.URL_APP
                }
                R.id.retrofit_radioBtn -> {
                    fileName = getString(R.string.retrofit_radio_title)
                    urlSelected = Util.URL_RETROFIT
                }
            }
        }

        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            if (retrofit_radioBtn.isChecked == false && load_app_radioBtn.isChecked == false && glide_radioBtn.isChecked == false) {
                Toast.makeText(this, "select file please.", Toast.LENGTH_LONG).show()
            } else {
                download()
                custom_button.buttonState = ButtonState.Loading
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            custom_button.buttonState = ButtonState.Completed

            val downloadMgr = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val query = DownloadManager.Query()
            query.setFilterById(id!!)

            val cursor = downloadMgr.query(query)

            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                if (status == DownloadManager.STATUS_FAILED) {
                    //create notification with failed status
                    NotificationBuilder.send(fileName, "failed", applicationContext)
                } else {
                    //create notification with success status
                    NotificationBuilder.send(fileName, "success", applicationContext)
                }
            }
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }
}