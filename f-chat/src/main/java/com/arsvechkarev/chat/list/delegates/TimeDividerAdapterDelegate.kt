package com.arsvechkarev.chat.list.delegates

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.chat.R
import core.model.messaging.TimeDivider
import core.recycler.AdapterDelegate
import core.recycler.DisplayableItem
import core.util.inflate
import kotlinx.android.synthetic.main.item_time_divider.view.textHeader

class TimeDividerAdapterDelegate : AdapterDelegate {
  
  override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
    return TimeDividerViewHolder(parent.inflate(R.layout.item_time_divider))
  }
  
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DisplayableItem) {
    val timeDividerViewHolder = holder as TimeDividerViewHolder
    val timeDivider = item as TimeDivider
    timeDividerViewHolder.bind(timeDivider)
  }
  
  class TimeDividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    fun bind(timeDivider: TimeDivider) {
      itemView.textHeader.text = timeDivider.header
    }
    
  }
}