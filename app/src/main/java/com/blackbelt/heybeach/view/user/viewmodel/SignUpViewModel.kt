package com.blackbelt.heybeach.view.user.viewmodel

import android.databinding.Bindable
import android.util.Patterns
import com.blackbelt.heybeach.BR
import com.blackbelt.heybeach.R
import com.blackbelt.heybeach.domain.OnDataLoadedListener
import com.blackbelt.heybeach.domain.model.ErrorModel
import com.blackbelt.heybeach.domain.user.IUserManager
import com.blackbelt.heybeach.domain.user.model.SignUpModel
import com.blackbelt.heybeach.view.misc.viewmodel.BaseViewModel

class SignUpViewModel constructor(userManager: IUserManager) : BaseViewModel(), OnDataLoadedListener<SignUpModel> {

    private val mUserManager = userManager

    var loading: Boolean = false
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.loading)
        }

    var email: String = ""

    var password: String = ""

    var isLogin: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
            notifyPropertyChanged(BR.buttonLabel)
        }

    @Bindable
    fun getTitle(): Int {
        return if (isLogin) {
            R.string.login
        } else {
            R.string.sign_up_title
        }
    }

    @Bindable
    fun getButtonLabel(): Int {
        return if (isLogin) {
            R.string.login
        } else {
            R.string.signup_for_free
        }
    }

    fun doSignUp() {
        if (!canSignUp()) {
            return
        }
        loading = true
        if (isLogin) {
            mUserManager.signIn(email, password, this)
        } else {
            mUserManager.signUp(email, password, this)
        }
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

    override fun onDataLoaded(data: SignUpModel) {
        loading = false
        mListener?.onDataLoaded(data)
    }

    override fun onError(message: ErrorModel?, throwable: Throwable?) {
        mListener?.onError(message, throwable)
        loading = false
    }
}

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return length > 5
}