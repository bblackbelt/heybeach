package com.blackbelt.heybeach.view.user

import android.os.Bundle
import com.blackbelt.heybeach.BR
import com.blackbelt.heybeach.R
import com.blackbelt.heybeach.domain.model.ErrorModel
import com.blackbelt.heybeach.domain.user.model.SignUpModel
import com.blackbelt.heybeach.view.HeyBeachApp
import com.blackbelt.heybeach.view.View
import com.blackbelt.heybeach.view.misc.BaseActivity
import com.blackbelt.heybeach.view.user.viewmodel.SignUpViewModel

class SignUpActivity : BaseActivity(), View<SignUpModel> {

    private val mSignUpViewModel: SignUpViewModel by lazy {
        SignUpViewModel(HeyBeachApp.getInstance().getUserManager())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up, BR.signUpViewModel, mSignUpViewModel)
        mSignUpViewModel.mListener = this
    }

    override fun onDataLoaded(data: SignUpModel) {
    }

    override fun onError(message: ErrorModel?, throwable: Throwable?, resId: Int) {
        val errorMessage = message?.message ?: if (resId != -1) getString(resId) else getString(R.string.oops_something_went_wrong)
        showErrorMessage(errorMessage)
    }
}