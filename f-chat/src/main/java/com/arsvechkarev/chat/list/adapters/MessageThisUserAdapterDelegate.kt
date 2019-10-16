package com.arsvechkarev.chat.list.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.chat.R
import com.arsvechkarev.core.extensions.inflate
import com.arsvechkarev.core.model.messaging.MessageThisUser
import com.arsvechkarev.core.recycler.DisplayableItem
import com.arsvechkarev.core.recycler.AdapterDelegate
import kotlinx.android.synthetic.main.item_message_this_user.view.textMessage

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
    }
    
  }
}