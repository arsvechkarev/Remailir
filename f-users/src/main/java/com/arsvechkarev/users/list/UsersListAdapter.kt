package com.arsvechkarev.users.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.core.model.users.OtherUser
import com.arsvechkarev.users.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_user.view.imageProfile
import kotlinx.android.synthetic.main.item_user.view.textUsername

class UsersListAdapter(private val clickListener: (OtherUser) -> Unit) :
  ListAdapter<OtherUser, FriendsViewHolder>(UsersDiffCallback()) {
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    return FriendsViewHolder(inflater.inflate(R.layout.item_user, parent, false))
  }
  
  override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
    holder.bind(getItem(position), clickListener)
  }
  
}

class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  
  fun bind(otherUser: OtherUser, clickListener: (OtherUser) -> Unit) {
    Picasso.get().load("https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg")
      .into(itemView.imageProfile)
    itemView.textUsername.text = otherUser.username
    itemView.setOnClickListener { clickListener(otherUser) }
  }
  
}