package core.recycler

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class BaseAdapter : RecyclerView.Adapter<ViewHolder>() {
  
  var data: MutableList<DisplayableItem> = ArrayList()
  val delegates = SparseArrayCompat<AdapterDelegate>()
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return delegates[viewType]!!.onCreateViewHolder(parent)
  }
  
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    delegates[getItemViewType(position)]!!.onBindViewHolder(holder, data[position])
  }
  
  override fun getItemViewType(position: Int): Int {
    return data[position].type
  }
  
  override fun getItemCount(): Int {
    return data.size
  }
  
  fun submitList(list: MutableList<DisplayableItem>?) {
    data = list ?: ArrayList()
    notifyDataSetChanged()
  }
}