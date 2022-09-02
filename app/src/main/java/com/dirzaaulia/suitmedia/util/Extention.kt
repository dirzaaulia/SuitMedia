package com.dirzaaulia.suitmedia.util

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.documentfile.provider.DocumentFile
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import com.dirzaaulia.suitmedia.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException

//ImageView & ShapeableImageView
fun ShapeableImageView.loadImage(uri: String) {
  val circularProgressDrawable = CircularProgressDrawable(this.context)
  circularProgressDrawable.strokeWidth = 5f
  circularProgressDrawable.centerRadius = 30f
  circularProgressDrawable.start()

  this.load(uri) {
    crossfade(true)
    placeholder(circularProgressDrawable)
  }
}

//Long
fun Long?.replaceIfNull(replacementValue: Long = 0L): Long {
  if (this == null)
    return replacementValue
  return this
}

//String
fun String?.replaceIfNull(replacementValue: String = ""): String {
  if (this == null)
    return replacementValue
  return this
}

fun String.isPalindrome(): Boolean {
  val reversed = this.reversed()
  return this.equals(reversed, ignoreCase = true)
}

//Throwable
fun Throwable.getErrorHttpMessage(context: Context): String {
  return when (this) {
    is ConnectException,
    is UnknownHostException,
    is SocketException,
    is InterruptedIOException,
    -> context.getString(R.string.connection_error)
    else -> if (this.message == null)
      context.getString(R.string.unknown_error)
    else
      this.message!!
  }
}

//Uri
fun Uri.checkSize(context: Context): Long {
  return DocumentFile.fromSingleUri(context, this)?.length()
    .replaceIfNull(0L)
}

//View
fun View.showSnackbar(message: String) {
  Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}