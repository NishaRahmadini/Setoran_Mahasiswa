package com.example.setoranhafalanapp.network

// ==============================================================
// Tahap 5: Import semua model data class yang akan digunakan
import com.example.setoranhafalanapp.model.*
// ==============================================================

import retrofit2.Response
import retrofit2.http.*

// Interface yang mendefinisikan semua endpoint API
interface ApiService {

    // Endpoint untuk menyimpan setoran (POST)
    @POST("/setoran-dev/v1/mahasiswa/setoran/{nim}")
    suspend fun simpanSetoran(
        @Path("nim") nim: String,
        @Query("apikey") apiKey: String, // API Key sebagai query parameter
        @Body request: SimpanSetoranRequest, // Body request menggunakan data class SimpanSetoranRequest
        @Header("Authorization") authToken: String // Bearer Token di Header
    ): Response<ApiResponse<Void>> // Response umum, data null untuk operasi ini

    // Endpoint untuk menghapus setoran (DELETE)
    @DELETE("/setoran-dev/v1/mahasiswa/setoran/{nim}")
    suspend fun deleteSetoran(
        @Path("nim") nim: String,
        @Query("apikey") apiKey: String,
        @Body request: SimpanSetoranRequest, // Struktur body bisa sama atau berbeda dengan simpan (sesuai Postman)
        @Header("Authorization") authToken: String
    ): Response<ApiResponse<Void>>

    // Endpoint untuk mendapatkan data setoran mahasiswa (GET)
    @GET("/setoran-dev/v1/mahasiswa/setoran/{nim}")
    suspend fun getSetoranMahasiswa(
        @Path("nim") nim: String,
        @Query("apikey") apiKey: String,
        @Header("Authorization") authToken: String
    ): Response<ApiResponse<SetoranMahasiswaData>> // Response dengan data SetoranMahasiswaData

    // Endpoint untuk mendapatkan daftar mahasiswa PA (GET, untuk role dosen)
    // Anda mungkin perlu membuat data class DosenPAData jika response ini mengembalikan data struktural
    @GET("/setoran-dev/v1/dosen/pa-saya")
    suspend fun getMahasiswaPA(
        @Query("apikey") apiKey: String,
        @Header("Authorization") authToken: String
    ): Response<ApiResponse<Any>> // Gunakan Any jika struktur datanya belum jelas atau tidak digunakan

    // --- Endpoint Autentikasi Keycloak (KC_URL) ---

    // Endpoint untuk login (POST dengan FormUrlEncoded)
    @FormUrlEncoded // Gunakan ini karena body request adalah form-urlencoded
    @POST("realms/dev/protocol/openid-connect/token") // Perhatikan: ini menggunakan BASE_URL_KC, bukan BASE_URL_API
    suspend fun login(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("scope") scope: String
    ): Response<LoginResponse> // Response berupa data class LoginResponse

    // Endpoint untuk logout (POST dengan FormUrlEncoded)
    @FormUrlEncoded
    @POST("realms/dev/protocol/openid-connect/logout")
    suspend fun logout(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("id_token_hint") idTokenHint: String
    ): Response<Void> // Response kosong untuk logout

    // Endpoint untuk refresh token (POST dengan FormUrlEncoded)
    @FormUrlEncoded
    @POST("realms/dev/protocol/openid-connect/token") // Perhatikan: ini menggunakan BASE_URL_KC
    suspend fun refreshToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String
    ): Response<LoginResponse> // Response berupa LoginResponse baru

    // Endpoint untuk mendapatkan info user (GET)
    @GET("realms/dev/protocol/openid-connect/userinfo") // Perhatikan: ini menggunakan BASE_URL_KC
    suspend fun getUserInfo(
        @Header("Authorization") authToken: String // Membutuhkan Bearer Token
    ): Response<UserInfoData> // Response berupa data class UserInfoData

    // --- Endpoint Lainnya (BASE_URL_API) ---

    // Endpoint untuk cek status API (GET)
    @GET("/setoran-dev/v1/") // Perhatikan path root BASE_URL
    suspend fun getStatus(): Response<ApiResponse<Any>> // Sesuaikan jika ada data spesifik di response status

    // Endpoint untuk cek health API (GET)
    @GET("/setoran-dev/v1/health")
    suspend fun getHealth(): Response<ApiResponse<Any>> // Sesuaikan jika ada data spesifik di response health
}