package com.blackbelt.heybeach.widgets

import android.databinding.BindingAdapter
import android.view.View

@BindingAdapter("toVisibility")
fun setVisibility(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}