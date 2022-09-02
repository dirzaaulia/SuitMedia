package com.dirzaaulia.suitmedia.screens.guest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dirzaaulia.suitmedia.R
import com.dirzaaulia.suitmedia.data.model.Guest
import com.dirzaaulia.suitmedia.databinding.ItemGuestBinding
import com.dirzaaulia.suitmedia.util.loadImage

typealias OnGuestAdapterClickListener = (Guest) -> Unit

class GuestAdapter(
  private val onGuestClickedListener: OnGuestAdapterClickListener
) : PagingDataAdapter<Guest, GuestAdapter.ViewHolder>(GuestDiffCallback()) {

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = getItem(position)
    item?.let { holder.bind(item) }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      ItemGuestBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
      )
    )
  }

  inner class ViewHolder(
    private val binding: ItemGuestBinding
  ): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Guest) {
      binding.apply {
        root.setOnClickListener {
          onGuestClickedListener(item)
        }
        avatar.loadImage(item.avatar.toString())
        name.text = root.context.getString(R.string.name_format, item.firstName, item.lastName)
      }
    }
  }
}

private class GuestDiffCallback: DiffUtil.ItemCallback<Guest>() {
  override fun areItemsTheSame(oldItem: Guest, newItem: Guest): Boolean {
    return oldItem.id == newItem.id
  }

  override fun areContentsTheSame(oldItem: Guest, newItem: Guest): Boolean {
    return oldItem == newItem
  }
}