package com.arsvechkarev.friends.old

import android.widget.TextView
import com.arsvechkarev.views.UserItemView
import com.arsvechkarev.recycler.CustomListAdapter
import com.arsvechkarev.recycler.listDelegate
import core.model.FriendsType
import core.model.User
import viewdsl.childViewAs
import viewdsl.onClick
import viewdsl.text
import viewdsl.withViewBuilder

class FriendsOldAdapter(
  private val onUserClicked: (User) -> Unit
) : CustomListAdapter() {
  
  init {
    addDelegates(
      listDelegate<User> {
        view { parent ->
          parent.withViewBuilder { UserItemView() }
        }
        onInitViewHolder {
          itemView.onClick { onUserClicked(item) }
        }
        onBind {
          itemView.childViewAs<TextView>().text(item.username)
        }
      }
    )
  }
  
  var currentFriendsType: FriendsType = FriendsType.ALL_FRIENDS
}