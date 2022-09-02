package com.dirzaaulia.suitmedia.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dirzaaulia.suitmedia.DATABASE_NAME
import com.dirzaaulia.suitmedia.data.model.Guest
import com.dirzaaulia.suitmedia.data.model.GuestRemoteKeys

@Database(
  entities = [Guest::class, GuestRemoteKeys::class],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {

  abstract fun databaseDao(): DatabaseDao
  abstract fun remoteKeysDao(): GuestRemoteKeyDao

  companion object {
    @Volatile
    private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
      return instance ?: synchronized(this) {
        instance ?: buildDatabase(context).also { instance = it }
      }
    }

    private fun buildDatabase(context: Context): AppDatabase {
      return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DATABASE_NAME
      )
        .fallbackToDestructiveMigration()
        .build()
    }
  }
}