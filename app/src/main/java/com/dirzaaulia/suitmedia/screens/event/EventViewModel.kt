package com.dirzaaulia.suitmedia.screens.event

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import com.dirzaaulia.suitmedia.data.model.Event
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(

): ViewModel() {

  private val _eventName: MutableStateFlow<String> = MutableStateFlow("")
  val eventName = _eventName.asStateFlow()

  private val _eventList: MutableStateFlow<List<Event>> = MutableStateFlow(emptyList())
  val eventList = _eventList.asStateFlow()

  private val _currentSelectedMarker: MutableStateFlow<Marker?> = MutableStateFlow(null)
  val currentSelectedMarker = _currentSelectedMarker.asStateFlow()

  init {
    setEventList()
  }

  @MainThread
  fun setEventName(eventName: String) {
    _eventName.value = eventName
  }

  @MainThread
  fun setCurrentSelectedMarker(marker: Marker) {
    _currentSelectedMarker.value = marker
  }

  private fun setEventList() {
    val item = Event(
      title = "Event 1",
      description = "This is Event 1",
      date = "15 Jan 2021",
      time = "9:00 AM",
      latitude = -6.88045272271226,
      longitude = 107.58426207103643
    )
    val item2 = Event(
      title = "Event 2",
      description = "This is Event 2",
      date = "15 Jan 2021",
      time = "9:00 AM",
      latitude = -6.879131926235311,
      longitude =  107.58833902837321
    )
    val item3 = Event(
      title = "Event 3",
      description = "This is Event 3",
      date = "15 Jan 2021",
      time = "9:00 AM",
      latitude = -6.878535436299701,
      longitude =  107.58426207103643
    )
    val item4 = Event(
      title = "Event 4",
      description = "This is Event 4",
      date = "15 Jan 2021",
      time = "9:00 AM",
      latitude = -6.881858727827953,
      longitude =  107.58027094438043
    )
    val item5 = Event(
      title = "Event 5",
      description = "This is Event 5",
      date = "15 Jan 2021",
      time = "9:00 AM",
      latitude = -6.885889060424427,
      longitude =  107.5812334049729
    )
    val list = ArrayList<Event>().apply {
      add(item)
      add(item2)
      add(item3)
      add(item4)
      add(item5)
    }
    _eventList.value = list
  }
}