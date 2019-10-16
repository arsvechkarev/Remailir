package com.arsvechkarev.core.recycler

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * Item for displaying in [RecyclerView]
 *
 * @author Arseniy Svechkarev
 */
interface DisplayableItem {
  
  /**
   * Type to bind recycler adapter
   */
  val type: Int
  
  /**
   * Id to distinguish two different elements
   */
  val id: String
  
  /**
   * Every class inherits from [DisplayableItem] should override equals in order to compare elements
   * properly
   */
  override operator fun equals(other: Any?): Boolean
  
  /**
   * Callback for updating items in recycler view
   */
  class DiffCallBack : DiffUtil.ItemCallback<DisplayableItem>() {
    
    override fun areItemsTheSame(oldItem: DisplayableItem, newItem: DisplayableItem) =
      oldItem.id == newItem.id
    
    override fun areContentsTheSame(oldItem: DisplayableItem, newItem: DisplayableItem) =
      oldItem == newItem
  }
}