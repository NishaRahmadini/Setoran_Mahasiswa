package com.example.setoranhafalanapp.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Objek singleton untuk menyediakan instance Retrofit
object RetrofitClient {

    // ==============================================================
    // Tahap 5: Definisikan Base URLs
    private const val BASE_URL_API = "https://api.tif.uin-suska.ac.id"
    private const val BASE_URL_KC = "https://id.tif.uin-suska.ac.id"
    // ==============================================================


    // Interceptor untuk logging request dan response (berguna saat debug)
    // Hapus atau ubah ke Level.NONE saat aplikasi rilis (production)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Level.BODY akan mencetak body request/response
    }

    // OkHttpClient dengan logging interceptor dan timeout
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS) // Contoh: timeout koneksi 30 detik
        .readTimeout(30, TimeUnit.SECONDS)    // Contoh: timeout baca 30 detik
        .writeTimeout(30, TimeUnit.SECONDS)   // Contoh: timeout tulis 30 detik
        .build()

    // ==============================================================
    // Tahap 5: Instance Retrofit untuk endpoint API Setoran Hafalan (menggunakan BASE_URL_API)
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_API) // Gunakan URL_API untuk endpoint setoran, status, health
            .addConverterFactory(GsonConverterFactory.create()) // Konverter JSON
            .client(client) // Gunakan client OkHttp yang sudah dikonfigurasi
            .build()
            .create(ApiService::class.java)
    }

    // Tahap 5: Instance Retrofit terpisah untuk endpoint Autentikasi Keycloak (menggunakan BASE_URL_KC)
    val kcAuthService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_KC) // Gunakan KC_URL untuk endpoint login, userinfo, logout, refresh
            .addConverterFactory(GsonConverterFactory.create())
            .client(client) // Gunakan client OkHttp yang sama
            .build()
            .create(ApiService::class.java)
    }
    // ==============================================================
}