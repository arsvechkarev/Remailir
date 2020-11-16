package com.arsvechkarev.core.recycler

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.arsvechkarev.core.concurrency.AndroidThreader
import com.arsvechkarev.core.concurrency.Threader
import com.arsvechkarev.core.recycler.CallbackType.ALWAYS_FALSE
import com.arsvechkarev.core.recycler.CallbackType.APPENDED_LIST
import com.arsvechkarev.core.recycler.CallbackType.TWO_LISTS
import kotlin.reflect.KClass

abstract class ListAdapter(
  private val delegates: List<ListAdapterDelegate<out DifferentiableItem>>,
  private val callbackType: CallbackType = TWO_LISTS,
  private val threader: Threader = AndroidThreader,
  private var onReadyToLoadNextPage: (() -> Unit)? = null
) : RecyclerView.Adapter<ViewHolder>() {
  
  protected var recyclerView: RecyclerView? = null
    private set
  
  protected var data: MutableList<DifferentiableItem> = ArrayList()
  private val classesToViewTypes = HashMap<KClass<*>, Int>()
  private val delegatesSparseArray = SparseArrayCompat<ListAdapterDelegate<out DifferentiableItem>>()
  
  constructor(
    vararg delegates: ListAdapterDelegate<out DifferentiableItem>,
    callbackType: CallbackType = TWO_LISTS,
    threader: Threader = AndroidThreader,
    onReadyToLoadNextPage: () -> Unit = {}
  ) : this(delegates.toList(), callbackType, threader, onReadyToLoadNextPage)
  
  init {
    delegates.forEachIndexed { i, delegate ->
      classesToViewTypes[delegate.modelClass] = i
      delegatesSparseArray.put(i, delegate)
    }
  }
  
  fun addItem(item: DifferentiableItem) {
    threader.onMainThread {
      data.add(item)
      notifyItemInserted(data.size - 1)
    }
  }
  
  fun removeLastAndAdd(list: List<DifferentiableItem>) {
    val oldSize = data.size
    data.removeLast()
    data.addAll(list)
    applyChanges(AppendedListDiffCallbacks(data, oldSize))
  }
  
  fun addItems(list: List<DifferentiableItem>) {
    val oldSize = data.size
    data.addAll(list)
    applyChanges(AppendedListDiffCallbacks(data, oldSize))
  }
  
  fun submitList(list: List<DifferentiableItem>) {
    val callback = when (callbackType) {
      APPENDED_LIST -> AppendedListDiffCallbacks(list, data.size)
      TWO_LISTS -> TwoListsDiffCallBack(data, list)
      ALWAYS_FALSE -> AlwaysFalseCallback(data, list)
    }
    data = list as MutableList<DifferentiableItem>
    applyChanges(callback)
  }
  
  private fun applyChanges(callback: DiffUtil.Callback) {
    threader.onBackground {
      val diffResult = DiffUtil.calculateDiff(callback)
      threader.onMainThread {
        diffResult.dispatchUpdatesTo(this)
      }
    }
  }
  
  override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    this.recyclerView = recyclerView
    delegates.forEach { it.onAttachedToRecyclerView(recyclerView) }
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val delegate = delegatesSparseArray[viewType] ?: error("No delegate for view type $viewType")
    return delegate.onCreateViewHolder(parent)
  }
  
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    if (position == data.size - 3) {
      onReadyToLoadNextPage?.invoke()
    }
    val adapterDelegate = delegatesSparseArray[getItemViewType(position)]
        ?: error("No delegate for position $position")
    adapterDelegate.onBindViewHolderRaw(holder, data[position])
  }
  
  override fun getItemViewType(position: Int): Int {
    return classesToViewTypes[data[position]::class] ?: error(
      "Can't find delegate for position: $position")
  }
  
  override fun getItemCount(): Int {
    return data.size
  }
  
  override fun onViewRecycled(holder: ViewHolder) {
    (holder as? DelegateViewHolder<*>)?.onViewRecycled()
  }
  
  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    delegates.forEach { it.onDetachedFromRecyclerView(recyclerView) }
    onReadyToLoadNextPage = null
  }
}