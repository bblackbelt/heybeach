package com.blackbelt.heybeach.data

import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.concurrent.Callable

interface ITask<T> : Callable<T>

private const val X_AUTH_HEADER = "x-auth"

class Task constructor(taskDescriptor: TaskDescriptor, listener: TaskListener<String>, xAuthToken: String? = null) : ITask<String> {

    private val mTaskDescriptor = taskDescriptor

    private val mTaskListener: TaskListener<String>? = listener

    private val mAuthToken = xAuthToken

    override fun call(): String {
        var jsonString = ""
        var urlConnection: HttpURLConnection? = null
        try {
            urlConnection = URL(mTaskDescriptor.url).openConnection() as HttpURLConnection
            urlConnection.setRequestProperty("Accept", "application/json")
            urlConnection.setRequestProperty("Content-Type", "application/json")
            urlConnection.useCaches = false
            urlConnection.requestMethod = mTaskDescriptor.requestMethod.name

            if (mAuthToken != null) {
                urlConnection.setRequestProperty(X_AUTH_HEADER, mAuthToken)
            }

            urlConnection.doInput = true

            if (mTaskDescriptor.requestMethod == RequestMethod.POST) {
                urlConnection.doOutput = true
                urlConnection.body(mTaskDescriptor.body)
            }

            if (urlConnection.responseCode == 200) {

                val token: String? = urlConnection.getHeaderField(X_AUTH_HEADER)
                jsonString = urlConnection.inputStream.readTextAndClose()

                if (token == null) {
                    mTaskListener?.onTaskCompleted(jsonString)
                } else {
                    mTaskListener?.onTaskCompleted(jsonString, token)
                }
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
    setRequestProperty("Content-Length", bodyArray.size.toString())
    outputStream.write(body.toByteArray(Charsets.UTF_8))
    outputStream.close()
}

fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String {
    return this.bufferedReader(charset).use { it.readText() }
}