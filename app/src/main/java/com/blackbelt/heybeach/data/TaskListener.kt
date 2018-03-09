package com.blackbelt.heybeach.data

interface TaskListener<T> {
    fun onTaskCompleted(result: T)

    fun onTaskFailed(message: String?, throwable: Throwable?)
}