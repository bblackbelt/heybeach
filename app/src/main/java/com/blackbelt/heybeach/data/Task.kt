package com.blackbelt.heybeach.data

import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.concurrent.Callable

interface ITask<T> : Callable<T>

class Task constructor(taskDescriptor: TaskDescriptor, listener: TaskListener<String>) : ITask<String> {

    private val mTaskDescriptor = taskDescriptor

    private val mTaskListener: TaskListener<String>? = listener

    override fun call(): String {
        var jsonString = ""
        var urlConnection: HttpURLConnection? = null
        try {
            urlConnection = URL(mTaskDescriptor.url).openConnection() as HttpURLConnection
            urlConnection.setRequestProperty("Accept", "application/json")
            urlConnection.setRequestProperty("Content-Type", "application/json")
            urlConnection.useCaches = false
            urlConnection.requestMethod = mTaskDescriptor.requestMethod.name

            urlConnection.doInput = true

            if (mTaskDescriptor.requestMethod == RequestMethod.POST) {
                urlConnection.doOutput = true
                urlConnection.body(mTaskDescriptor.body)
            }

            if (urlConnection.responseCode == 200) {
                jsonString = urlConnection.inputStream.readTextAndClose()

                mTaskListener?.onTaskCompleted(jsonString)
            } else {
                mTaskListener?.onTaskFailed(urlConnection.errorStream.readTextAndClose(), null)
            }
        } catch (e: IOException) {
            mTaskListener?.onTaskFailed(e.message, e)
        } finally {
            urlConnection?.disconnect()
        }
        return jsonString
    }
}

fun HttpURLConnection.body(body: String?) {
    body ?: return

    val bodyArray = body.toByteArray(Charsets.UTF_8)
    setRequestProperty( "Content-Length", bodyArray.size.toString())
    outputStream.write(body.toByteArray(Charsets.UTF_8))
    outputStream.close()
}

fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String {
    return this.bufferedReader(charset).use { it.readText() }
}