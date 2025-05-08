package com.example.setoranhafalanapp.model

// Data class untuk body request simpan setoran
data class SimpanSetoranRequest(
    val data_setoran: List<SetoranData>,
    val tgl_setoran: String? = null // Optional: Bisa Null/Gausah Dikirim
)