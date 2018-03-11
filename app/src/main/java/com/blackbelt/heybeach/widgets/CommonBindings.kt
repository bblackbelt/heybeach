package com.blackbelt.heybeach.widgets

import android.databinding.BindingAdapter
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView

@BindingAdapter("toVisibility")
fun setVisibility(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("toTextColor")
fun TextView.setTextColorChecked(@ColorRes color: Int) {
    if (color > 0) {
        setTextColor(ContextCompat.getColor(context, color))
    }
}

@BindingAdapter("toText")
fun TextView.setTextChecked(@StringRes stringRes: Int) {
    if (stringRes > 0) {
        setText(stringRes)
    }
}

@BindingAdapter("width", "height")
fun updateImageSize(view: View, w: Int, h: Int) {
    view.layoutParams ?: return
    view.layoutParams.width = w
    view.layoutParams.height = h
}