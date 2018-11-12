package com.blackbelt.heybeach.widgets

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.support.v7.util.DiffUtil.calculateDiff
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*


class AndroidItemBinder(val layoutId: Int, val dataBindingVariable: Int)

class BindableRecyclerViewAdapter internal constructor(private val mItemBinder: Map<Class<*>, AndroidItemBinder>,
                                                       items: List<*>?) : RecyclerView.Adapter<BindableRecyclerViewAdapter.BindableViewHolder>() {

    class BindableViewHolder(viewDataBinding: ViewDataBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
        val mViewDataBinding = viewDataBinding
    }

    val dataSet: ObservableList<Any> = ObservableArrayList()

    private val pendingUpdates = ArrayDeque<List<Any>>()

    init {
        if (items != null) {
            setDataSet(ArrayList(items))
        }
    }

    fun setDataSet(items: List<*>?) {
        val oldItems = ArrayList(dataSet)
        GlobalScope.launch(Dispatchers.Main){
            val resultPair = GlobalScope.async(Dispatchers.IO) {
                Pair<List<*>?, DiffUtil.DiffResult>(items,
                        calculateDiff(ItemSourceDiffCallback(oldItems, items)))
            }.await()
            applyDiffResult(resultPair)
        }
    }

    private fun applyDiffResult(resultPair: Pair<List<*>?, DiffUtil.DiffResult>) {

        var firstStart = true

        if (!pendingUpdates.isEmpty()) {
            pendingUpdates.remove()
        }

        if (dataSet.size > 0) {
            dataSet.clear()
            firstStart = false
        }

        if (resultPair.first != null) {
            dataSet.addAll(ArrayList(resultPair.first))
        }

        if (firstStart) {
            notifyDataSetChanged()
        } else {
            resultPair.second.dispatchUpdatesTo(this)
        }

        if (pendingUpdates.size > 0) {
            setDataSet(pendingUpdates.peek())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), viewType, parent, false)
        return BindableViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        val key = dataSet[position].javaClass
        val dataBinder: AndroidItemBinder = mItemBinder[key]
                ?: throw Exception("AndroidDataBinder not configured correctly $key")
        return dataBinder.layoutId
    }

    override fun onBindViewHolder(holder: BindableViewHolder, position: Int) {
        val item = dataSet[position]
        val itemBinder = mItemBinder[item.javaClass] ?: return
        holder.mViewDataBinding.setVariable(itemBinder.dataBindingVariable, item)
        holder.mViewDataBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}