package com.arsvechkarev.chat.list.delegates

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.chat.R
import core.model.messaging.MessageOtherUser
import core.recycler.AdapterDelegate
import core.recycler.DisplayableItem
import core.strings.FORMAT_MSG_TIME
import core.util.inflate
import kotlinx.android.synthetic.main.item_message_other_user.view.textMessage
import kotlinx.android.synthetic.main.item_message_other_user.view.textTime
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter


class MessageOtherUserAdapterDelegate : AdapterDelegate {
  
  override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
    return MessageOtherUserViewHolder(parent.inflate(R.layout.item_message_other_user))
  }
  
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DisplayableItem) {
    val messageOtherUserViewHolder = holder as MessageOtherUserViewHolder
    val messageOtherUser = item as MessageOtherUser
    messageOtherUserViewHolder.bind(messageOtherUser)
  }
  
  class MessageOtherUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    fun bind(messageOtherUser: MessageOtherUser) {
      itemView.textMessage.text = messageOtherUser.text
      val time =
        LocalDateTime.ofEpochSecond(messageOtherUser.timestamp, 0, ZonedDateTime.now().offset)
      val format = time.format(DateTimeFormatter.ofPattern(FORMAT_MSG_TIME))
      itemView.textTime.text = format
    }
    
  }
}