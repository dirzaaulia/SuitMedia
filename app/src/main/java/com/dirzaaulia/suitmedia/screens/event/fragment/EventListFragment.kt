package com.dirzaaulia.suitmedia.screens.event.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dirzaaulia.suitmedia.R
import com.dirzaaulia.suitmedia.data.model.Event
import com.dirzaaulia.suitmedia.data.model.Guest
import com.dirzaaulia.suitmedia.databinding.FragmentEventListBinding
import com.dirzaaulia.suitmedia.screens.event.EventViewModel
import com.dirzaaulia.suitmedia.screens.event.adapter.EventListAdapter
import com.dirzaaulia.suitmedia.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventListFragment : Fragment() {

  private lateinit var binding: FragmentEventListBinding

  private val viewModel: EventViewModel by activityViewModels()

  private val adapter = EventListAdapter(this::onEventItemClicked)

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    binding = FragmentEventListBinding.inflate(layoutInflater)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupAdapter()
    subscribeEventList()
  }

  private fun setupAdapter() {
    binding.recyclerView.adapter = adapter
  }

  private fun subscribeEventList() {
    lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.eventList.collect {
          adapter.submitList(it)
        }
      }
    }
  }

  private fun onEventItemClicked(event: Event) {
    viewModel.setEventName(event.title)
    binding.root.showSnackbar(getString(R.string.event_format, event.title))
  }
}