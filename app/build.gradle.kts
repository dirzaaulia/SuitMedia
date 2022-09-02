plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id("kotlin-parcelize")
  id("dagger.hilt.android.plugin")
  id("androidx.navigation.safeargs.kotlin")
  id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
  compileSdk = AppConfig.compileSdk

  defaultConfig {
    applicationId = AppConfig.applicationId
    minSdk = AppConfig.minSdk
    targetSdk = AppConfig.targetSdk
    versionCode = AppConfig.versionCode
    versionName = AppConfig.versionName

    testInstrumentationRunner = AppConfig.testInstrumentationRunner
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
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlinOptions {
    jvmTarget = "11"

    freeCompilerArgs =  freeCompilerArgs +
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi" +
            "-opt-in=kotlin.time.ExperimentalTime" +
            "-opt-in=androidx.paging.ExperimentalPagingApi"
  }

  buildFeatures {
    viewBinding = true
  }
}

dependencies {

  //Implementation
  implementation(Dependencies.AndroidX.implementation)
  implementation(Dependencies.Coil.implementation)
  implementation(Dependencies.Coroutines.implementation)
  implementation(Dependencies.Hilt.implementation)
  implementation(Dependencies.GoogleMaps.implementation)
  implementation(Dependencies.Material.implementation)
  implementation(Dependencies.Navigation.implementation)
  implementation(Dependencies.OkHttp.implementation)
  implementation(Dependencies.Other.implementation)
  implementation(Dependencies.Paging3.implementation)
  implementation(Dependencies.Retrofit.implementation)
  implementation(Dependencies.Room.implementation)
  implementation("androidx.appcompat:appcompat:1.5.0")
  implementation("com.google.android.material:material:1.4.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")

  //Kapt
  kapt(Dependencies.Hilt.kapt)
  kapt(Dependencies.Room.kapt)

  //Debug Implementation
  debugImplementation(Dependencies.Chucker.debugImplementation)

  //Release Implementation
  releaseImplementation(Dependencies.Chucker.releaseImplementation)

  //Test Implementation
  testImplementation(Dependencies.JUnit.testImplementation)

  //Android Test Implementation
  androidTestImplementation(Dependencies.AndroidX.Test.androidTestImplementation)
}