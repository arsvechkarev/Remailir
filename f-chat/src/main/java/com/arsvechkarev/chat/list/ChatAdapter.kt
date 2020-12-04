package com.arsvechkarev.chat.list

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.arsvechkarev.core.extenstions.assertThat
import com.arsvechkarev.core.model.messaging.DisplayableMessage
import com.arsvechkarev.core.model.messaging.MessageOtherUser
import com.arsvechkarev.core.model.messaging.MessageThisUser
import com.arsvechkarev.core.recycler.ListAdapter
import com.arsvechkarev.core.recycler.delegate
import com.arsvechkarev.core.viewbuilding.Colors.MessageOtherUser
import com.arsvechkarev.core.viewbuilding.Colors.MessageThisUser
import com.arsvechkarev.core.viewbuilding.Dimens.MessageStickingDistance
import com.arsvechkarev.core.viewbuilding.Styles.MessageTextView
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.viewdsl.ViewBuilder
import com.arsvechkarev.viewdsl.background
import com.arsvechkarev.viewdsl.gravity
import com.arsvechkarev.viewdsl.paddingVertical
import com.arsvechkarev.viewdsl.paddings
import com.arsvechkarev.viewdsl.text
import com.arsvechkarev.views.drawables.MessageDrawable
import com.arsvechkarev.views.drawables.MessageDrawable.MessageType
import com.arsvechkarev.views.drawables.MessageDrawable.MessageType.LEFT
import com.arsvechkarev.views.drawables.MessageDrawable.MessageType.LEFT_CORNER
import com.arsvechkarev.views.drawables.MessageDrawable.MessageType.RIGHT
import com.arsvechkarev.views.drawables.MessageDrawable.MessageType.RIGHT_CORNER

class ChatAdapter : ListAdapter() {
  
  init {
    addDelegates(
      delegate<MessageThisUser> {
        buildView {
          createTextView(Gravity.END) {
            paddings(start = 8.dp, end = MessageStickingDistance + 8.dp)
          }
        }
        onBind {
          bindTextView(itemView, adapterPosition, item, MessageThisUser, RIGHT)
        }
      },
      delegate<MessageOtherUser> {
        buildView {
          createTextView(Gravity.START) {
            paddings(start = MessageStickingDistance + 8.dp, end = 8.dp)
          }
        }
        onBind {
          bindTextView(itemView, adapterPosition, item, MessageOtherUser, LEFT)
        }
      }
    )
  }
  
  private fun ViewBuilder.createTextView(gravity: Int, style: TextView.() -> Unit) =
      RootHorizontalLayout(MatchParent, WrapContent) {
        paddings(bottom = 8.dp)
        gravity(gravity)
        TextView(WrapContent, WrapContent) {
          paddingVertical(6.dp)
          apply(MessageTextView)
          apply(style)
        }
      }
  
  private fun bindTextView(
    itemView: View,
    position: Int,
    item: DisplayableMessage,
    color: Int,
    type: MessageType
  ) {
    val resultType = figureOutType(type, position)
    val textView = (itemView as ViewGroup).getChildAt(0) as TextView
    textView.text(item.text)
    textView.background(MessageDrawable(color, resultType))
  }
  
  private fun figureOutType(type: MessageType, position: Int): MessageType {
    assertThat(type == LEFT || type == RIGHT)
    if (position == 0) {
      return if (type == LEFT) LEFT_CORNER else RIGHT_CORNER
    }
    if (type == LEFT && data[position - 1] is MessageThisUser) {
      return LEFT_CORNER
    } else if (type == RIGHT && data[position - 1] is MessageOtherUser) {
      return RIGHT_CORNER
    }
    return type
  }
}