package com.blackbelt.heybeach.view.misc

import android.databinding.Bindable
import android.databinding.Observable
import com.blackbelt.heybeach.R

interface IErrorView : Observable {

    @Bindable
    fun getErrorText(): Int?

    fun getErrorTextColor() = R.color.colorAccent

    fun getReloadText() = R.string.reload

    fun reload()

    @Bindable
    fun isErrorViewVisible(): Boolean
}