package com.blackbelt.heybeach.view.user

import android.content.Intent
import android.os.Bundle
import com.blackbelt.heybeach.BR
import com.blackbelt.heybeach.R
import com.blackbelt.heybeach.view.HeyBeachApp
import com.blackbelt.heybeach.view.View
import com.blackbelt.heybeach.view.intro.LOG_IN_KEY
import com.blackbelt.heybeach.view.main.MainActivity
import com.blackbelt.heybeach.view.misc.BaseActivity
import com.blackbelt.heybeach.view.user.viewmodel.SignUpViewModel

class SignUpActivity : BaseActivity(), View<Any> {

    private val mSignUpViewModel: SignUpViewModel by lazy {
        SignUpViewModel(HeyBeachApp.getInstance().getUserManager())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSignUpViewModel.isLogin = intent.getBooleanExtra(LOG_IN_KEY, false)
        setContentView(R.layout.activity_sign_up, BR.signUpViewModel, mSignUpViewModel)
    }

    override fun onDataLoaded(data: Any) {
        startActivity(Intent(this, MainActivity::class.java))
    }
}