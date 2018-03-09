package com.blackbelt.heybeach.view

import android.app.Application
import com.blackbelt.heybeach.data.ParsingFactory
import com.blackbelt.heybeach.data.RequestExecutor
import com.blackbelt.heybeach.domain.beaches.BeachesManager
import com.blackbelt.heybeach.domain.beaches.IBeachesManager

class HeyBeachApp : Application() {

    companion object {
        private lateinit var mHeyBeachApp: HeyBeachApp

        fun getInstance() = mHeyBeachApp
    }

    private val mBeachesManager: IBeachesManager by lazy {
        BeachesManager(RequestExecutor.getInstance(), ParsingFactory)
    }

    override fun onCreate() {
        super.onCreate()
        mHeyBeachApp = this
    }

    fun getBeachesManager() = mBeachesManager

}