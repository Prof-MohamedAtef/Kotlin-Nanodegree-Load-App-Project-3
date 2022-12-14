package com.udacity.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.udacity.R
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var status: String? = null
    private var fileName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        intent = intent
        fileName = intent.getStringExtra(applicationContext.getString(R.string.file_name))
        status = intent.getStringExtra(applicationContext.getString(R.string.status_))

        binding.contentDetailLayout.tvFileNameValue.setText(fileName)
        binding.contentDetailLayout.tvStatusValue.setText(status)
    }
}