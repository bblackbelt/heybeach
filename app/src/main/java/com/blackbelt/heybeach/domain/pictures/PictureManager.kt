package com.blackbelt.heybeach.domain.pictures

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import android.widget.ImageView
import com.blackbelt.heybeach.data.TaskFactory
import com.blackbelt.heybeach.domain.OnDataLoadedListener
import com.blackbelt.heybeach.domain.model.ErrorModel
import com.blackbelt.heybeach.view.HeyBeachApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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
            GlobalScope.launch(Dispatchers.Main) {
                val downloadedBitmap = GlobalScope.async(Dispatchers.IO) {
                    val inputStream = FileInputStream(diskBitmapFile)
                    inputStream.use { BitmapFactory.decodeStream(it) }
                }.await()
                onDataLoadedListener?.onDataLoaded(downloadedBitmap)
                addBitmapToMemoryCache(key, downloadedBitmap)
            }

            return
        }

        GlobalScope.launch(Dispatchers.Main) {
            val downloadedBitmap = async(Dispatchers.IO) {
                val bitmap = TaskFactory.createBitmapTask(path).call()
                try {
                    bitmap.compressAndClose(diskBitmapFile)
                } catch (e: Exception) {
                }
                bitmap
            }.await() ?: return@launch
            onDataLoadedListener?.onDataLoaded(downloadedBitmap)
            addBitmapToMemoryCache(key, downloadedBitmap)
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

                override fun onError(message: ErrorModel?, throwable: Throwable?) {
                }
            })
}

fun Bitmap.compressAndClose(file: File, compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG, quality: Int = 100) {
    val outputStream = FileOutputStream(file)
    outputStream.use {
        compress(compressFormat, quality, it)
    }
}