package com.blackbelt.heybeach.data

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

interface IRequestExecutor {
    fun <T> executeTask(task: ITask<T>)
}

class RequestExecutor : IRequestExecutor {

    private val mExecutor: ExecutorService  by lazy {
        Executors.newFixedThreadPool(1)
    }

    override fun <T> executeTask(task: ITask<T>) {
        mExecutor.submit(task)
    }
}

