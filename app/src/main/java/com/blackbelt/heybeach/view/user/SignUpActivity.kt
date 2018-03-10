package com.blackbelt.heybeach.view.user

import android.os.Bundle
import com.blackbelt.heybeach.BR
import com.blackbelt.heybeach.R
import com.blackbelt.heybeach.view.misc.BaseActivity
import com.blackbelt.heybeach.view.user.viewmodel.SignUpViewModel

class SignUpActivity : BaseActivity() {

    private val mSignUpViewModel: SignUpViewModel by lazy {
        SignUpViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up, BR.signUpViewModel, mSignUpViewModel)
    }

}