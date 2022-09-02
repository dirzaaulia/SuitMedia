package com.dirzaaulia.suitmedia.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Entity(tableName = "guest_table")
@Parcelize
data class Guest(
  @PrimaryKey
  val id: Int? = null,
  val email: String? = null,
  @Json(name = "first_name")
  val firstName: String? = null,
  @Json(name = "last_name")
  val lastName: String? = null,
  val avatar: String? = null
): Parcelable