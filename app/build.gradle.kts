
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.uniminuto.recordatorio"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.uniminuto.recordatorio"
        minSdk = 24
        targetSdk = 36
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
        languageVersion = "1.9"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    try {
        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.material3)
        kapt(libs.androidx.room.compiler)
        implementation(libs.androidx.room.ktx)

        implementation(libs.androidx.navigation.compose)
        implementation(libs.androidx.lifecycle.viewmodel.compose)
        implementation(libs.androidx.lifecycle.runtime.compose)

        implementation(libs.androidx.compose.material.icons.extended)

        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.activity.compose)
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.compose.ui)
        implementation(libs.androidx.compose.ui.graphics)
        implementation(libs.androidx.compose.ui.tooling.preview)
        implementation(libs.androidx.compose.material3)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.compose.ui.test.junit4)
        debugImplementation(libs.androidx.compose.ui.tooling)
        debugImplementation(libs.androidx.compose.ui.test.manifest)

        coreLibraryDesugaring(libs.desugar.jdk.libs)

        implementation("com.google.maps.android:maps-compose:4.3.0")

        // Google Location Services (Fused Location Provider)
        implementation("com.google.android.gms:play-services-location:21.0.1")

        // Geocodificación (para convertir coordenadas a dirección legible)
        implementation("com.google.android.gms:play-services-maps:18.2.0")
        implementation("com.google.maps.android:android-maps-utils:3.0.0")

        implementation(platform("com.google.firebase:firebase-bom:34.4.0"))
        implementation("com.google.firebase:firebase-analytics")

        implementation("androidx.compose.runtime:runtime-livedata:1.6.8")
    } catch (e: Exception) {
        TODO("Not yet implemented")
    }
}