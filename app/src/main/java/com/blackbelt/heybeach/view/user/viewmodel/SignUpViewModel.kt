package com.blackbelt.heybeach.view.user.viewmodel

import android.databinding.Bindable
import com.blackbelt.heybeach.BR
import com.blackbelt.heybeach.R
import com.blackbelt.heybeach.domain.OnDataLoadedListener
import com.blackbelt.heybeach.domain.model.ErrorModel
import com.blackbelt.heybeach.domain.user.IUserManager
import com.blackbelt.heybeach.domain.user.model.SignUpModel
import com.blackbelt.heybeach.view.misc.viewmodel.BaseViewModel
import java.util.regex.Pattern

open class SignUpViewModel constructor(userManager: IUserManager) : BaseViewModel(), OnDataLoadedListener<SignUpModel> {

    private val mUserManager = userManager

    private val mEmailPattern = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+")

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

    fun canSignUp(): Boolean {

        if (!isValidEmail(email)) {
            mListener?.onError(null, null, R.string.invalid_email)
            return false
        }

        if (!isValidPassword(password)) {
            mListener?.onError(null, null, R.string.invalid_password)
            return false
        }

        return true
    }

    override fun onDataLoaded(data: SignUpModel) {
        loading = false
        mListener?.onDataLoaded(data)
    }

    override fun onError(message: ErrorModel?, throwable: Throwable?) {
        mListener?.onError(message, throwable)
        loading = false
    }

    fun isValidPassword(password: String): Boolean {
        return password.length > 5
    }

    fun isValidEmail(email: String): Boolean {
        return mEmailPattern.matcher(email).matches()
    }
}