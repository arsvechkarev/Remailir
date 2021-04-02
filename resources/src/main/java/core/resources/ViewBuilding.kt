package core.resources

import android.app.Activity
import viewdsl.Densities

object ViewBuilding {
  
  fun initializeWithActivityContext(activity: Activity) {
    Colors.init(activity)
    TextSizes.init(activity)
    Fonts.init(activity)
    Densities.init(activity.resources)
  }
}