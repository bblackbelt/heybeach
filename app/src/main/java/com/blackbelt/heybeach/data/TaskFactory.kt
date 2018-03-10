package com.blackbelt.heybeach.data

import android.graphics.Bitmap
import android.net.Uri
import com.blackbelt.heybeach.data.model.SignUpRequestModel


const val BASE_URL = "http://techtest.lab1886.io:3000"

object TaskFactory {

    fun createBeachTask(page: Int = 1, listener: TaskListener<String>): ITask<String> {
        val url = BASE_URL.generateUrl(listOf("beaches"), mapOf("page" to page.toString()))
        val taskDescriptor = TaskDescriptor(url)
        return Task(taskDescriptor, listener)
    }

    fun createBitmapTask(imagePath: String): ITask<Bitmap> {
        val url = BASE_URL.generateUrl(listOf(imagePath))
        val taskDescriptor = TaskDescriptor(url)
        return ImageFetcherTask(taskDescriptor)
    }

    fun createSignUpTask(body: SignUpRequestModel, listener: TaskListener<String>): ITask<String> {
        val url = BASE_URL.generateUrl(listOf("user", "register"))
        val taskDescriptor = TaskDescriptor(url, RequestMethod.POST, body.toJsonString())
        return Task(taskDescriptor, listener)
    }

}

fun String.generateUrl(pathSegments: List<String> = listOf(), query: Map<String, String> = mapOf()): String {
    val uri = Uri.parse(this).buildUpon()
    pathSegments.forEach { uri.appendEncodedPath(it) }
    query.forEach { uri.appendQueryParameter(it.key, it.value) }
    return uri.toString()
}