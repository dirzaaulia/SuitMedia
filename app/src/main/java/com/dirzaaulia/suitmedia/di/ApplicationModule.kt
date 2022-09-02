package com.dirzaaulia.suitmedia.di

import android.content.Context
import com.dirzaaulia.suitmedia.data.local.AppDatabase
import com.dirzaaulia.suitmedia.data.local.DataStore
import com.dirzaaulia.suitmedia.data.local.DatabaseDao
import com.dirzaaulia.suitmedia.data.remote.service.Service
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

  @Provides
  @Singleton
  fun provideDataStore(@ApplicationContext context: Context): DataStore {
    return DataStore(context)
  }

  @Provides
  @Singleton
  fun provideService(@ApplicationContext context: Context): Service {
    return Service.create(context)
  }

  @Singleton
  @Provides
  fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
    return AppDatabase.getInstance(context)
  }

  @Provides
  fun provideDatabaseDao(appDatabase: AppDatabase): DatabaseDao {
    return appDatabase.databaseDao()
  }
}