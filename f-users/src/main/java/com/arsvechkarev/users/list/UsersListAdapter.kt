package com.arsvechkarev.users.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.users.R
import com.squareup.picasso.Picasso
import core.model.users.User
import core.recycler.DisplayableItem.DiffCallBack
import core.strings.DEFAULT_IMG_URL
import core.util.inflate
import kotlinx.android.synthetic.main.item_user.view.imageProfile
import kotlinx.android.synthetic.main.item_user.view.textUsername

class UsersListAdapter(private val clickListener: (User) -> Unit) :
  ListAdapter<User, FriendsViewHolder>(DiffCallBack()) {
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
    return FriendsViewHolder(parent.inflate(R.layout.item_user))
  }
  
  override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
    holder.bind(getItem(position), clickListener)
  }
  
}

class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  
  fun bind(otherUser: User, clickListener: (User) -> Unit) {
    if (otherUser.imageUrl == DEFAULT_IMG_URL) {
      itemView.imageProfile.setBackgroundResource(com.arsvechkarev.messaging.R.drawable.image_profile_stub)
    } else {
      Picasso.get().load(otherUser.imageUrl).into(itemView.imageProfile)
    }
    itemView.textUsername.text = otherUser.name
    itemView.setOnClickListener { clickListener(otherUser) }
  }
  
}