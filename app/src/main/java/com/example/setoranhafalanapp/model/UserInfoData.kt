package com.example.setoranhafalanapp.model

import com.google.gson.annotations.SerializedName

// Data class untuk response dari endpoint GET userinfo Keycloak
data class UserInfoData(
    val preferred_username: String,
    val sub: String,
    @SerializedName("email_verified") val emailVerified: Boolean,
    val name: String,
    @SerializedName("preferred_username") val preferredUsername: String,
    @SerializedName("given_name") val givenName: String,
    @SerializedName("family_name") val familyName: String,
    val email: String
    // ... tambahkan field lain jika ada di response JSON
)