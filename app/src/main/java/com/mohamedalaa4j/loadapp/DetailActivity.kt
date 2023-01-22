package com.mohamedalaa4j.loadapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mohamedalaa4j.loadapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fileName = intent.getStringExtra("FILE_NAME_KEY").toString()
        val status = intent.getStringExtra("STATUS_KEY").toString()

        binding.tvFileNameValue.text = fileName
        binding.tvStatusValue.text = status

        if (status == "Failed") binding.tvStatusValue.setTextColor(getColor(R.color.red))

        binding.btnOk.setOnClickListener {
            onBackPressed()
            finish()
        }
    }
}