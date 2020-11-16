package com.arsvechkarev.core.viewbuilding

import android.content.Context
import com.arsvechkarev.core.R
import com.arsvechkarev.viewdsl.getAttrColor

object Colors {
  
  private val colors = IntArray(23) { 0 }
  
  val Background get() = colors[0]
  val Surface get() = colors[1]
  val Dialog get() = colors[2]
  val Icon get() = colors[3]
  val Ripple get() = colors[4]
  val Accent get() = colors[5]
  val AccentLight get() = colors[6]
  val OnAccent get() = colors[7]
  val Success get() = colors[8]
  val Error get() = colors[9]
  val ErrorRipple get() = colors[10]
  val TextPrimary get() = colors[11]
  val TextSecondary get() = colors[12]
  val UserIconPrimary get() = colors[13]
  val UserIconSecondary get() = colors[14]
  val MessageThisUser get() = colors[15]
  val MessageOtherUser get() = colors[16]
  val ErrorGradientStart get() = colors[17]
  val ErrorGradientEnd get() = colors[18]
  val Disabled get() = colors[19]
  val Border get() = colors[20]
  val Share get() = colors[21]
  val SourceCode get() = colors[22]
  
  fun init(context: Context) {
    colors[0] = context.getAttrColor(R.attr.colorBackground)
    colors[1] = context.getAttrColor(R.attr.colorSurface)
    colors[2] = context.getAttrColor(R.attr.colorDialog)
    colors[3] = context.getAttrColor(R.attr.colorIcon)
    colors[4] = context.getAttrColor(R.attr.colorRipple)
    colors[5] = context.getAttrColor(R.attr.colorAccent)
    colors[6] = context.getAttrColor(R.attr.colorAccentLight)
    colors[7] = context.getAttrColor(R.attr.colorOnAccent)
    colors[8] = context.getAttrColor(R.attr.colorSuccess)
    colors[9] = context.getAttrColor(R.attr.colorError)
    colors[10] = context.getAttrColor(R.attr.colorErrorRipple)
    colors[11] = context.getAttrColor(R.attr.colorTextPrimary)
    colors[12] = context.getAttrColor(R.attr.colorTextSecondary)
    colors[13] = context.getAttrColor(R.attr.colorUserIconPrimary)
    colors[14] = context.getAttrColor(R.attr.colorUserIconSecondary)
    colors[15] = context.getAttrColor(R.attr.colorMessageThisUser)
    colors[16] = context.getAttrColor(R.attr.colorMessageOtherUser)
    colors[17] = context.getAttrColor(R.attr.colorErrorGradientStart)
    colors[18] = context.getAttrColor(R.attr.colorErrorGradientEnd)
    colors[19] = context.getAttrColor(R.attr.colorDisabled)
    colors[20] = context.getAttrColor(R.attr.colorBorder)
    colors[21] = context.getAttrColor(R.attr.colorShare)
    colors[22] = context.getAttrColor(R.attr.colorSourceCode)
  }
}