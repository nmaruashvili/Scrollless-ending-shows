package com.example.scrolllessendingshows.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.scrolllessendingshows.databinding.ActivityMainBinding
import com.example.scrolllessendingshows.presentation.screens.list.TvShowListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .add(binding.container.id, TvShowListFragment.newInstance())
            .commit()
    }
}