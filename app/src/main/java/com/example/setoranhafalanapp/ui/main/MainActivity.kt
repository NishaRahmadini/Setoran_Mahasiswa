package com.example.setoranhafalanapp.ui.main // Pastikan package name sudah berubah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.setoranhafalanapp.databinding.ActivityMainBinding
import android.content.Intent // Untuk navigasi
import com.example.setoranhafalanapp.ui.setoran.SetoranActivity // Import SetoranActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // Set root view dari binding object
        binding.buttonGoToSetoran.setOnClickListener {
            val intent = Intent(this, SetoranActivity::class.java)
            startActivity(intent)
        }
    }
}