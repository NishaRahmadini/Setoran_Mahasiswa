package com.example.setoranhafalanapp.ui.setoran // Pastikan package name sesuai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
// ==============================================================
// Tahap 8: Import model data yang dibutuhkan
import com.example.setoranhafalanapp.model.SetoranData
import com.example.setoranhafalanapp.model.SimpanSetoranRequest
import com.example.setoranhafalanapp.model.SetoranMahasiswaData
// ==============================================================
import com.example.setoranhafalanapp.network.RetrofitClient // Import RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
// ==============================================================
// Tahap 8: Import binding class untuk activity_setoran.xml
import com.example.setoranhafalanapp.databinding.ActivitySetoranBinding
// ==============================================================
// Import library untuk RecyclerView (jika Anda menggunakannya)
import androidx.recyclerview.widget.LinearLayoutManager
// import com.example.setoranhafalanapp.ui.setoran.adapter.YourSetoranAdapter // Import Adapter Anda


class SetoranActivity : AppCompatActivity() {

    // ==============================================================
    // Tahap 8: Deklarasikan binding object
    private lateinit var binding: ActivitySetoranBinding
    // ==============================================================
    // TODO: Deklarasikan Adapter untuk RecyclerView (jika menggunakan RecyclerView)
    // private lateinit var setoranAdapter: YourSetoranAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ==============================================================
        // Tahap 8: Inflate layout menggunakan View Binding
        binding = ActivitySetoranBinding.inflate(layoutInflater)
        setContentView(binding.root) // Set root view dari binding object
        // ==============================================================

        // TODO: Inisialisasi RecyclerView (atur LayoutManager dan Adapter)
        // Jika Anda menggunakan RecyclerView di activity_setoran.xml
        /*
        binding.recyclerViewSetoran.layoutManager = LinearLayoutManager(this)
        setoranAdapter = YourSetoranAdapter(emptyList()) { clickedItem ->
            // Handle item click jika perlu
            // Misalnya, tampilkan detail setoran atau opsi edit/delete
        }
        binding.recyclerViewSetoran.adapter = setoranAdapter
        */


        // TODO: Ambil token dan API Key dari penyimpanan lokal (misalnya SharedPreferences)
        val sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val apiKey = "YOUR_API_KEY" // Ganti dengan API Key Anda yang sebenarnya
        val authToken = sharedPref.getString("access_token", null) // Ambil access token yang disimpan setelah login

        // TODO: Ambil NIM mahasiswa. Ini bisa didapat dari berbagai sumber:
        // 1. Dari Intent yang dikirim dari Activity sebelumnya (misalnya MainActivity)
        //    val nimMahasiswa = intent.getStringExtra("EXTRA_NIM") ?: "default_nim" // Pastikan NIM tidak null
        // 2. Dari data profil pengguna yang disimpan setelah login
        //    val nimMahasiswa = sharedPref.getString("user_nim", null)
        // 3. Atau jika hanya untuk 1 user, hardcode dulu untuk testing (TIDAK DISARANKAN UNTUK APLIKASI NYATA)
        val nimMahasiswa = "12350114004" // Contoh NIM, GANTI DENGAN LOGIKA AMBIL NIM ASLI

        // ==============================================================
        // Tahap 8: Lakukan panggilan API untuk mendapatkan data setoran saat Activity dibuat
        if (authToken != null && nimMahasiswa != null) {
            getSetoranMahasiswa(nimMahasiswa, apiKey, authToken)
        } else {
            Toast.makeText(this, "Anda belum login atau NIM tidak tersedia", Toast.LENGTH_SHORT).show()
            // TODO: Arahkan pengguna ke layar login jika token tidak ada
        }
        // ==============================================================


        // ==============================================================
        // Tahap 8: Contoh listener untuk tombol simpan setoran
        binding.buttonSimpanSetoran.setOnClickListener {
            if (authToken != null && nimMahasiswa != null) {
                // TODO: Ambil data setoran yang ingin disimpan dari UI (misalnya dari Checkbox atau daftar yang diedit)
                // Data ini harus berupa List<SetoranData> sesuai struktur SimpanSetoranRequest

                // Contoh dummy data yang ingin disimpan (ganti dengan data asli dari UI)
                val dataSetoranUntukDisimpan = listOf(
                    SetoranData(id_komponen_setoran = "id_surat_1", nama_komponen_setoran = "An-Naba'"),
                    SetoranData(id_komponen_setoran = "id_surat_2", nama_komponen_setoran = "An-Nazi'at")
                    // ... tambahkan data setoran lainnya sesuai yang dipilih/diedit di UI
                )
                // Pastikan dataSetoranUntukDisimpan tidak kosong jika Anda tidak ingin mengirim request kosong
                if (dataSetoranUntukDisimpan.isNotEmpty()) {
                    simpanSetoran(nimMahasiswa, apiKey, authToken, dataSetoranUntukDisimpan)
                } else {
                    Toast.makeText(this, "Tidak ada setoran untuk disimpan", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Anda belum login atau NIM tidak tersedia", Toast.LENGTH_SHORT).show()
                // TODO: Arahkan pengguna ke layar login
            }
        }
        // ==============================================================


        // TODO: Tambahkan listener untuk operasi delete jika ada tombol atau aksi delete di UI
        // deleteSetoran(...)
    }

    // ==============================================================
    // Tahap 8: Fungsi untuk memanggil API GET setoran mahasiswa
    private fun getSetoranMahasiswa(nim: String, apiKey: String, authToken: String) {
        // TODO: Tampilkan indikator loading (misalnya ProgressBar atau SwipeRefreshLayout)
        // binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch { // Memulai coroutine di scope Activity/Fragment
            withContext(Dispatchers.IO) { // Pindah ke IO thread untuk operasi jaringan
                try {
                    // Panggil fungsi API dari ApiService
                    val response = RetrofitClient.apiService.getSetoranMahasiswa(nim, apiKey, "Bearer $authToken")

                    withContext(Dispatchers.Main) { // Kembali ke Main thread untuk update UI
                        // TODO: Sembunyikan indikator loading
                        // binding.progressBar.visibility = View.GONE

                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            if (apiResponse != null && apiResponse.response) { // Cek response sukses dari API dan boolean 'response'
                                // Data berhasil diterima
                                Log.d("SetoranActivity", "Data Setoran Sukses: ${apiResponse.message}")
                                val setoranDataDetailList = apiResponse.data?.setoran?.detail // Ambil list detail setoran

                                // TODO: Update UI, misalnya tampilkan data di RecyclerView atau View lainnya
                                if (setoranDataDetailList != null) {
                                    // Contoh: Jika menggunakan RecyclerView, update adapter dengan data baru
                                    // setoranAdapter.submitList(setoranDataDetailList) // Jika menggunakan ListAdapter
                                    // setoranAdapter.setData(setoranDataDetailList) // Jika menggunakan custom method di Adapter
                                    Log.d("SetoranActivity", "Jumlah item setoran detail: ${setoranDataDetailList.size}")
                                    // TODO: Tampilkan data info_dasar dan ringkasan di UI juga
                                    val infoDasar = apiResponse.data.setoran.infoDasar
                                    val ringkasan = apiResponse.data.setoran.ringkasan
                                    Log.d("SetoranActivity", "Total sudah setor: ${infoDasar.totalSudahSetor}")

                                } else {
                                    // Data list detail setoran kosong atau null
                                    Log.d("SetoranActivity", "Data detail setoran kosong atau null")
                                    Toast.makeText(this@SetoranActivity, "Data setoran tidak ditemukan.", Toast.LENGTH_SHORT).show()
                                    // TODO: Tampilkan pesan "data kosong" di UI
                                }

                            } else {
                                // Respons API menunjukkan kegagalan (boolean 'response' false)
                                Log.e("SetoranActivity", "API Response Error: ${apiResponse?.message}")
                                Toast.makeText(this@SetoranActivity, "Gagal memuat data setoran: ${apiResponse?.message}", Toast.LENGTH_SHORT).show()
                                // TODO: Tampilkan pesan error dari API ke pengguna
                            }
                        } else {
                            // Gagal mendapatkan data dari server (misalnya kode status 401 Unauthorized, 404 Not Found, dll.)
                            val errorBody = response.errorBody()?.string()
                            Log.e("SetoranActivity", "HTTP Error Get Setoran: ${response.code()} - ${response.message()} - Error Body: $errorBody")

                            // TODO: Handle error spesifik, seperti 401 Unauthorized (perlu refresh token atau login ulang)
                            if (response.code() == 401) {
                                // Token tidak valid atau kadaluarsa, coba refresh token atau minta user login ulang
                                Toast.makeText(this@SetoranActivity, "Sesi habis, silakan login ulang.", Toast.LENGTH_LONG).show()
                                // TODO: Panggil fungsi refresh token atau arahkan ke login
                            } else {
                                Toast.makeText(this@SetoranActivity, "Gagal memuat data setoran: Error ${response.code()}", Toast.LENGTH_SHORT).show()
                                // TODO: Tampilkan pesan error umum
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Error jaringan atau exception lainnya (misalnya koneksi terputus, GSON parsing error)
                    Log.e("SetoranActivity", "Error selama Get Setoran", e)
                    withContext(Dispatchers.Main) {
                        // TODO: Sembunyikan indikator loading
                        // binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@SetoranActivity, "Terjadi kesalahan jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // ==============================================================
    // Tahap 8: Fungsi untuk memanggil API POST simpan setoran
    private fun simpanSetoran(nim: String, apiKey: String, authToken: String, dataSetoran: List<SetoranData>) {
        // TODO: Tampilkan indikator loading

        val requestBody = SimpanSetoranRequest(data_setoran = dataSetoran, tgl_setoran = "2025-04-27") // Tanggal opsional

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.apiService.simpanSetoran(nim, apiKey, requestBody, "Bearer $authToken")
                    withContext(Dispatchers.Main) {
                        // TODO: Sembunyikan indikator loading
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            if (apiResponse != null && apiResponse.response) {
                                // Setoran berhasil disimpan/divalidasi
                                Log.d("SetoranActivity", "Simpan Setoran Sukses: ${apiResponse.message}")
                                Toast.makeText(this@SetoranActivity, "Setoran berhasil disimpan!", Toast.LENGTH_SHORT).show()
                                // TODO: Refresh data setoran setelah menyimpan jika perlu
                                // getSetoranMahasiswa(nim, apiKey, authToken)
                            } else {
                                // Respons API menunjukkan kegagalan
                                Log.e("SetoranActivity", "API Response Error Simpan: ${apiResponse?.message}")
                                Toast.makeText(this@SetoranActivity, "Gagal menyimpan setoran: ${apiResponse?.message}", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Gagal simpan setoran dari server (misalnya validasi gagal, dll.)
                            val errorBody = response.errorBody()?.string()
                            Log.e("SetoranActivity", "HTTP Error Simpan Setoran: ${response.code()} - ${response.message()} - Error Body: $errorBody")

                            if (response.code() == 401) {
                                // Token tidak valid, perlu refresh atau login ulang
                                Toast.makeText(this@SetoranActivity, "Sesi habis, silakan login ulang.", Toast.LENGTH_LONG).show()
                                // TODO: Panggil refresh token atau arahkan ke login
                            } else {
                                Toast.makeText(this@SetoranActivity, "Gagal menyimpan setoran: Error ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Error jaringan atau exception lainnya
                    Log.e("SetoranActivity", "Error selama Simpan Setoran", e)
                    withContext(Dispatchers.Main) {
                        // TODO: Sembunyikan indikator loading
                        Toast.makeText(this@SetoranActivity, "Terjadi kesalahan jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // ==============================================================
    // Tahap 8: Fungsi untuk memanggil API DELETE setoran (Contoh)
    private fun deleteSetoran(nim: String, apiKey: String, authToken: String, dataSetoranToDelete: List<SetoranData>) {
        // TODO: Tampilkan indikator loading

        // Untuk request DELETE setoran, body-nya juga berstruktur SimpanSetoranRequest tapi hanya membutuhkan 'id' dari SetoranData
        // Nama komponen mungkin tidak wajib di body DELETE tapi disertakan di contoh Postman. Sesuaikan jika API memerlukan struktur berbeda.
        val requestBody = SimpanSetoranRequest(data_setoran = dataSetoranToDelete)


        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.apiService.deleteSetoran(nim, apiKey, requestBody, "Bearer $authToken")
                    withContext(Dispatchers.Main) {
                        // TODO: Sembunyikan indikator loading

                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            if (apiResponse != null && apiResponse.response) {
                                // Setoran berhasil dihapus/dibatalkan
                                Log.d("SetoranActivity", "Delete Setoran Sukses: ${apiResponse.message}")
                                Toast.makeText(this@SetoranActivity, "Setoran berhasil dibatalkan!", Toast.LENGTH_SHORT).show()
                                // TODO: Refresh data setoran setelah menghapus jika perlu
                                // getSetoranMahasiswa(nim, apiKey, authToken)
                            } else {
                                // Respons API menunjukkan kegagalan
                                Log.e("SetoranActivity", "API Response Error Delete: ${apiResponse?.message}")
                                Toast.makeText(this@SetoranActivity, "Gagal membatalkan setoran: ${apiResponse?.message}", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Gagal hapus setoran dari server
                            val errorBody = response.errorBody()?.string()
                            Log.e("SetoranActivity", "HTTP Error Delete Setoran: ${response.code()} - ${response.message()} - Error Body: $errorBody")

                            if (response.code() == 401) {
                                // Token tidak valid, perlu refresh atau login ulang
                                Toast.makeText(this@SetoranActivity, "Sesi habis, silakan login ulang.", Toast.LENGTH_LONG).show()
                                // TODO: Panggil refresh token atau arahkan ke login
                            } else {
                                Toast.makeText(this@SetoranActivity, "Gagal membatalkan setoran: Error ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Error jaringan atau exception lainnya
                    Log.e("SetoranActivity", "Error selama Delete Setoran", e)
                    withContext(Dispatchers.Main) {
                        // TODO: Sembunyikan indikator loading
                        Toast.makeText(this@SetoranActivity, "Terjadi kesalahan jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    // ==============================================================
}