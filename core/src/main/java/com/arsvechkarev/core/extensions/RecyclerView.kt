package com.arsvechkarev.core.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun <VH : RecyclerView.ViewHolder?> RecyclerView.setChatView(adapter: RecyclerView.Adapter<VH>) {
  val layoutManager = LinearLayoutManager(context)
  layoutManager.stackFromEnd = true
  this.layoutManager = layoutManager
  this.adapter = adapter
}