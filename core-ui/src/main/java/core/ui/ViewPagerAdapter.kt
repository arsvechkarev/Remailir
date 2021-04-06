package core.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import moxy.MvpDelegate

open class ViewPagerAdapter(
  private val parentMvpDelegate: MvpDelegate<*>,
  private val screens: List<ViewPagerScreen>
) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerScreenViewHolder>() {
  
  fun releaseScreens() {
    screens.forEach { screen ->
      screen.onDetachDelegate()
      screen.onDestroyDelegate()
      screen.onRelease()
      screen._view = null
    }
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerScreenViewHolder {
    val screen = screens[viewType]
    screen.createLayout(parent.context)
    screen.onCreateDelegate(parentMvpDelegate)
    screen.onInit()
    return ViewPagerScreenViewHolder(screen)
  }
  
  override fun onBindViewHolder(holder: ViewPagerScreenViewHolder, position: Int) {
    require(screens[position] === holder.screen)
    holder.screen.onAttachDelegate()
  }
  
  override fun getItemCount() = screens.size
  override fun getItemViewType(position: Int) = position
  
  class ViewPagerScreenViewHolder(
    val screen: ViewPagerScreen
  ) : RecyclerView.ViewHolder(screen.viewNonNull)
}