//package com.dirzaaulia.suitmedia.paging
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.dirzaaulia.footballclips.util.executeWithResponse
//import com.dirzaaulia.footballclips.util.pagingSucceeded
//import com.dirzaaulia.suitmedia.REMOTE_PAGING_STARTING_INDEX
//import com.dirzaaulia.suitmedia.data.local.DatabaseDao
//import com.dirzaaulia.suitmedia.data.model.Guest
//import com.dirzaaulia.suitmedia.data.remote.service.Service
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import retrofit2.HttpException
//
//class GuestPagingSource(
//  private val service: Service,
//  private val databaseDao: DatabaseDao,
//): PagingSource<Int, Guest>() {
//
//  override fun getRefreshKey(state: PagingState<Int, Guest>): Int? {
//    return state.anchorPosition?.let { anchorPosition ->
//      state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
//        ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
//    }
//  }
//
//  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Guest> {
//    val page = params.key ?: REMOTE_PAGING_STARTING_INDEX
//    var totalPage = 0
//
//    val data = withContext(Dispatchers.IO) {
//      executeWithResponse {
//        val response = service.getGuests(page)
//        response.body()?.let {
//          totalPage = it.totalPage ?: 0
//          it.data?.map { guest ->
//            databaseDao.insertGuest(guest)
//          }
//        } ?: run {
//          throw HttpException(response)
//        }
//      }
//    }
//
//    return data.pagingSucceeded { list ->
//      LoadResult.Page(
//        data = list,
//        prevKey = if (page == 1) null else page - 1,
//        nextKey = if (page < totalPage) page + 1 else null
//      )
//    }
//  }
//}