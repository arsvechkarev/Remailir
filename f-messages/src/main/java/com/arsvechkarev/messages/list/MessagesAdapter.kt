package com.arsvechkarev.messages.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.messages.R
import core.extensions.gone
import core.extensions.inflate
import core.extensions.visible
import core.model.messaging.Chat
import core.recycler.DisplayableItem
import core.strings.DEFAULT_IMG_URL
import firebase.thisUser
import kotlinx.android.synthetic.main.item_chat.view.divider
import kotlinx.android.synthetic.main.item_chat.view.imageUser
import kotlinx.android.synthetic.main.item_chat.view.textLastMessage
import kotlinx.android.synthetic.main.item_chat.view.textUsername

class MessagesAdapter(
  private val clickListener: (Chat) -> Unit
) : ListAdapter<Chat, MessagesAdapter.ChatViewHolder>(DisplayableItem.DiffCallBack()) {
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
    return ChatViewHolder(parent.inflate(R.layout.item_chat))
  }
  
  override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
    holder.bind(getItem(position))
  }
  
  inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    fun bind(chat: Chat) {
      if (adapterPosition == itemCount - 1) {
        itemView.divider.gone()
      } else {
        itemView.divider.visible()
      }
      itemView.setOnClickListener { clickListener(chat) }
      itemView.textUsername.text = chat.otherUser.username
      if (chat.otherUser.imageUrl == DEFAULT_IMG_URL) {
        itemView.imageUser.setBackgroundResource(R.drawable.image_stub)
      } else {
      }
      chat.lastMessage?.let {
        itemView.textLastMessage.text = if (it.fromUserId == thisUser.uid) {
          "You: ${it.text}"
        } else {
          "${chat.otherUser.username}: ${it.text}"
        }
      }
    }
  }
  
}