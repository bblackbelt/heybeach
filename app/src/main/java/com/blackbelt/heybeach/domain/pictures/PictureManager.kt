package com.blackbelt.heybeach.domain.pictures

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import android.widget.ImageView
import com.blackbelt.heybeach.data.TaskFactory
import com.blackbelt.heybeach.domain.OnDataLoadedListener
import com.blackbelt.heybeach.view.HeyBeachApp
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


interface IPictureManager {

    fun loadBitmap(path: String, onDataLoadedListener: OnDataLoadedListener<Bitmap>?)
}

class PictureManager constructor(cache: LruCache<String, Bitmap>, diskCacheRoot: File) : IPictureManager {

    private val mMemoryCache = cache

    private var mDiskCacheRoot = diskCacheRoot

    init {
        if (!mDiskCacheRoot.exists()) {
            mDiskCacheRoot.mkdirs()
        }
    }

    private fun addBitmapToMemoryCache(key: String, bitmap: Bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap)
        }
    }

    private fun getBitmapFromMemCache(key: String): Bitmap? {
        return mMemoryCache.get(key)
    }

    override fun loadBitmap(path: String, onDataLoadedListener: OnDataLoadedListener<Bitmap>?) {
        val key = path.replace("/", "-")
        val bitmap: Bitmap? = getBitmapFromMemCache(key)
        if (bitmap != null) {
            onDataLoadedListener?.onDataLoaded(bitmap)
            return
        }

        val diskBitmapFile = File(mDiskCacheRoot, key)
        if (diskBitmapFile.exists()) {
            launch(UI) {
                val downloadedBitmap = async(CommonPool) {
                    val inputStream = FileInputStream(diskBitmapFile)
                    inputStream.use { BitmapFactory.decodeStream(it) }
                }.await()
                addBitmapToMemoryCache(key, downloadedBitmap)
                onDataLoadedListener?.onDataLoaded(downloadedBitmap)
            }
            return
        }

        launch(UI) {
            val downloadedBitmap = async(CommonPool) {
                val bitmap = TaskFactory.createBitmapTask(path).call()
                try {
                    bitmap.compressAndClose(diskBitmapFile)
                } catch (e: Exception) {
                }
                bitmap
            }.await() ?: return@launch
            addBitmapToMemoryCache(key, downloadedBitmap)
            onDataLoadedListener?.onDataLoaded(downloadedBitmap)
        }
    }
}

@BindingAdapter("srcUrl")
fun ImageView.loadInto(imagePath: String) {
    HeyBeachApp.getInstance().getPictureManager()
            .loadBitmap(imagePath, object : OnDataLoadedListener<Bitmap> {
                override fun onDataLoaded(data: Bitmap) {
                    setImageBitmap(data)
                }

                override fun onError(message: String?, throwable: Throwable?) {
                }
            })
}

fun Bitmap.compressAndClose(file: File, compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 100) {
    val outputStream = FileOutputStream(file)
    outputStream.use {
        compress(compressFormat, quality, it)
    }
}