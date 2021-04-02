package com.arsvechkarev.friends.presentation

import android.widget.TextView
import com.arsvechkarev.recycler.CustomListAdapter
import com.arsvechkarev.recycler.listDelegate
import com.arsvechkarev.views.UserItemView
import core.model.User
import viewdsl.childViewAs
import viewdsl.onClick
import viewdsl.text
import viewdsl.withViewBuilder

open class CommonFriendsAdapter(
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
}