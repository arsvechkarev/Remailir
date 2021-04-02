package recycler

/**
 * Item for recycler view that can be compared to another item
 */
interface DifferentiableItem : DisplayableItem {
  
  /**
   * Id to distinguish two different elements
   */
  val id: String
  
  /**
   * Every class that inherits from [DifferentiableItem] should override equals so that elements
   * could be compared properly
   */
  override fun equals(other: Any?): Boolean
}