package com.mohamedalaa4j.loadapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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


    }
}