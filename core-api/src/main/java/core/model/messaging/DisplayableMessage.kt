package core.model.messaging

import recycler.DifferentiableItem

/**
 * A message to be displayed in chat's recycler view
 *
 * @param id Unique message id
 * @param text Message text
 */
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

/**
 * Represents a message from this user
 */
class MessageThisUser(id: String, text: String) : DisplayableMessage(id, text)

/**
 * Represents a message from other user
 */
class MessageOtherUser(id: String, text: String) : DisplayableMessage(id, text)