package com.arsvechkarev.core.model

import com.arsvechkarev.core.recycler.DifferentiableItem

data class User(val username: String) : DifferentiableItem {
  
  override val id = username
}