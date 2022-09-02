package com.dirzaaulia.suitmedia.screens.event.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dirzaaulia.suitmedia.data.model.Event
import com.dirzaaulia.suitmedia.data.model.Guest
import com.dirzaaulia.suitmedia.databinding.ItemEventBinding

typealias OnEventListAdapterClickListener = (Event) -> Unit

class EventListAdapter(
  private val onEventListAdapterClickListener: OnEventListAdapterClickListener
): ListAdapter<Event, EventListAdapter.ViewHolder>(EventDiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      ItemEventBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
      )
    )
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = getItem(position)
    item?.let { holder.bind(item) }
  }

  inner class ViewHolder(
    private val binding: ItemEventBinding
  ): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Event) {
      binding.apply {
        root.setOnClickListener {
          onEventListAdapterClickListener(item)
        }
        title.text = item.title
        description.text = item.description
        date.text = item.date
        time.text = item.date
      }
    }
  }
}

private class EventDiffCallback: DiffUtil.ItemCallback<Event>() {
  override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
    return oldItem.title == newItem.title
  }

  override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
    return oldItem == newItem
  }

}