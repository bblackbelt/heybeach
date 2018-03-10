package com.blackbelt.heybeach.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.blackbelt.heybeach.BR
import com.blackbelt.heybeach.R
import com.blackbelt.heybeach.view.HeyBeachApp
import com.blackbelt.heybeach.view.intro.IntroActivity
import com.blackbelt.heybeach.view.main.viewmodel.MainViewModel
import com.blackbelt.heybeach.view.misc.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val mMainViewModel: MainViewModel by lazy {
        MainViewModel(HeyBeachApp.getInstance().getUserManager(), HeyBeachApp.getInstance().getBeachesManager())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main, BR.mainViewModel, mMainViewModel)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        mMainViewModel.logOut()
        startActivity(Intent(this, IntroActivity::class.java))
        return super.onOptionsItemSelected(item)
    }
}
