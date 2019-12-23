package com.arsvechkarev.chat.list.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.chat.R
import core.extensions.inflate
import core.model.messaging.MessageThisUser
import core.recycler.AdapterDelegate
import core.recycler.DisplayableItem
import core.strings.FORMAT_MSG_TIME
import kotlinx.android.synthetic.main.item_message_this_user.view.textMessage
import kotlinx.android.synthetic.main.item_message_this_user.view.textTime
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class MessageThisUserAdapterDelegate : AdapterDelegate {
  
  override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
    return MessageThisUserViewHolder(parent.inflate(R.layout.item_message_this_user))
  }
  
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DisplayableItem) {
    val messageThisUserViewHolder = holder as MessageThisUserViewHolder
    val messageThis = item as MessageThisUser
    messageThisUserViewHolder.bind(messageThis)
  }
  
  class MessageThisUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    fun bind(messageThis: MessageThisUser) {
      itemView.textMessage.text = messageThis.text
      val time = LocalDateTime.ofEpochSecond(messageThis.timestamp, 0, ZonedDateTime.now().offset)
      val format = time.format(DateTimeFormatter.ofPattern(FORMAT_MSG_TIME))
      itemView.textTime.text = format
    }
    
  }
}