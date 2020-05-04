package com.example.yandexautointernshipproblem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yandexautointernshipproblem.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
