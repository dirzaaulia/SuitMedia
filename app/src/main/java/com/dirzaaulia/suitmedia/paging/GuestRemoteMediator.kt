package com.dirzaaulia.suitmedia.paging

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dirzaaulia.suitmedia.REMOTE_PAGING_STARTING_INDEX
import com.dirzaaulia.suitmedia.data.local.AppDatabase
import com.dirzaaulia.suitmedia.data.model.Guest
import com.dirzaaulia.suitmedia.data.model.GuestRemoteKeys
import com.dirzaaulia.suitmedia.data.remote.service.Service

class GuestRemoteMediator(
  private val service: Service,
  private val appDatabase: AppDatabase,
): RemoteMediator<Int, Guest>() {

  override suspend fun initialize(): InitializeAction {
    return InitializeAction.LAUNCH_INITIAL_REFRESH
  }

  override suspend fun load(loadType: LoadType, state: PagingState<Int, Guest>): MediatorResult {
    val page = when (loadType) {
      LoadType.REFRESH -> {
        val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
        remoteKeys?.nextKey?.minus(1) ?: REMOTE_PAGING_STARTING_INDEX
      }
      LoadType.PREPEND -> {
        val remoteKeys = getRemoteKeyForFirstItem(state)
        val prevKey = remoteKeys?.prevKey
          ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
        prevKey
      }
      LoadType.APPEND -> {
        val remoteKeys = getRemoteKeyForLastItem(state)
        val nextKey = remoteKeys?.nextKey
          ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
        nextKey
      }
    }

    try {
      val apiResponse = service.getGuests(page)
      val guests = apiResponse.body()?.data
      val totalPage = apiResponse.body()?.totalPage ?: 1

      appDatabase.withTransaction {
        if (loadType == LoadType.REFRESH) {
          appDatabase.remoteKeysDao().clearRemoteKeys()
          appDatabase.databaseDao().clearGuests()
        }

        val prevKey = if (page == REMOTE_PAGING_STARTING_INDEX) null else page - 1
        val nextKey = if (page < totalPage) page + 1 else null
        val keys = guests?.map {
          GuestRemoteKeys(
            guestId = it.id,
            prevKey = prevKey,
            nextKey = nextKey
          )
        }

        appDatabase.remoteKeysDao().insertAll(keys)
        appDatabase.databaseDao().insertAllGuests(guests)
      }

      return MediatorResult.Success(endOfPaginationReached = guests?.isEmpty() == true)
    } catch (throwable: Throwable) {
      return MediatorResult.Error(throwable)
    }
  }

  private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Guest>): GuestRemoteKeys? {
    // Get the last page that was retrieved, that contained items.
    // From that last page, get the last item
    return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
      ?.let { repo ->
        // Get the remote keys of the last item retrieved
        repo.id?.let { appDatabase.remoteKeysDao().remoteKeysGuestId(it) }
      }
  }

  private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Guest>): GuestRemoteKeys? {
    // Get the first page that was retrieved, that contained items.
    // From that first page, get the first item
    return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
      ?.let { repo ->
        // Get the remote keys of the first items retrieved
        repo.id?.let { appDatabase.remoteKeysDao().remoteKeysGuestId(it) }
      }
  }

  private suspend fun getRemoteKeyClosestToCurrentPosition(
    state: PagingState<Int, Guest>
  ): GuestRemoteKeys? {
    // The paging library is trying to load data after the anchor position
    // Get the item closest to the anchor position
    return state.anchorPosition?.let { position ->
      state.closestItemToPosition(position)?.id?.let { guestId ->
        appDatabase.remoteKeysDao().remoteKeysGuestId(guestId)
      }
    }
  }
}