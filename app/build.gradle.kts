plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Hapus atau komentari baris berikut jika ada, karena ini plugin untuk Compose
    // id("org.jetbrains.kotlin.plugin.compose") // <-- Hapus baris ini
    id("kotlin-kapt") // Penting untuk View Binding, tambahkan jika belum ada
}

android {
    // Pastikan namespace sesuai dengan package utama Anda
    // Dari gambar Anda, package utamanya adalah com.example.setoranhafalanapp
    namespace = "com.example.setoranhafalanapp"

    // Compile SDK: Ganti ke 34 untuk kompatibilitas yang lebih luas dengan dependensi umum
    // Jika Anda yakin semua dependensi mendukung 35, bisa tetap 35,
    // tetapi 34 umumnya lebih aman saat ini.
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.setoranhafalanapp"
        // Minimum SDK: Sesuaikan dengan target audiens Anda. 24 adalah pilihan umum.
        minSdk = 26
        targetSdk = 34 // Ganti ke 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8 // Ubah ke 1.8
        targetCompatibility = JavaVersion.VERSION_1_8 // Ubah ke 1.8
    }
    kotlinOptions {
        jvmTarget = "1.8" // Ubah ke 1.8
    }

    // Hapus blok buildFeatures untuk compose
    /*
    buildFeatures {
        compose = true // <-- Hapus blok ini sepenuhnya
    }
    */

    // Tambahkan konfigurasi untuk mengaktifkan View Binding
    buildFeatures {
        viewBinding = true
    }

    // Hapus blok composeOptions jika ada
    /*
    composeOptions {
        kotlinCompilerExtensionVersion = "..." // <-- Hapus blok ini sepenuhnya
    }
    */
}

dependencies {
    // Default dependencies (Pastikan ini merujuk ke versi View-based UI jika pakai libs.versions.toml)
    // Dependensi inti untuk Activity dan UI dasar View-based
    implementation("androidx.core:core-ktx:1.12.0") // Atau gunakan libs.androidx.core.ktx
    implementation("androidx.appcompat:appcompat:1.6.1") // Untuk AppCompatActivity, dll.
    implementation("com.google.android.material:material:1.11.0") // Komponen Material Design (Buttons, EditTexts, dll.)
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Untuk ConstraintLayout (jika digunakan)

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0") // Atau gunakan libs.androidx.lifecycle.runtime.ktx

    // Hapus dependensi Jetpack Compose yang ada di sini
    // implementation(libs.androidx.activity.compose) // <-- Hapus
    // implementation(platform(libs.androidx.compose.bom)) // <-- Hapus
    // implementation(libs.androidx.ui) // <-- Hapus
    // implementation(libs.androidx.ui.graphics) // <-- Hapus
    // implementation(libs.androidx.ui.tooling.preview) // <-- Hapus
    // implementation(libs.androidx.material3) // <-- Hapus jika hanya pakai com.google.android.material

    testImplementation("junit:junit:4.13.2") // Atau gunakan libs.junit
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // Atau gunakan libs.androidx.junit
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // Atau gunakan libs.androidx.espresso.core

    // Hapus dependensi testing Compose
    // androidTestImplementation(platform(libs.androidx.compose.bom)) // <-- Hapus
    // androidTestImplementation(libs.androidx.ui.test.junit4) // <-- Hapus
    // debugImplementation(libs.androidx.ui.tooling) // <-- Hapus
    // debugImplementation(libs.androidx.ui.test.manifest) // <-- Hapus

    // Tambahkan dependencies untuk Retrofit, Gson, dan OkHttp (sama seperti sebelumnya)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Kotlin Coroutines (sama seperti sebelumnya)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // View Binding Library: Tidak perlu dependensi tambahan jika sudah diaktifkan di blok buildFeatures { viewBinding = true }
}