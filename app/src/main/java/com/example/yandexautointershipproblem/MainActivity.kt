package com.example.yandexautointershipproblem

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.yandexautointershipproblem.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val navController = findNavController(R.id.nav_host_fragment)
////        val view: BottomNavigationView = binding.bottomNav
//        NavigationUI.setupWithNavController(view, navController)
//        NavigationUI.setupActionBarWithNavController(this, navController);
    }
}
