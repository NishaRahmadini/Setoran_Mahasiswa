package com.example.setoranhafalanapp.ui.auth // Pastikan package name sesuai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast // Untuk menampilkan pesan singkat
import androidx.lifecycle.lifecycleScope // Untuk menggunakan Coroutines dengan lifecycle Activity
import com.example.setoranhafalanapp.network.RetrofitClient // Import RetrofitClient
import kotlinx.coroutines.Dispatchers // Untuk berpindah thread (IO, Main)
import kotlinx.coroutines.launch // Untuk memulai Coroutine
import kotlinx.coroutines.withContext // Untuk berpindah thread sementara
// ==============================================================
// Tahap 7: Import binding class untuk activity_login.xml
import com.example.setoranhafalanapp.databinding.ActivityLoginBinding
// ==============================================================
import android.content.Intent // Untuk navigasi antar Activity
import com.example.setoranhafalanapp.ui.main.MainActivity // Target navigasi setelah login

class LoginActivity : AppCompatActivity() {

    // ==============================================================
    // Tahap 7: Deklarasikan binding object
    private lateinit var binding: ActivityLoginBinding
    // ==============================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ==============================================================
        // Tahap 7: Inflate layout menggunakan View Binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root) // Set root view dari binding object
        // ==============================================================

        // ==============================================================
        // Tahap 7: Atur listener untuk tombol login menggunakan binding object
        binding.buttonLogin.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()
            // Lakukan validasi input sederhana sebelum memanggil API
            if (username.isNotEmpty() && password.isNotEmpty()) {
                performLogin(username, password)
            } else {
                Toast.makeText(this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
        // ==============================================================

        // TODO: Anda mungkin ingin menambahkan logika untuk memeriksa apakah pengguna sudah login
        // dan langsung navigasi ke MainActivity jika sudah.
    }

    private fun performLogin(username: String, password: String) {
        // TODO: Tampilkan indikator loading (misalnya ProgressBar)
        // binding.progressBarLogin.visibility = View.VISIBLE

        // Menggunakan Coroutine untuk panggilan API di background thread (IO)
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    // Panggil fungsi login dari ApiService (melalui RetrofitClient)
                    val response = RetrofitClient.kcAuthService.login(
                        clientId = "setoran-mobile-dev", // Ganti dengan client_id yang benar dari dokumentasi API/Postman
                        clientSecret = "aqJp3xnXKudgC7RMOshEQP7ZoVKWzoSl", // Ganti dengan client_secret yang benar
                        grantType = "password", // Grant type untuk login dengan username/password
                        username = username,
                        password = password,
                        scope = "openid profile email" // Scope yang diminta
                    )

                    // Kembali ke Main thread untuk update UI atau navigasi
                    withContext(Dispatchers.Main) {
                        // TODO: Sembunyikan indikator loading
                        // binding.progressBarLogin.visibility = View.GONE

                        if (response.isSuccessful) {
                            val loginResponse = response.body()
                            if (loginResponse != null) {
                                // Login sukses!
                                Log.d("LoginActivity", "Login Sukses: ${loginResponse.accessToken}")

                                // ==============================================================
                                // Tahap 7: Simpan accessToken dan refreshToken dengan aman
                                // Gunakan SharedPreferences atau DataStore untuk menyimpan token
                                val sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                                with (sharedPref.edit()) {
                                    putString("access_token", loginResponse.accessToken)
                                    putString("refresh_token", loginResponse.refreshToken)
                                    // Anda mungkin juga ingin menyimpan informasi user lainnya
                                    // putString("username", username) // Contoh menyimpan username
                                    apply() // Gunakan apply() untuk menyimpan secara asynchronous
                                }
                                // ==============================================================

                                Toast.makeText(this@LoginActivity, "Login Berhasil!", Toast.LENGTH_SHORT).show()

                                // ==============================================================
                                // Tahap 7: Navigasi ke Activity berikutnya (MainActivity)
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                // Opsional: Tambahkan flag agar pengguna tidak bisa kembali ke layar login dengan tombol back
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish() // Tutup LoginActivity
                                // ==============================================================

                            } else {
                                // Response sukses tapi body null (jarang terjadi jika API benar)
                                Log.e("LoginActivity", "Login response body is null")
                                Toast.makeText(this@LoginActivity, "Login Gagal: Respons tidak valid", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Login gagal (misalnya username/password salah)
                            val errorBody = response.errorBody()?.string()
                            Log.e("LoginActivity", "Login Gagal: ${response.code()} - ${response.message()} - Error Body: $errorBody")
                            Toast.makeText(this@LoginActivity, "Login Gagal: Cek username/password Anda", Toast.LENGTH_SHORT).show()
                            // TODO: Parse errorBody untuk mendapatkan pesan error yang lebih spesifik jika API menyediakannya
                        }
                    }
                } catch (e: Exception) {
                    // Error jaringan atau exception lainnya (misalnya koneksi terputus)
                    Log.e("LoginActivity", "Error selama login", e)
                    withContext(Dispatchers.Main) {
                        // TODO: Sembunyikan indikator loading
                        // binding.progressBarLogin.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, "Terjadi kesalahan jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // TODO: Implementasikan fungsi untuk refresh token jika diperlukan di sini atau di tempat lain
    // private fun refreshAuthToken() { ... }
}