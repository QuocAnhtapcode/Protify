package com.example.hoangquocanh.test

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.hoangquocanh.databinding.ActivityTestBinding
import kotlinx.coroutines.launch

class TestActivity: AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    private val viewmodel: TestViewmodel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val url = "https://static.apero.vn/techtrek/AnhKhongLamGiDauAnhThe.mp3"
        val fileName = "AnhKhongLamGiDauAnhThe.mp3"
        binding.increaseA.setOnClickListener{
            lifecycleScope.launch {
                try {
                    viewmodel.downloadMp3(this@TestActivity, url, fileName)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        binding.increaseB.setOnClickListener{

        }
    }
}