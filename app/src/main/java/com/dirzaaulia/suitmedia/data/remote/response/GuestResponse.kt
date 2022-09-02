package com.dirzaaulia.suitmedia.data.remote.response

import com.dirzaaulia.suitmedia.data.model.Guest
import com.squareup.moshi.Json

data class GuestResponse(
  val page: Int? = null,
  @Json(name = "per_page")
  val perPage: Int? = null,
  val total: Int? = null,
  @Json(name = "total_pages")
  val totalPage: Int? = null,
  val data: List<Guest>? = null
)