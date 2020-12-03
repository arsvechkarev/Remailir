package com.arsvechkarev.chat.list

import android.view.Gravity
import android.widget.TextView
import com.arsvechkarev.core.model.MessageOtherUser
import com.arsvechkarev.core.model.MessageThisUser
import com.arsvechkarev.core.recycler.ListAdapter
import com.arsvechkarev.core.recycler.delegate
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.viewdsl.gravity
import com.arsvechkarev.viewdsl.padding
import com.arsvechkarev.viewdsl.size
import com.arsvechkarev.viewdsl.text
import com.arsvechkarev.viewdsl.textColor
import com.arsvechkarev.viewdsl.textSize

class ChatAdapter : ListAdapter(
  delegate<MessageThisUser> {
    buildView {
      TextView(context).apply {
        size(MatchParent, WrapContent)
        gravity(Gravity.END)
        padding(12.dp)
        textSize(TextSizes.H2)
        textColor(Colors.TextPrimary)
      }
    }
    onBind {
      (itemView as TextView).text(item.text)
    }
  },
  delegate<MessageOtherUser> {
    buildView {
      TextView(context).apply {
        size(MatchParent, WrapContent)
        gravity(Gravity.START)
        padding(12.dp)
        textSize(TextSizes.H2)
        textColor(Colors.TextPrimary)
      }
    }
    onBind {
      (itemView as TextView).text(item.text)
    }
  }
)