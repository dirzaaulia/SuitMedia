package com.dirzaaulia.suitmedia.screens.event

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.dirzaaulia.suitmedia.R
import com.dirzaaulia.suitmedia.databinding.ActivityEventBinding
import com.dirzaaulia.suitmedia.screens.event.fragment.EventListFragmentDirections
import com.dirzaaulia.suitmedia.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventActivity : AppCompatActivity() {

  private lateinit var binding: ActivityEventBinding

  private val viewModel: EventViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityEventBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setup()
  }

  override fun onBackPressed() {
    val fragmentId = this.findNavController(R.id.nav_host_fragment).currentDestination?.id

    when (fragmentId) {
      R.id.eventListFragment -> {
        val eventName = viewModel.eventName.value
        if (eventName.isNotBlank()) {
          val intent = Intent().apply {
            putExtra("eventName", eventName)
          }
          setResult(Activity.RESULT_OK, intent)
          finish()
        } else {
          finish()
        }
      }
      else -> super.onBackPressed()
    }
  }

  private fun setup() {
    setupToolbar()
  }

  private fun setupToolbar() {
    binding.toolbar.root.apply {
      title = context.getString(R.string.events)
      inflateMenu(R.menu.event_menu)
      setNavigationOnClickListener {
        onBackPressed()
      }
      setOnMenuItemClickListener {
        when (it.itemId) {
          R.id.menu_map -> {
            val direction = EventListFragmentDirections.actionEventListFragmentToEventMapFragment()
            this@EventActivity.findNavController(R.id.nav_host_fragment).navigate(direction)
            menu[1].isVisible = false
            menu[2].isVisible = true
            return@setOnMenuItemClickListener true
          }
          R.id.menu_list -> {
            this@EventActivity.findNavController(R.id.nav_host_fragment).popBackStack()
            menu[1].isVisible = true
            menu[2].isVisible = false
            return@setOnMenuItemClickListener true
          }
          else -> return@setOnMenuItemClickListener  false
        }
      }
    }
  }
}