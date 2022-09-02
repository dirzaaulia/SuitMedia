package com.dirzaaulia.suitmedia.screens.guest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.dirzaaulia.suitmedia.R
import com.dirzaaulia.suitmedia.data.model.Guest
import com.dirzaaulia.suitmedia.databinding.ActivityGuestBinding
import com.dirzaaulia.suitmedia.screens.guest.adapter.GuestAdapter
import com.dirzaaulia.suitmedia.screens.guest.adapter.GuestLoadStateAdapter
import com.dirzaaulia.suitmedia.util.getErrorHttpMessage
import com.dirzaaulia.suitmedia.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GuestActivity : AppCompatActivity() {

  private lateinit var binding: ActivityGuestBinding

  private val viewModel: GuestViewModel by viewModels()

  private val adapter = GuestAdapter(this::onGuestItemClicked)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityGuestBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setup()
  }

  override fun onBackPressed() {
    if (viewModel.guestName.value.isNotBlank()) {
      val intent = Intent().apply {
        putExtra("guestName", viewModel.guestName.value)
      }
      setResult(Activity.RESULT_OK, intent)
      finish()
    } else {
      finish()
    }
  }

  private fun setup() {
    setupToolbar()
    setupSwipeRefresh()
    setupAdapter()
    subscribeGuests()
  }

  private fun setupToolbar() {
    binding.toolbar.root.apply {
      title = context.getString(R.string.guests)
      setNavigationOnClickListener {
        onBackPressed()
      }
    }
  }

  private fun setupSwipeRefresh() {
    binding.swipeRefresh.setOnRefreshListener {
      adapter.refresh()
    }
  }

  private fun setupAdapter() {
    binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
      GuestLoadStateAdapter { adapter.retry() },
      GuestLoadStateAdapter { adapter.retry() }
    )

    adapter.addLoadStateListener { loadStates ->
      setViewState(loadStates)
      when (loadStates.source.refresh) {
        is LoadState.Loading -> {
          binding.swipeRefresh.isRefreshing = false
        }
        is LoadState.Error -> {
          val throwable =(loadStates.source.refresh as LoadState.Error).error

          binding.root.showSnackbar(throwable.getErrorHttpMessage(this))
        }
        else -> {}
      }
    }
  }

  private fun setViewState(loadStates: CombinedLoadStates) {
    binding.apply {
      progressBar.isVisible = loadStates.source.refresh is LoadState.Loading
      recyclerView.isVisible = loadStates.source.refresh is LoadState.NotLoading
    }
  }

  private fun subscribeGuests() {
    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.guests.collectLatest {
          adapter.submitData(it)
        }
      }
    }
  }

  private fun onGuestItemClicked(guest: Guest) {
    val name = getString(R.string.name_format, guest.firstName, guest.lastName)
    viewModel.setGuestName(name)
    binding.root.showSnackbar(getString(R.string.guest_format, name))
  }
}