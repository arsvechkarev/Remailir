package com.arsvechkarev.core.recycler

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.arsvechkarev.core.recycler.DisplayableItem.DiffCallBack

abstract class BaseListAdapter : ListAdapter<DisplayableItem, ViewHolder>(DiffCallBack()) {
  
  var data: MutableList<DisplayableItem> = ArrayList()
  val delegates = SparseArrayCompat<AdapterDelegate>()
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return delegates[viewType]!!.onCreateViewHolder(parent)
  }
  
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    delegates[getItemViewType(position)]!!.onBindViewHolder(holder, data[position])
  }
  
  override fun getItemViewType(position: Int): Int {
    return data[position].type
  }
  
  override fun submitList(list: MutableList<DisplayableItem>?) {
    data = list ?: ArrayList()
    super.submitList(list)
  }
}