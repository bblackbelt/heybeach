package com.blackbelt.heybeach.view.misc.viewmodel

import android.databinding.BaseObservable
import com.blackbelt.heybeach.view.View

class ProgressLoader

abstract class BaseViewModel : BaseObservable() {

    var mListener: View<Any>? = null

    open fun onCreate() {}

    open fun onStart() {}

    open fun onStop() {}

    open fun onDestroy() {
        mListener = null
    }
}