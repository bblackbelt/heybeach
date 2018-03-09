package com.blackbelt.heybeach.view

import android.app.Application
import android.graphics.Bitmap
import android.util.LruCache
import com.blackbelt.heybeach.data.ParsingFactory
import com.blackbelt.heybeach.data.RequestExecutor
import com.blackbelt.heybeach.domain.beaches.BeachesManager
import com.blackbelt.heybeach.domain.beaches.IBeachesManager
import com.blackbelt.heybeach.domain.pictures.IPictureManager
import com.blackbelt.heybeach.domain.pictures.PictureManager
import java.io.File

class HeyBeachApp : Application() {

    companion object {
        private lateinit var mHeyBeachApp: HeyBeachApp

        fun getInstance() = mHeyBeachApp
    }

    private val mBeachesManager: IBeachesManager by lazy {
        BeachesManager(RequestExecutor.getInstance(), ParsingFactory)
    }

    private val mPictureCache: LruCache<String, Bitmap> by lazy {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024);
        object : LruCache<String, Bitmap>(maxMemory.toInt()) {
            override fun sizeOf(key: String?, value: Bitmap?): Int {
                value ?: return -1
                return value.byteCount / 1024
            }
        }
    }

    private val mPictureManager: IPictureManager by lazy {
        PictureManager(mPictureCache, File(getExternalFilesDir(null), "images"))
    }

    override fun onCreate() {
        super.onCreate()
        mHeyBeachApp = this
    }

    fun getBeachesManager() = mBeachesManager

    fun getPictureManager() = mPictureManager
}