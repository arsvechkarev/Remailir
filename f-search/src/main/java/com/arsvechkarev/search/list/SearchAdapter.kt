package com.arsvechkarev.search.list

import android.widget.TextView
import com.arsvechkarev.common.UserItemView
import com.arsvechkarev.core.model.User
import com.arsvechkarev.core.recycler.ListAdapter
import com.arsvechkarev.core.recycler.delegate
import com.arsvechkarev.viewdsl.childViewAs
import com.arsvechkarev.viewdsl.onClick
import com.arsvechkarev.viewdsl.text

class SearchAdapter(
  private val onUserClicked: (User) -> Unit
) : ListAdapter(
  delegate<User> {
    buildView {
      UserItemView()
    }
    onInitViewHolder {
      itemView.onClick { onUserClicked(item) }
    }
    onBind {
      itemView.childViewAs<TextView>().text(item.username)
    }
  }
)