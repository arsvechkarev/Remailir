package com.arsvechkarev.core.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.core.recycler.DisplayableItem

interface AdapterDelegate {
  
  fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
  
  fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DisplayableItem)
}