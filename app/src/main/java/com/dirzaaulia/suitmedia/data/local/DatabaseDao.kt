package com.dirzaaulia.suitmedia.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dirzaaulia.suitmedia.data.model.Guest

@Dao
interface DatabaseDao {

  @Insert(
    entity = Guest::class,
    onConflict = OnConflictStrategy.REPLACE
  )
  suspend fun insertGuest(guest: Guest)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAllGuests(repos: List<Guest>?)

  @Query("SELECT * FROM guest_table")
  fun getGuests(): PagingSource<Int, Guest>

  @Query("DELETE FROM guest_table")
  suspend fun clearGuests()
}