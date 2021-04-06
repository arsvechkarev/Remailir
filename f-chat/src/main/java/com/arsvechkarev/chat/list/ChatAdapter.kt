package com.arsvechkarev.chat.list

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.arsvechkarev.recycler.BaseListAdapter
import com.arsvechkarev.recycler.listDelegate
import viewdsl.Ints.dp
import viewdsl.background
import viewdsl.paddings
import viewdsl.text
import com.arsvechkarev.views.drawables.MessageDrawable
import com.arsvechkarev.views.drawables.MessageDrawable.MessageType
import com.arsvechkarev.views.drawables.MessageDrawable.MessageType.LEFT
import com.arsvechkarev.views.drawables.MessageDrawable.MessageType.LEFT_CORNER
import com.arsvechkarev.views.drawables.MessageDrawable.MessageType.RIGHT
import com.arsvechkarev.views.drawables.MessageDrawable.MessageType.RIGHT_CORNER
import core.model.messaging.DisplayableMessage
import core.model.messaging.MessageOtherUser
import core.model.messaging.MessageThisUser
import core.resources.Colors
import core.resources.Dimens.MessageStickingDistance
import core.resources.Styles.MessageTextView
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.ViewBuilder
import viewdsl.gravity
import viewdsl.paddingVertical
import viewdsl.withViewBuilder

class ChatAdapter : BaseListAdapter() {
  
  init {
    addDelegates(
      listDelegate<MessageThisUser> {
        view { parent ->
          parent.withViewBuilder {
            createTextView(Gravity.END) {
              paddings(start = 8.dp, end = MessageStickingDistance + 8.dp)
            }
          }
        }
        onBind {
          bindTextView(itemView, adapterPosition, item, Colors.MessageThisUser, RIGHT)
        }
      },
      listDelegate<MessageOtherUser> {
        view { parent ->
          parent.withViewBuilder {
            createTextView(Gravity.START) {
              paddings(start = MessageStickingDistance + 8.dp, end = 8.dp)
            }
          }
        }
        onBind {
          bindTextView(itemView, adapterPosition, item, Colors.MessageOtherUser, LEFT)
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
    require(type == LEFT || type == RIGHT)
    if (position == 0) {
      return if (type == LEFT) LEFT_CORNER else RIGHT_CORNER
    }
    if (type == LEFT && getItem(position - 1) is MessageThisUser) {
      return LEFT_CORNER
    } else if (type == RIGHT && getItem(position - 1) is MessageOtherUser) {
      return RIGHT_CORNER
    }
    return type
  }
}