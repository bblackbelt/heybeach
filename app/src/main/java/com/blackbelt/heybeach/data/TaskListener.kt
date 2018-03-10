package com.blackbelt.heybeach.data

interface TaskListener<T> {
    fun onTaskCompleted(result: T) {
    }

    fun onTaskCompleted(result: T, token: String?) {
    }

    fun onTaskFailed(message: String?, throwable: Throwable?)
}