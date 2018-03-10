package com.blackbelt.heybeach.view.main

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
        main_rv.layoutManager = LinearLayoutManager(this)

        val margin: Int = resources.getDimension(R.dimen.margin_16).toInt()

        main_rv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect?.bottom = margin
                if (parent?.layoutManager?.getPosition(view) == 0) {
                    outRect?.top = margin
                }
                outRect?.left = margin
                outRect?.right = margin
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        mMainViewModel.logOut()
        startActivity(Intent(this, IntroActivity::class.java))
        finish()
        return super.onOptionsItemSelected(item)
    }
}
