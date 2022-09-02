import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
  object AndroidX {
    private const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Version.constraintLayout}"
    private const val coreKtx = "androidx.core:core-ktx:${Version.coreKtx}"
    private const val dataStore = "androidx.datastore:datastore-preferences:${Version.dataStore}"
    private const val fragmentKtx = "androidx.fragment:fragment-ktx:${Version.fragmentKtx}"
    private const val swipeRefreshLayout =
      "androidx.swiperefreshlayout:swiperefreshlayout:${Version.swipeRefreshLayout}"

    val implementation = arrayListOf<String>().apply {
      add(constraintLayout)
      add(coreKtx)
      add(dataStore)
      add(fragmentKtx)
      add(swipeRefreshLayout)
    }

    object Test {
      private const val core = "androidx.test:core:${Version.jUnit}"
      private const val rules = "androidx.test:rules:${Version.jUnit}"
      private const val espressoCore = "androidx.test.espresso:espresso-core:${Version.espresso}"

      val androidTestImplementation = arrayListOf<String>().apply {
        add(core)
        add(rules)
        add(espressoCore)
      }
    }
  }

  object Chucker {
    private const val library = "com.github.chuckerteam.chucker:library:${Version.chucker}"
    private const val libraryNoOp = "com.github.chuckerteam.chucker:library-no-op:${Version.chucker}"

    val debugImplementation = arrayListOf<String>().apply {
      add(library)
    }

    val releaseImplementation = arrayListOf<String>().apply {
      add(libraryNoOp)
    }
  }

  object Coil {
    private const val coil = "io.coil-kt:coil:${Version.coil}"

    val implementation = arrayListOf<String>().apply {
      add(coil)
    }
  }

  object Coroutines {
    private const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"
    private const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"

    val implementation = arrayListOf<String>().apply {
      add(android)
      add(core)
    }
  }

  object GoogleMaps {
    private const val maps = "com.google.android.gms:play-services-maps:${Version.googleMaps}"

    val implementation = arrayListOf<String>().apply {
      add(maps)
    }
  }

  object Hilt {
    private const val android = "com.google.dagger:hilt-android:${Version.hilt}"
    private const val compiler = "com.google.dagger:hilt-android-compiler:${Version.hilt}"

    val implementation = arrayListOf<String>().apply {
      add(android)
    }

    val kapt = arrayListOf<String>().apply {
      add(compiler)
    }
  }

  object JUnit {
    private const val junit = "junit:junit:${Version.jUnit}"

    val testImplementation = arrayListOf<String>().apply {
      add(junit)
    }
  }

  object Material {
    private const val material = "com.google.android.material:material:${Version.materialDesign}"

    val implementation = arrayListOf<String>().apply {
      add(material)
    }
  }

  object Navigation {
    private const val activity = "androidx.navigation:navigation-ui-ktx:${Version.navigation}"
    private const val fragment = "androidx.navigation:navigation-fragment-ktx:${Version.navigation}"

    val implementation = arrayListOf<String>().apply {
      add(activity)
      add(fragment)
    }
  }

  object OkHttp{
    private const val okhttp = "com.squareup.okhttp3:okhttp:${Version.okHttp}"
    private const val logging = "com.squareup.okhttp3:logging-interceptor:${Version.okHttp}"

    val implementation = arrayListOf<String>().apply {
      add(okhttp)
      add(logging)
    }
  }

  object Other {
    private const val permissionX = "com.guolindev.permissionx:permissionx:1.5.1"

    val implementation = arrayListOf<String>().apply {
      add(permissionX)
    }
  }

  object Paging3 {
    private const val runtime = "androidx.paging:paging-runtime-ktx:${Version.paging3}"

    val implementation = arrayListOf<String>().apply {
      add(runtime)
    }
  }

  object Retrofit {
    private const val retrofit = "com.squareup.retrofit2:retrofit:${Version.retrofit}"
    private const val converterMoshi = "com.squareup.retrofit2:converter-moshi:${Version.retrofit}"
    private const val moshi = "com.squareup.moshi:moshi-kotlin:${Version.moshi}"

    val implementation = arrayListOf<String>().apply {
      add(retrofit)
      add(converterMoshi)
      add(moshi)
    }
  }

  object Room {
    private const val compiler = "androidx.room:room-compiler:${Version.room}"
    private const val ktx = "androidx.room:room-ktx:${Version.room}"
    private const val paging = "androidx.room:room-paging:${Version.room}"
    private const val runtime = "androidx.room:room-runtime:${Version.room}"

    val implementation = arrayListOf<String>().apply {
      add(ktx)
      add(paging)
      add(runtime)
    }

    val kapt = arrayListOf<String>().apply {
      add(compiler)
    }
  }
}

fun DependencyHandler.kapt(list: List<String>) {
  list.forEach { dependency ->
    add("kapt", dependency)
  }
}

fun DependencyHandler.implementation(list: List<String>) {
  list.forEach { dependency ->
    add("implementation", dependency)
  }
}

fun DependencyHandler.testImplementation(list: List<String>) {
  list.forEach { dependency ->
    add("testImplementation", dependency)
  }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
  list.forEach { dependency ->
    add("androidTestImplementation", dependency)
  }
}

fun DependencyHandler.debugImplementation(list: List<String>) {
  list.forEach { dependency ->
    add("debugImplementation", dependency)
  }
}

fun DependencyHandler.releaseImplementation(list: List<String>) {
  list.forEach { dependency ->
    add("releaseImplementation", dependency)
  }
}