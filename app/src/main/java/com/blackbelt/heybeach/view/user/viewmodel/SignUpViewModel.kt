package com.blackbelt.heybeach.view.user.viewmodel

import android.databinding.Bindable
import android.util.Patterns
import com.blackbelt.heybeach.BR
import com.blackbelt.heybeach.R
import com.blackbelt.heybeach.domain.OnDataLoadedListener
import com.blackbelt.heybeach.domain.model.ErrorModel
import com.blackbelt.heybeach.domain.user.IUserManager
import com.blackbelt.heybeach.domain.user.model.SignUpModel
import com.blackbelt.heybeach.view.View
import com.blackbelt.heybeach.view.misc.viewmodel.BaseViewModel

class SignUpViewModel constructor(userManager: IUserManager) : BaseViewModel() {

    private val mUserManager = userManager

    var loading: Boolean = false
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.loading)
        }

    var mListener: View<SignUpModel>? = null

    var email: String = ""

    var password: String = ""

    fun doSignUp() {
        if (!canSignUp()) {
            return
        }
        loading = true
        mUserManager.signUp(email, password, object : OnDataLoadedListener<SignUpModel> {
            override fun onDataLoaded(data: SignUpModel) {
                loading = false
                mListener?.onDataLoaded(data)
            }

            override fun onError(message: ErrorModel?, throwable: Throwable?) {
                mListener?.onError(message, throwable)
                loading = false
            }
        })
    }

    private fun canSignUp(): Boolean {

        val validEmail = email.isValidEmail()
        if (!validEmail) {
            mListener?.onError(null, null, R.string.invalid_email)
            return false
        }

        val validPassword = password.isValidPassword()
        if (!validPassword) {
            mListener?.onError(null, null, R.string.invalid_password)
            return false
        }

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mListener = null
    }
}

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return length > 5
}