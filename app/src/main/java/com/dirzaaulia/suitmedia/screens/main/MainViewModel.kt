package com.dirzaaulia.suitmedia.screens.main

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirzaaulia.suitmedia.data.local.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val dataStore: DataStore
): ViewModel() {

  private val _isNameFilled = MutableStateFlow(false)
  private val _isPalindromeFilled = MutableStateFlow(false)

  val isFormFilled = _isNameFilled.flatMapLatest { isNameFilled ->
    _isPalindromeFilled.flatMapLatest { isPalindromeFilled ->
       flow {
         emit(isNameFilled && isPalindromeFilled)
       }
    }
  }

  @MainThread
  fun setIsNameFilled(value: Boolean) {
    _isNameFilled.value = value
  }

  @MainThread
  fun setIsPalindromeFilled(value: Boolean) {
    _isPalindromeFilled.value = value
  }

  fun setNameToDataStore(name: String): Job {
    return viewModelScope.launch {
      dataStore.setNameToDataStore(name)
    }
  }
}