package com.blackbelt.heybeach.view.intro

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.blackbelt.heybeach.BR
import com.blackbelt.heybeach.R
import com.blackbelt.heybeach.domain.beaches.IBeachesManager
import com.blackbelt.heybeach.view.HeyBeachApp
import com.blackbelt.heybeach.view.intro.viewmodel.IntroViewModel
import com.blackbelt.heybeach.view.misc.BaseActivity
import com.blackbelt.heybeach.view.user.SignUpActivity
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : BaseActivity() {

    private val mBeachesManager: IBeachesManager by lazy {
        HeyBeachApp.getInstance().getBeachesManager()
    }

    private val mIntroViewModel: IntroViewModel by lazy {
        IntroViewModel(mBeachesManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro, BR.introViewModel, mIntroViewModel)
        rv.layoutManager = GridLayoutManager(this, 2)
        createAccountButton.setOnClickListener { startActivity(Intent(this, SignUpActivity::class.java)) }
    }
}