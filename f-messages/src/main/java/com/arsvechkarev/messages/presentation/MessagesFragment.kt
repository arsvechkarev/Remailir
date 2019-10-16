package com.arsvechkarev.messages.presentation


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arsvechkarev.messages.R


class MessagesFragment : Fragment() {
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_messages, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  
  }
}
