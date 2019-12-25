package com.arsvechkarev.auth.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsvechakrev.auth.R
import core.extensions.inflate
import core.model.other.Letter
import core.recycler.AdapterDelegate
import core.recycler.DisplayableItem
import kotlinx.android.synthetic.main.item_letter.view.textLetter

class LetterAdapterDelegate : AdapterDelegate {
  
  override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
    return LetterViewHolder(parent.inflate(R.layout.item_letter))
  }
  
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DisplayableItem) {
    val letterViewHolder = holder as LetterViewHolder
    val letter = item as Letter
    letterViewHolder.bind(letter)
  }
  
  class LetterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    fun bind(letter: Letter) {
      itemView.textLetter.text = letter.digit
    }
  }
}