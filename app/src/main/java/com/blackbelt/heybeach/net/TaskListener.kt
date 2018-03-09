package com.blackbelt.heybeach.net

interface TaskListener<T> {
    fun onTaskCompleted(result: T)

    fun onTaskFailed(message: String?, throwable: Throwable?)
}