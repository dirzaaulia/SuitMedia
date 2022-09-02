package com.dirzaaulia.suitmedia.data.remote.service

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dirzaaulia.suitmedia.REMOTE_BASE_URL
import com.dirzaaulia.suitmedia.data.remote.response.GuestResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface Service {

  @GET("users")
  suspend fun getGuests(
    @Query("page") page: Int,
    @Query("per_page") perPage: Int = 10
  ): Response<GuestResponse>

  companion object {
    fun create(context: Context): Service {
      val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
      val chucker = ChuckerInterceptor.Builder(context)
        .collector(ChuckerCollector(context))
        .maxContentLength(250000L)
        .redactHeaders(emptySet())
        .alwaysReadResponseBody(false)
        .build()

      val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .addInterceptor(chucker)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

      val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

      return Retrofit.Builder()
        .baseUrl(REMOTE_BASE_URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(Service::class.java)
    }
  }
}