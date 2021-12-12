plugins {
    id("com.android.application")
    kotlin("android")
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.0.1")
    implementation("com.github.bumptech.glide:glide:4.11.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
}

android {
    compileSdkVersion(31)
    defaultConfig {
        applicationId = "pl.oczadly.baltic.lsc.android"
        minSdkVersion(28)
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}