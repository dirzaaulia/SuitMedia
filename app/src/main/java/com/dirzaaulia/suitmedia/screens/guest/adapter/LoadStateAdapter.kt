package com.dirzaaulia.suitmedia.screens.guest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dirzaaulia.suitmedia.R
import com.dirzaaulia.suitmedia.databinding.ViewLoadStateBinding
import com.dirzaaulia.suitmedia.util.getErrorHttpMessage

class GuestLoadStateAdapter(
  private val retry: () -> Unit
): LoadStateAdapter<GuestLoadStateAdapter.ViewHolder>() {

  override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
    holder.bind(loadState)
  }

  override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
    return ViewHolder.create(parent, retry)
  }

  class ViewHolder(
    private val binding: ViewLoadStateBinding,
    retry: () -> Unit
  ): RecyclerView.ViewHolder(binding.root) {

    init {
      binding.apply {
        buttonRetry.setOnClickListener { retry.invoke() }
      }
    }

    fun bind(loadState: LoadState) {
      binding.apply {
        if (loadState is LoadState.Error) {
          errorMessage.text = loadState.error.getErrorHttpMessage(root.context)
        }

        progressBar.isVisible = loadState is LoadState.Loading

        errorMessage.isVisible = loadState is LoadState.Error
        buttonRetry.isVisible = loadState is LoadState.Error
      }
    }

    companion object {
      fun create(parent: ViewGroup, retry: () -> Unit): ViewHolder {
        val view = LayoutInflater.from(parent.context)
          .inflate(R.layout.view_load_state, parent, false)
        val binding = ViewLoadStateBinding.bind(view)

        return ViewHolder(binding, retry)
      }
    }
  }
}