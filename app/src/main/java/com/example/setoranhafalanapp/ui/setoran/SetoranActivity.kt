package com.example.setoranhafalanapp.ui.setoran // Pastikan package name sesuai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.setoranhafalanapp.model.SetoranData
import com.example.setoranhafalanapp.model.SimpanSetoranRequest
import com.example.setoranhafalanapp.model.SetoranMahasiswaData
import com.example.setoranhafalanapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.setoranhafalanapp.databinding.ActivitySetoranBinding
import androidx.recyclerview.widget.LinearLayoutManager

class SetoranActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetoranBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetoranBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val apiKey = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJVTUhSdlYzR1FjU0YzRWpsZmdtb0dETEpsWGZqOVBpZTdiWWVuNUxLRFZvIn0.eyJleHAiOjE3NDc3MjQzNjUsImlhdCI6MTc0NzcyMzQ2NSwianRpIjoib25ydHJvOmQ5YjgxODQ1LWIwYjgtNDU0Yy1iNjM1LTI2NzFhZDVmODI4NyIsImlzcyI6Imh0dHBzOi8vaWQudGlmLnVpbi1zdXNrYS5hYy5pZC9yZWFsbXMvZGV2Iiwic3ViIjoiZWFmOTNhMjQtYjQzNi00MDRhLTgwMTEtYTRhZmU2MDM4MzQ3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoic2V0b3Jhbi1tb2JpbGUtZGV2Iiwic2lkIjoiZjZjZDk5YmMtOWJkMi00YzM0LWFhMjUtM2YwYWJlYWZlNjE4IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJtYWhhc2lzd2EiXX0sInNjb3BlIjoib3BlbmlkIGVtYWlsIHJvbGVzIHByb2ZpbGUiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6Ik5JU0hBIFJBSE1BRElOSSBTWUFIREEiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiIxMjM1MDEyMTM2NiIsImdpdmVuX25hbWUiOiJOSVNIQSBSQUhNQURJTkkiLCJmYW1pbHlfbmFtZSI6IlNZQUhEQSIsImVtYWlsIjoiMTIzNTAxMjEzNjZAc3R1ZGVudHMudWluLXN1c2thLmFjLmlkIn0.Axgu7oOPV5z6OEMVino2q0aSmjDsojskCmPUNblPjNf4VqhCHH3b5o5XJzAc7aC498JiUHrmYahhdna_K1hyXwjydb1s4N7woRT7OqX-XlVGkq4zF89X6A8hUBTXPdh8otATQjXzLAbiDY6oe6yVoB7D3WhPYmqL3HGI0dTOkv8IBWBuEymgT3x76uzbxP5mTTJ2xdyZRxEEW1TpET5HOk4vEPRFfUXxEsaNS-qQPVGDubmRSrf8Dnie0kjsse8flvXvdWOecdaDIaFQ9fUUi9q0vA7FVdOj6N5GrzKSRANxNc5pdmU_jbzUyXssJzFZG-11GlI62STylkCMoCREcQ" // Ganti dengan API Key Anda yang sebenarnya
        val authToken = sharedPref.getString("access_token", null)
        val nimMahasiswa = sharedPref.getString("user_nim", null)
        if (authToken != null && nimMahasiswa != null) {
            Log.d("DEBUG", "Token: Bearer $authToken")
            Log.d("DEBUG", "NIM: $nimMahasiswa")
            Log.d("DEBUG", "API KEY: $apiKey")
            getSetoranMahasiswa(nimMahasiswa, apiKey, authToken)
        } else {
            Toast.makeText(this, "Anda belum login atau NIM tidak tersedia", Toast.LENGTH_SHORT).show()
        }
        binding.buttonSimpanSetoran.setOnClickListener {
            if (authToken != null && nimMahasiswa != null) {
                val dataSetoranUntukDisimpan = listOf(
                    SetoranData(id_komponen_setoran = "id_surat_1", nama_komponen_setoran = "An-Naba'"),
                    SetoranData(id_komponen_setoran = "id_surat_2", nama_komponen_setoran = "An-Nazi'at")
                )
                if (dataSetoranUntukDisimpan.isNotEmpty()) {
                    simpanSetoran(nimMahasiswa, apiKey, authToken, dataSetoranUntukDisimpan)
                } else {
                    Toast.makeText(this, "Tidak ada setoran untuk disimpan", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Anda belum login atau NIM tidak tersedia", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getSetoranMahasiswa(nim: String, apiKey: String, authToken: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.apiService.getSetoranMahasiswa(nim, apiKey, "Bearer $authToken")

                    withContext(Dispatchers.Main) {

                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            if (apiResponse != null && apiResponse.response) {
                                Log.d("SetoranActivity", "Data Setoran Sukses: ${apiResponse.message}")
                                val setoranDataDetailList = apiResponse.data?.setoran?.detail
                                if (setoranDataDetailList != null) {
                                    Log.d("SetoranActivity", "Jumlah item setoran detail: ${setoranDataDetailList.size}")
                                    val infoDasar = apiResponse.data.setoran.infoDasar
                                    val ringkasan = apiResponse.data.setoran.ringkasan
                                    Log.d("SetoranActivity", "Total sudah setor: ${infoDasar.totalSudahSetor}")

                                } else {
                                    Log.d("SetoranActivity", "Data detail setoran kosong atau null")
                                    Toast.makeText(this@SetoranActivity, "Data setoran tidak ditemukan.", Toast.LENGTH_SHORT).show()
                                }

                            } else {
                                Log.e("SetoranActivity", "API Response Error: ${apiResponse?.message}")
                                Toast.makeText(this@SetoranActivity, "Gagal memuat data setoran: ${apiResponse?.message}", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("SetoranActivity", "HTTP Error Get Setoran: ${response.code()} - ${response.message()} - Error Body: $errorBody")
                            if (response.code() == 401) {
                                Toast.makeText(this@SetoranActivity, "Sesi habis, silakan login ulang.", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this@SetoranActivity, "Gagal memuat data setoran: Error ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SetoranActivity", "Error selama Get Setoran", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SetoranActivity, "Terjadi kesalahan jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun simpanSetoran(nim: String, apiKey: String, authToken: String, dataSetoran: List<SetoranData>) {

        val requestBody = SimpanSetoranRequest(data_setoran = dataSetoran, tgl_setoran = "2025-04-27")
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.apiService.simpanSetoran(nim, apiKey, requestBody, "Bearer $authToken")
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            if (apiResponse != null && apiResponse.response) {
                                Log.d("SetoranActivity", "Simpan Setoran Sukses: ${apiResponse.message}")
                                Toast.makeText(this@SetoranActivity, "Setoran berhasil disimpan!", Toast.LENGTH_SHORT).show()
                            } else {
                                Log.e("SetoranActivity", "API Response Error Simpan: ${apiResponse?.message}")
                                Toast.makeText(this@SetoranActivity, "Gagal menyimpan setoran: ${apiResponse?.message}", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("SetoranActivity", "HTTP Error Simpan Setoran: ${response.code()} - ${response.message()} - Error Body: $errorBody")
                            if (response.code() == 401) {
                                Toast.makeText(this@SetoranActivity, "Sesi habis, silakan login ulang.", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this@SetoranActivity, "Gagal menyimpan setoran: Error ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SetoranActivity", "Error selama Simpan Setoran", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SetoranActivity, "Terjadi kesalahan jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun deleteSetoran(nim: String, apiKey: String, authToken: String, dataSetoranToDelete: List<SetoranData>) {
        val requestBody = SimpanSetoranRequest(data_setoran = dataSetoranToDelete)
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.apiService.deleteSetoran(nim, apiKey, requestBody, "Bearer $authToken")
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            if (apiResponse != null && apiResponse.response) {
                                Log.d("SetoranActivity", "Delete Setoran Sukses: ${apiResponse.message}")
                                Toast.makeText(this@SetoranActivity, "Setoran berhasil dibatalkan!", Toast.LENGTH_SHORT).show()
                            } else {
                                Log.e("SetoranActivity", "API Response Error Delete: ${apiResponse?.message}")
                                Toast.makeText(this@SetoranActivity, "Gagal membatalkan setoran: ${apiResponse?.message}", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("SetoranActivity", "HTTP Error Delete Setoran: ${response.code()} - ${response.message()} - Error Body: $errorBody")

                            if (response.code() == 401) {
                                Toast.makeText(this@SetoranActivity, "Sesi habis, silakan login ulang.", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this@SetoranActivity, "Gagal membatalkan setoran: Error ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SetoranActivity", "Error selama Delete Setoran", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SetoranActivity, "Terjadi kesalahan jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}