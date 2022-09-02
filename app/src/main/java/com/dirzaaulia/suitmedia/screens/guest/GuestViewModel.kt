package com.dirzaaulia.suitmedia.screens.guest

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dirzaaulia.suitmedia.data.local.DataStore
import com.dirzaaulia.suitmedia.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuestViewModel @Inject constructor(
  repository: Repository,
): ViewModel() {

  val guests = repository.getGuests().cachedIn(viewModelScope)

  private val _guestName: MutableStateFlow<String> = MutableStateFlow("")
  val guestName = _guestName.asStateFlow()

  @MainThread
  fun setGuestName(guestName: String) {
    _guestName.value = guestName
  }
}