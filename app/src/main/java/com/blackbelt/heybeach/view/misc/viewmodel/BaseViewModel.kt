package com.blackbelt.heybeach.view.misc.viewmodel

import android.databinding.BaseObservable

class ProgressLoader

abstract class BaseViewModel : BaseObservable() {

    open fun onCreate() {}

    open fun onStart() {}

    open fun onStop() {}

    open fun onDestroy() {}
}