package com.blackbelt.heybeach.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ImageFetcherTask constructor(taskDescriptor: TaskDescriptor, taskListener: TaskListener<Bitmap>? = null) : ITask<Bitmap> {

    private val mTaskDescriptor = taskDescriptor

    private val mTaskListener: TaskListener<Bitmap>? = taskListener

    override fun call(): Bitmap? {

        var bitmap: Bitmap? = null
        var urlConnection: HttpURLConnection? = null

        try {
            urlConnection = URL(mTaskDescriptor.url).openConnection() as HttpURLConnection;
            urlConnection.setRequestProperty("Accept", "application/json")
            urlConnection.requestMethod = mTaskDescriptor.requestMethod.name

            if (urlConnection.responseCode == 200) {
                bitmap = BitmapFactory.decodeStream(urlConnection.inputStream)
                mTaskListener?.onTaskCompleted(bitmap)
            } else {
                mTaskListener?.onTaskFailed(urlConnection.errorStream.readTextAndClose(), null)
            }

        } catch (e: IOException) {
            mTaskListener?.onTaskFailed(e.message, e)
        } finally {
            urlConnection?.disconnect()
        }
        return bitmap
    }
}