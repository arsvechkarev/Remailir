package com.arsvechkarev.core.model

import com.arsvechkarev.core.recycler.DifferentiableItem

open class DisplayableMessage(
  override val id: String,
  val text: String
) : DifferentiableItem {
  
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is DisplayableMessage) return false
    
    if (id != other.id) return false
    if (text != other.text) return false
    
    return true
  }
  
  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + text.hashCode()
    return result
  }
}

class MessageThisUser(id: String, text: String) : DisplayableMessage(id, text)

class MessageOtherUser(id: String, text: String) : DisplayableMessage(id, text)