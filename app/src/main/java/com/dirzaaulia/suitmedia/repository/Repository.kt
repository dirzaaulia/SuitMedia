package com.dirzaaulia.suitmedia.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dirzaaulia.suitmedia.data.local.AppDatabase
import com.dirzaaulia.suitmedia.data.model.Guest
import com.dirzaaulia.suitmedia.data.remote.service.Service
import com.dirzaaulia.suitmedia.paging.GuestRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
  private val service: Service,
  private val appDatabase: AppDatabase
) {

  fun getGuests(): Flow<PagingData<Guest>> {
    val pagingSourceFactory = { appDatabase.databaseDao().getGuests() }

    return Pager(
      config = PagingConfig(pageSize = 10),
      remoteMediator = GuestRemoteMediator(
        service,
        appDatabase
      ),
      pagingSourceFactory = pagingSourceFactory
    ).flow
  }

  suspend fun insertGuestToDatabase(guest: Guest) {
    appDatabase.databaseDao().insertGuest(guest)
  }
}