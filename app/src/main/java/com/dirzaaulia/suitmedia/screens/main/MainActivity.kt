package com.dirzaaulia.suitmedia.screens.main

import android.Manifest
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.dirzaaulia.suitmedia.*
import com.dirzaaulia.suitmedia.databinding.ActivityMainBinding
import com.dirzaaulia.suitmedia.screens.onboarding.OnBoardingActivity
import com.dirzaaulia.suitmedia.util.checkSize
import com.dirzaaulia.suitmedia.util.isPalindrome
import com.dirzaaulia.suitmedia.util.showSnackbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  private val viewModel: MainViewModel by viewModels()

  private var uri: Uri? = null

  private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture())
  { isSaved ->
    if (isSaved) {
      val fileSize = uri
        ?.checkSize(this)

      if (fileSize != 0L) {
        binding.viewWelcome.avatar.load(uri)
      } else {
        binding.root.showSnackbar(getString(R.string.take_picture_error))
        return@registerForActivityResult
      }
    }
  }

  private val selectFiles =
    registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
      uri?.let {
        val fileSize = it.checkSize(this)

        if (fileSize != 0L) {
          binding.viewWelcome.avatar.load(it)
        } else {
          binding.root.showSnackbar(getString(R.string.select_files_error))
          return@registerForActivityResult
        }
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setup()
  }

  private fun setup() {
    setupOnClickListeners()
    setupTextWatcher()
    subscribeForm()
  }

  private fun setupOnClickListeners() {
    binding.viewWelcome.apply {
      avatar.setOnClickListener {
        showAvatarOptionsDialog()
      }

      buttonCheck.setOnClickListener {
        palindromeCheck()
      }
    }
  }

  private fun setupTextWatcher() {
    binding.viewWelcome.apply {
      editTextName.doAfterTextChanged {
        viewModel.setIsNameFilled(it.toString().isNotBlank())
      }

      editTextPalindrome.doAfterTextChanged {
        viewModel.setIsPalindromeFilled(it.toString().isNotBlank())
      }
    }
  }

  private fun subscribeForm() {
    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.isFormFilled.collectLatest { isFormFilled ->
          binding.viewWelcome.buttonNext.setOnClickListener {
            if (isFormFilled) {
              setNameToDataStore()
            } else {
              binding.root.showSnackbar(getString(R.string.welcome_form_not_filled_error))
            }
          }
        }
      }
    }
  }

  private fun palindromeCheck() {
    val text = binding.viewWelcome.editTextPalindrome.text.toString()
    if (text.isNotBlank()) {
      val isPalindrome = text.isPalindrome()

      if (isPalindrome) {
        binding.root.showSnackbar(getString(R.string.palindrome_message))
      } else {
        binding.root.showSnackbar(getString(R.string.not_palindrome_message))
      }
    } else {
      binding.root.showSnackbar(getString(R.string.palindrome_empty))
    }
  }

  private fun setNameToDataStore() {
    val name = binding.viewWelcome.editTextName.text.toString()
    viewModel.setNameToDataStore(name).invokeOnCompletion {
      navigateToOnBoarding()
    }
  }

  private fun showAvatarOptionsDialog() {
    MaterialAlertDialogBuilder(this)
      .setTitle(getString(R.string.avatar))
      .setMessage(getString(R.string.avatar_dialog_chooser_description))
      .setNeutralButton(getString(R.string.cancel)) { dialog, _ ->
        dialog.dismiss()
      }
      .setNegativeButton(getString(R.string.camera)) { dialog, _ ->
        dialog.dismiss()
        initCameraPermission()
      }
      .setPositiveButton(getString(R.string.storage)) { dialog, _ ->
        dialog.dismiss()
        initReadStoragePermission()
      }
      .show()
  }

  private fun initCameraPermission() {
    PermissionX.init(this)
      .permissions(Manifest.permission.CAMERA)
      .explainReasonBeforeRequest()
      .onExplainRequestReason { scope, deniedList ->
        scope.showRequestReasonDialog(
          deniedList,
          getString(R.string.camera_permission_explain_request),
          getString(R.string.ok),
          getString(R.string.cancel)
        )
      }
      .onForwardToSettings{ scope, deniedList ->
        scope.showForwardToSettingsDialog(
          deniedList,
          getString(R.string.camera_permission_explain_setting),
          getString(R.string.ok),
          getString(R.string.cancel)
        )
      }
      .request { allGranted, _, _ ->
        if (allGranted) {
          openCamera()
        } else {
          binding.root.showSnackbar(getString(R.string.camera_permission_denied))
        }
      }
  }

  private fun initReadStoragePermission() {
    PermissionX.init(this)
      .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
      .explainReasonBeforeRequest()
      .onExplainRequestReason { scope, deniedList ->
        scope.showRequestReasonDialog(
          deniedList,
          getString(R.string.storage_permission_explain_request),
          getString(R.string.ok),
          getString(R.string.cancel)
        )
      }
      .onForwardToSettings{ scope, deniedList ->
        scope.showForwardToSettingsDialog(
          deniedList,
          getString(R.string.storage_permission_explain_setting),
          getString(R.string.ok),
          getString(R.string.cancel)
        )
      }
      .request { allGranted, _, _ ->
        if (allGranted) {
          openStorage()
        } else {
          binding.root.showSnackbar(getString(R.string.storage_permission_denied))
        }
      }
  }

  private fun openCamera() {
    val photoFile = File.createTempFile(
      "IMG_",
      ".jpg",
      getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    )

    uri = FileProvider.getUriForFile(
      this,
      BuildConfig.APPLICATION_ID + ".provider",
      photoFile
    )

    takePicture.launch(uri)
  }

  private fun openStorage() {
    selectFiles.launch("image/*")
  }

  private fun navigateToOnBoarding() {
    val intent = OnBoardingActivity.newIntent(this)
    startActivity(intent)
  }
}