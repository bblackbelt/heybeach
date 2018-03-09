package com.blackbelt.heybeach.data

import android.net.Uri


const val BASE_URL = "http://techtest.lab1886.io:3000"

object TaskFactory {

    fun createBeachTask(page: Int = 1, listener: TaskListener<String>): Task {
        val url = BASE_URL.generateUrl(listOf("beaches"), mapOf("page" to page.toString()))
        val taskDescriptor = TaskDescriptor(url)
        return Task(taskDescriptor, listener)
    }
}

fun String.generateUrl(pathSegments: List<String> = listOf(), query: Map<String, String> = mapOf()): String {
    val uri = Uri.parse(this).buildUpon()
    pathSegments.forEach { uri.appendPath(it) }
    query.forEach { uri.appendQueryParameter(it.key, it.value) }
    return uri.toString()
}