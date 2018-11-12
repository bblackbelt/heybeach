package com.blackbelt.heybeach.widgets

import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.databinding.InverseBindingListener
import android.support.v7.widget.RecyclerView
import com.blackbelt.heybeach.view.misc.model.ImageInfo

private const val KEY_ITEMS = -1024

@BindingAdapter("itemViewBinder")
fun setItemViewBinder(recyclerView: RecyclerView, itemViewMapper: Map<Class<*>, AndroidItemBinder>) {

    val items = recyclerView.getTag(KEY_ITEMS) as? List<Any>?

    if (recyclerView.adapter is BindableRecyclerViewAdapter) {
        (recyclerView.adapter as BindableRecyclerViewAdapter).setDataSet(items)
        return
    }

    val adapter = BindableRecyclerViewAdapter(itemViewMapper, items)
    recyclerView.adapter = adapter
}

@BindingAdapter("itemDecoration")
fun addItemDecoration(recyclerView: RecyclerView, itemDecoration: RecyclerView.ItemDecoration) {
    recyclerView.addItemDecoration(itemDecoration)
}

@BindingAdapter(value = ["pageDescriptorAttrChanged"])
fun setListener(recyclerView: BindableRecyclerView, listener: InverseBindingListener?) {
    if (listener != null) {
        recyclerView.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageChangeListener(pageDescriptor: PageDescriptor) {
                listener.onChange()
            }
        })
    }
}

@BindingAdapter("items")
fun setItems(recyclerView: RecyclerView, items: List<Any>) {
    recyclerView.setTag(KEY_ITEMS, items)
    if (recyclerView.adapter is BindableRecyclerViewAdapter) {
        (recyclerView.adapter as BindableRecyclerViewAdapter).setDataSet(items)
    }
}

@BindingAdapter("pageDescriptor")
fun setPageDescriptor(recyclerView: BindableRecyclerView, pageDescriptor: PageDescriptor) {
    if (recyclerView.pageDescriptor != pageDescriptor) {
        recyclerView.pageDescriptor = pageDescriptor
    }
}

@InverseBindingAdapter(attribute = "pageDescriptor")
fun getPageDescriptor(recyclerView: BindableRecyclerView): PageDescriptor? {
    return recyclerView.pageDescriptor
}

@BindingAdapter("GLMItems")
fun updateGLMDataSet(recyclerView: RecyclerView, imageList: List<ImageInfo>) {
    (recyclerView.layoutManager as? BeachesGridLayoutManager)?.setDataSet(imageList)
}