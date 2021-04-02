package core.ui.navigation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import core.ui.navigation.ViewPagerAdapter.ViewPagerScreenViewHolder

open class ViewPagerAdapter(
  private val screens: List<ViewPagerScreen>
) : RecyclerView.Adapter<ViewPagerScreenViewHolder>() {
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerScreenViewHolder {
    val screen = screens[viewType]
    screen.createLayout(parent.context)
    return ViewPagerScreenViewHolder(screen)
  }
  
  override fun onViewRecycled(holder: ViewPagerScreenViewHolder) {
    holder.releaseScreen()
  }
  
  override fun onBindViewHolder(holder: ViewPagerScreenViewHolder, position: Int) {
    require(screens[position] === holder.screen)
    holder.initializeScreen()
  }
  
  override fun getItemCount() = screens.size
  
  override fun getItemViewType(position: Int) = position
  
  class ViewPagerScreenViewHolder(
    val screen: ViewPagerScreen
  ) : RecyclerView.ViewHolder(screen.viewNonNull) {
    
    fun initializeScreen() {
      screen.onCreateDelegate()
      screen.onInit()
    }
    
    fun releaseScreen() {
      screen.onRelease()
      screen.onDestroyDelegate()
      screen._view = null
    }
  }
}