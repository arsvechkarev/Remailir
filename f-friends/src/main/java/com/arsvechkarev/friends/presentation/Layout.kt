package com.arsvechkarev.friends.presentation

import android.view.Gravity
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.Dimens
import com.arsvechkarev.core.viewbuilding.Styles
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.friends.R
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.Size
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.viewdsl.ViewBuilder
import com.arsvechkarev.viewdsl.addView
import com.arsvechkarev.viewdsl.backgroundRoundRect
import com.arsvechkarev.viewdsl.classNameTag
import com.arsvechkarev.viewdsl.drawablePadding
import com.arsvechkarev.viewdsl.drawables
import com.arsvechkarev.viewdsl.gravity
import com.arsvechkarev.viewdsl.marginHorizontal
import com.arsvechkarev.viewdsl.margins
import com.arsvechkarev.viewdsl.paddingHorizontal
import com.arsvechkarev.viewdsl.paddingVertical
import com.arsvechkarev.viewdsl.size
import com.arsvechkarev.viewdsl.tag
import com.arsvechkarev.viewdsl.text
import com.arsvechkarev.viewdsl.textColor
import com.arsvechkarev.viewdsl.textSize
import com.arsvechkarev.viewdsl.visible
import com.arsvechkarev.viewdsl.withViewBuilder
import com.arsvechkarev.views.SimpleDialog

fun CoordinatorLayout.FriendsDialog() = withViewBuilder {
  addView {
    SimpleDialog(context).apply {
      size(MatchParent, MatchParent)
      classNameTag()
      FrameLayout(WrapContent, WrapContent) {
        VerticalLayout(WrapContent, WrapContent) {
          paddingVertical(12.dp)
          paddingHorizontal(20.dp)
          marginHorizontal(32.dp)
          gravity(Gravity.CENTER)
          backgroundRoundRect(Dimens.DefaultCornerRadius, Colors.Dialog)
          TextView(WrapContent, WrapContent, style = Styles.BoldTextView) {
            tag(FriendsScreen.TextNameOfOtherUser)
            textSize(TextSizes.H1)
            textColor(Colors.TextSecondary)
            margins(bottom = 12.dp)
          }
          TextView(WrapContent, WrapContent) {
            apply(Styles.ClickableTextView(Colors.Ripple, Colors.Dialog))
            tag(FriendsScreen.TextAcceptRequest)
            margins(top = 12.dp)
            text(R.string.text_accept_request)
            drawables(start = R.drawable.ic_add_friend, color = Colors.TextPrimary)
            drawablePadding(16.dp)
          }
          TextView(WrapContent, WrapContent) {
            apply(Styles.ClickableTextView(Colors.Ripple, Colors.Dialog))
            tag(FriendsScreen.TextDismissOrRemove)
            drawablePadding(16.dp)
            margins(top = 12.dp)
          }
        }
        VerticalLayout(WrapContent, WrapContent) {
          visible()
          paddingVertical(12.dp)
          paddingHorizontal(20.dp)
          marginHorizontal(32.dp)
          gravity(Gravity.CENTER)
          backgroundRoundRect(Dimens.DefaultCornerRadius, Colors.Dialog)
          TextView(WrapContent, WrapContent, style = Styles.BoldTextView) {
            tag(FriendsScreen.TextConfirmation)
            textSize(TextSizes.H1)
            textColor(Colors.TextSecondary)
            margins(bottom = 12.dp)
          }
          TextView(WrapContent, WrapContent) {
            apply(Styles.ClickableTextView(Colors.Ripple, Colors.Dialog))
            tag(FriendsScreen.TextConfirm)
            margins(top = 12.dp)
            text(R.string.text_accept_request)
            drawables(start = R.drawable.ic_add_friend, color = Colors.TextPrimary)
            drawablePadding(16.dp)
          }
          TextView(WrapContent, WrapContent) {
            apply(Styles.ClickableTextView(Colors.Ripple, Colors.Dialog))
            tag(FriendsScreen.TextDismiss)
            drawablePadding(16.dp)
            margins(top = 12.dp)
          }
        }
      }
    }
  }
}