package com.dirzaaulia.suitmedia.screens.onboarding

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.dirzaaulia.suitmedia.R
import com.dirzaaulia.suitmedia.databinding.ActivityOnBoardingBinding
import com.dirzaaulia.suitmedia.screens.event.EventActivity
import com.dirzaaulia.suitmedia.screens.guest.GuestActivity
import com.dirzaaulia.suitmedia.util.replaceIfNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {

  private lateinit var binding: ActivityOnBoardingBinding

  private val viewModel: OnBoardingViewModel by viewModels()

  private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
      val intent = result.data
      val guestName = intent?.getStringExtra("guestName")
        .replaceIfNull(viewModel.guestName.value)
      val eventName = intent?.getStringExtra("eventName")
        .replaceIfNull(viewModel.eventName.value)
      binding.apply {
        buttonEvent.text = eventName
        buttonGuest.text = guestName
      }
    }
  }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityOnBoardingBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setup()
  }

  private fun setup() {
    setupInitialView()
    setupOnClickListeners()
  }

  private fun setupInitialView() {
    binding.name.text = viewModel.name.value

    viewModel.apply {
      setEventName(getString(R.string.choose_event))
      setGuestName(getString(R.string.choose_guest))
    }
  }

  private fun setupOnClickListeners() {
    binding.apply {
      buttonEvent.setOnClickListener {
        val intent = Intent(this@OnBoardingActivity, EventActivity::class.java)
        resultLauncher.launch(intent)
      }

      buttonGuest.setOnClickListener {
        val intent = Intent(this@OnBoardingActivity, GuestActivity::class.java)
        resultLauncher.launch(intent)
      }
    }
  }

  companion object {
    fun newIntent(context: Context): Intent {
      return Intent(context, OnBoardingActivity::class.java)
    }
  }
}