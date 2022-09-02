package com.dirzaaulia.suitmedia.screens.onboarding

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirzaaulia.suitmedia.data.local.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
  private val dataStore: DataStore
): ViewModel() {

  private val _name: MutableStateFlow<String> = MutableStateFlow("")
  val name = _name.asStateFlow()

  private val _guestName: MutableStateFlow<String> = MutableStateFlow("")
  val guestName = _guestName.asStateFlow()

  private val _eventName: MutableStateFlow<String> = MutableStateFlow("")
  val eventName = _eventName.asStateFlow()

  init {
    getNameFromDataStore()
  }

  @MainThread
  fun setGuestName(guestName: String) {
    _guestName.value = guestName
  }

  @MainThread
  fun setEventName(eventName: String) {
    _eventName.value = eventName
  }

  private fun getNameFromDataStore() {
    dataStore.nameFlow.onEach {
      _name.value = it
    }
      .launchIn(viewModelScope)
  }
}