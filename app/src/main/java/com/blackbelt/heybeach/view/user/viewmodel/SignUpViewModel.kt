package com.blackbelt.heybeach.view.user.viewmodel

import android.databinding.Bindable
import android.util.Patterns
import com.blackbelt.heybeach.BR
import com.blackbelt.heybeach.view.misc.viewmodel.BaseViewModel

class SignUpViewModel : BaseViewModel() {

    var loading: Boolean = false
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.loading)
        }

    var email: String? = null

    var password: String? = null

    fun doSignUp() {
        if (!canSignUp()) {
            return
        }
        loading = true
    }

    private fun canSignUp(): Boolean {
        return email?.isValidEmail() ?: false && !password.isNullOrEmpty()
    }
}

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}