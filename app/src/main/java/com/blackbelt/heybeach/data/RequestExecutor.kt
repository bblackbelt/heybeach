package com.blackbelt.heybeach.data

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RequestExecutor {

    private var mExecutor: ExecutorService? = null

    companion object {
        private val INSTANCE: RequestExecutor by lazy {
            val requestExecutor = RequestExecutor()
            requestExecutor.mExecutor = Executors.newCachedThreadPool()
            requestExecutor
        }

        fun getInstance() = INSTANCE
    }

    fun <T> executeTask(task: ITask<T>) {
        mExecutor?.submit(task)
    }
}

