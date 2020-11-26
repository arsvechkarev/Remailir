package com.arsvechkarev.friends.list

import android.text.TextUtils
import android.view.Gravity
import android.widget.TextView
import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.core.model.Text
import com.arsvechkarev.core.model.User
import com.arsvechkarev.core.recycler.DifferentiableItem
import com.arsvechkarev.core.recycler.ListAdapter
import com.arsvechkarev.core.recycler.delegate
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.Dimens.UserIconSize
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.viewdsl.Size.IntSize
import com.arsvechkarev.viewdsl.background
import com.arsvechkarev.viewdsl.childViewAs
import com.arsvechkarev.viewdsl.classNameTag
import com.arsvechkarev.viewdsl.gravity
import com.arsvechkarev.viewdsl.margins
import com.arsvechkarev.viewdsl.onClick
import com.arsvechkarev.viewdsl.paddings
import com.arsvechkarev.viewdsl.rippleBackground
import com.arsvechkarev.viewdsl.size
import com.arsvechkarev.viewdsl.text
import com.arsvechkarev.viewdsl.textSize
import com.arsvechkarev.views.drawables.ProfileDrawable

class FriendsAdapter(
  private val onClickListener: (User) -> Unit
) : ListAdapter(
  delegate<Text> {
    buildView {
      TextView(context).apply {
        size(WrapContent, WrapContent)
        margins(start = 16.dp, top = 16.dp, bottom = 8.dp)
      }
    }
    onBind {
      (itemView as TextView).text(item.text)
    }
  },
  delegate<User> {
    buildView {
      RootHorizontalLayout(MatchParent, WrapContent) {
        paddings(start = 20.dp, top = 12.dp, end = 16.dp, bottom = 12.dp)
        rippleBackground(Colors.Ripple)
        gravity(Gravity.CENTER_VERTICAL)
        View(IntSize(UserIconSize), IntSize(UserIconSize)) {
          background(ProfileDrawable(context, inverseColors = true))
          margins(end = 24.dp)
        }
        TextView(WrapContent, WrapContent) {
          classNameTag()
          textSize(TextSizes.H4)
          ellipsize = TextUtils.TruncateAt.END
        }
      }
    }
    onInitViewHolder {
      itemView.onClick { onClickListener(item) }
    }
    onBind {
      itemView.childViewAs<TextView>().text(item.username)
    }
  },
) {
  
  var currentFriendsType: FriendsType = FriendsType.ALL_FRIENDS
}