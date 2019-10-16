package com.arsvechkarev.friends.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.friends.R
import com.arsvechkarev.core.model.Friend
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_friend.view.imageProfile
import kotlinx.android.synthetic.main.item_friend.view.textUsername

class FriendsListAdapter(private val clickListener: (Friend) -> Unit) :
  ListAdapter<Friend, FriendsViewHolder>(FriendsDiffCallback()) {
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    return FriendsViewHolder(inflater.inflate(R.layout.item_friend, parent, false))
  }
  
  override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
    holder.bind(getItem(position), clickListener)
  }
  
}

class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  
  fun bind(friend: Friend, clickListener: (Friend) -> Unit) {
    Picasso.get().load("https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg")
      .into(itemView.imageProfile)
    itemView.textUsername.text = friend.username
    itemView.setOnClickListener { clickListener(friend) }
  }
  
}