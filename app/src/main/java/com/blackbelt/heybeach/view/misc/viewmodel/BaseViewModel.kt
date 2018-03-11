package com.blackbelt.heybeach.view.misc.viewmodel

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.blackbelt.heybeach.BR
import com.blackbelt.heybeach.R
import com.blackbelt.heybeach.domain.model.ErrorModel
import com.blackbelt.heybeach.view.View
import com.blackbelt.heybeach.view.misc.IErrorView
import java.io.IOException

class ProgressLoader

abstract class BaseViewModel : BaseObservable(), IErrorView {

    var mListener: View<Any>? = null

    var error: Int? = null
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.errorText)
            notifyPropertyChanged(BR.errorViewVisible)
        }

    open fun onCreate() {}

    open fun onStart() {}

    open fun onStop() {}

    open fun onDestroy() {
        mListener = null
    }

    open fun handleError(message: ErrorModel?, throwable: Throwable?) {
        error = when (throwable is IOException) {
            true -> R.string.connection_error
            false -> R.string.oops_something_went_wrong
        }
    }

    @Bindable
    override fun getErrorText(): Int? = error

    override fun reload() {
        error = null
    }

    @Bindable
    override fun isErrorViewVisible(): Boolean =
            getErrorText()?.let {
                it > 0
            } ?: false
}