package com.arsvechkarev.users.list

import androidx.recyclerview.widget.DiffUtil
import com.arsvechkarev.core.model.users.OtherUser

class UsersDiffCallback : DiffUtil.ItemCallback<OtherUser>() {
  override fun areItemsTheSame(oldItem: OtherUser, newItem: OtherUser): Boolean {
    return oldItem.id == newItem.id
  }
  
  override fun areContentsTheSame(oldItem: OtherUser, newItem: OtherUser): Boolean {
    return oldItem == newItem
  }
  
}