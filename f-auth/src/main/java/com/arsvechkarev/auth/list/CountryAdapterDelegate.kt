package com.arsvechkarev.auth.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsvechakrev.auth.R
import core.model.other.Country
import core.recycler.AdapterDelegate
import core.recycler.DisplayableItem
import core.util.inflate
import kotlinx.android.synthetic.main.item_country.view.textCountry
import kotlinx.android.synthetic.main.item_country.view.textCountryCode

class CountryAdapterDelegate(
  private val clickListener: (Country) -> Unit
) : AdapterDelegate {
  
  override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
    return CountryViewHolder(parent.inflate(R.layout.item_country), clickListener)
  }
  
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: DisplayableItem) {
    val countryViewHolder = holder as CountryViewHolder
    val country = item as Country
    countryViewHolder.bind(country)
  }
  
  class CountryViewHolder(
    itemView: View,
    private val clickListener: (Country) -> Unit
  ) : RecyclerView.ViewHolder(itemView) {
    
    fun bind(country: Country) {
      itemView.textCountry.text = country.name
      itemView.textCountryCode.text = "+${country.code}"
      itemView.setOnClickListener {
        clickListener(country)
      }
    }
  }
}