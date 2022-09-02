package com.dirzaaulia.suitmedia.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "guest_remote_keys")
data class GuestRemoteKeys(
    @PrimaryKey val guestId: Int?,
    val prevKey: Int?,
    val nextKey: Int?
)