package com.dirzaaulia.suitmedia.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dirzaaulia.suitmedia.data.model.GuestRemoteKeys

@Dao
interface GuestRemoteKeyDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(remoteKey: List<GuestRemoteKeys>?)

  @Query("SELECT * FROM guest_remote_keys WHERE guestId = :guestId")
  suspend fun remoteKeysGuestId(guestId: Int): GuestRemoteKeys?

  @Query("DELETE FROM guest_remote_keys")
  suspend fun clearRemoteKeys()
}