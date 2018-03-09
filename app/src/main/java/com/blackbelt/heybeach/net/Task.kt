package com.blackbelt.heybeach.net

import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class Task constructor(taskDescriptor: TaskDescriptor, listener: TaskListener<String>) : Runnable {

    private val mTaskDescriptor = taskDescriptor

    private val mTaskListener: TaskListener<String>? = listener

    override fun run() {
        var urlConnection: HttpURLConnection? = null
        try {
            urlConnection = URL(mTaskDescriptor.url).openConnection() as HttpURLConnection;
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.requestMethod = mTaskDescriptor.requestMethod.name

            if (mTaskDescriptor.requestMethod == RequestMethod.POST) {
                urlConnection.body(mTaskDescriptor.body)
            }

            if (urlConnection.responseCode == 200) {
                val jsonString = urlConnection.inputStream.readTextAndClose()
                mTaskListener?.onTaskCompleted(jsonString)
            } else {
                mTaskListener?.onTaskFailed(urlConnection.errorStream.readTextAndClose(), null)
            }
        } catch (e: IOException) {
            mTaskListener?.onTaskFailed(e.message, e)
        } finally {
            urlConnection?.disconnect()
        }
    }
}

fun HttpURLConnection.body(body : String?) {
    body ?: return
    outputStream.bufferedWriter().use { it.write(body) }
}

fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String {
    return this.bufferedReader(charset).use { it.readText() }
}