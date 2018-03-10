package com.blackbelt.heybeach.view.intro

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.blackbelt.heybeach.BR
import com.blackbelt.heybeach.R
import com.blackbelt.heybeach.domain.beaches.IBeachesManager
import com.blackbelt.heybeach.view.HeyBeachApp
import com.blackbelt.heybeach.view.intro.viewmodel.IntroViewModel
import com.blackbelt.heybeach.view.main.MainActivity
import com.blackbelt.heybeach.view.misc.BaseActivity
import com.blackbelt.heybeach.view.user.SignUpActivity
import kotlinx.android.synthetic.main.activity_intro.*

const val LOG_IN_KEY = "LOG_IN_KEY"

class IntroActivity : BaseActivity() {

    private val mBeachesManager: IBeachesManager by lazy {
        HeyBeachApp.getInstance().getBeachesManager()
    }

    private val mIntroViewModel: IntroViewModel by lazy {
        IntroViewModel(mBeachesManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!HeyBeachApp.getInstance().getUserManager().getAuthToken().isNullOrEmpty()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_intro, BR.introViewModel, mIntroViewModel)

        rv.layoutManager = LinearLayoutManager(this)

        val margin: Int = resources.getDimension(R.dimen.margin_16).toInt()
        rv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect?.bottom = margin
                if (parent?.layoutManager?.getPosition(view) == 0) {
                    outRect?.top = margin
                }
                outRect?.left = margin
                outRect?.right = margin
            }
        })

        createAccountButton.setOnClickListener { startActivity(Intent(this, SignUpActivity::class.java)) }
        login.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra(LOG_IN_KEY, true)
            startActivity(intent)
        }
    }
}