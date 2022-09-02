package com.dirzaaulia.suitmedia.screens.event.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dirzaaulia.suitmedia.R
import com.dirzaaulia.suitmedia.databinding.FragmentEventMapBinding
import com.dirzaaulia.suitmedia.screens.event.EventViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventMapFragment : Fragment(), OnMapReadyCallback {

  private lateinit var binding: FragmentEventMapBinding
  private lateinit var mMap: GoogleMap

  private val viewModel: EventViewModel by activityViewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    binding = FragmentEventMapBinding.inflate(layoutInflater)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setup()
  }

  override fun onMapReady(googleMap: GoogleMap) {
    mMap = googleMap

    viewModel.eventList.value.map {
      val item = LatLng(it.latitude, it.longitude)
      mMap.addMarker(
        MarkerOptions()
          .position(item)
          .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_unselected))
          .title(it.title)
      )
    }

    mMap.setOnMarkerClickListener { marker ->
      if (viewModel.currentSelectedMarker.value != null) {
        binding.cardInfo.root.isVisible = false
        viewModel.currentSelectedMarker.value!!.setIcon(
          BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_unselected)
        )
      }

      viewModel.setCurrentSelectedMarker(marker)
      marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_selected))

      val selectedEvent = viewModel.eventList.value.filter { event ->
        marker.title == event.title
      }

      if (selectedEvent.isNotEmpty()) {
        val currentEvent = selectedEvent[0]
        binding.cardInfo.apply {
          root.isVisible = true
          title.text = currentEvent.title
          description.text = currentEvent.description
          date.text = currentEvent.date
          time.text = currentEvent.time
        }
      }

      return@setOnMarkerClickListener true
    }

    val firstEvent = viewModel.eventList.value[0]
    val firstEventLocation = LatLng(firstEvent.latitude, firstEvent.longitude)

    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstEventLocation, 15f))
  }

  private fun setup() {
    setupMap()
  }

  private fun setupMap() {
    val mapFragment = childFragmentManager
      .findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)
  }
}