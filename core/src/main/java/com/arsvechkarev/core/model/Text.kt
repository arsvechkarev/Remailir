package com.arsvechkarev.core.model

import com.arsvechkarev.core.recycler.DifferentiableItem

data class Text(val text: String) : DifferentiableItem {
  
  override val id = text
}