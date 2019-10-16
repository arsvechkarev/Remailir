package com.arsvechkarev.friends.list

import androidx.recyclerview.widget.DiffUtil
import com.arsvechkarev.core.model.Friend

class FriendsDiffCallback : DiffUtil.ItemCallback<Friend>() {
  override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
    return oldItem.id == newItem.id
  }
  
  override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
    return oldItem == newItem
  }
  
}