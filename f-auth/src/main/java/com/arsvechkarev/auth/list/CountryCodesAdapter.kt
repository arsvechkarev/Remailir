package com.arsvechkarev.auth.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsvechakrev.auth.R
import com.arsvechkarev.core.extensions.inflate
import com.arsvechkarev.core.model.Country
import kotlinx.android.synthetic.main.item_country.view.textCountry

class CountryCodesAdapter(
  private val clickListener: (Country) -> Unit = {}
) : RecyclerView.Adapter<CountryCodesAdapter.CountryCodesViewHolder>() {
  
  private var data: List<Country> = ArrayList()
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryCodesViewHolder {
    return CountryCodesViewHolder(parent.inflate(R.layout.item_country))
  }
  
  override fun getItemCount() = data.size
  
  override fun onBindViewHolder(holder: CountryCodesViewHolder, position: Int) {
    holder.bind(data[position], clickListener)
  }
  
  fun submitList(data: List<Country>) {
    this.data = data
    notifyDataSetChanged()
  }
  
  class CountryCodesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: Country, clickListener: (Country) -> Unit) {
      itemView.textCountry.text = item.name
      itemView.setOnClickListener { clickListener(item) }
    }
  }
}