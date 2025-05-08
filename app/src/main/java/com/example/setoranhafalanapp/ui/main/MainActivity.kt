package com.example.setoranhafalanapp.ui.main // Pastikan package name sudah berubah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
// ==============================================================
// Tahap 6: Import binding class untuk activity_main.xml
import com.example.setoranhafalanapp.databinding.ActivityMainBinding
// ==============================================================
import android.content.Intent // Untuk navigasi
import com.example.setoranhafalanapp.ui.setoran.SetoranActivity // Import SetoranActivity

class MainActivity : AppCompatActivity() { // Ubah dari ComponentActivity ke AppCompatActivity

    // ==============================================================
    // Tahap 6: Deklarasikan binding object
    private lateinit var binding: ActivityMainBinding
    // ==============================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge() // <-- Hapus baris ini karena terkait Compose/EdgeToEdge
        // setContent { ... } // <-- Hapus blok setContent dan isinya

        // ==============================================================
        // Tahap 6: Inflate layout menggunakan View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // Set root view dari binding object
        // ==============================================================

        // TODO: Lakukan operasi yang relevan untuk layar utama di sini
        // Contoh: Menampilkan nama pengguna yang login (ambil dari SharedPreferences)
        // val sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        // val username = sharedPref.getString("username", "Pengguna") // Ganti dengan cara mengambil nama pengguna
        // binding.textViewWelcome.text = "Selamat Datang, $username!"

        // ==============================================================
        // Tahap 6: Contoh: Mengatur listener untuk navigasi ke layar setoran
        binding.buttonGoToSetoran.setOnClickListener {
            val intent = Intent(this, SetoranActivity::class.java)
            startActivity(intent)
        }
        // ==============================================================
    }

    // Hapus fungsi Greeting dan GreetingPreview karena terkait Compose
    // @Composable
    // fun Greeting(name: String, modifier: Modifier = Modifier) { ... }
    // @Preview(showBackground = true)
    // @Composable
    // fun GreetingPreview() { ... }
}