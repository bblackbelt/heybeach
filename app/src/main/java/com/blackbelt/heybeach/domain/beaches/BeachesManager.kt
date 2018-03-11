package com.blackbelt.heybeach.domain.beaches

import com.blackbelt.heybeach.data.IRequestExecutor
import com.blackbelt.heybeach.data.IResponseParser
import com.blackbelt.heybeach.data.TaskFactory
import com.blackbelt.heybeach.data.TaskListener
import com.blackbelt.heybeach.domain.OnDataLoadedListener
import com.blackbelt.heybeach.domain.beaches.model.Beach
import com.blackbelt.heybeach.domain.model.ErrorModel

interface IBeachesManager {
    fun loadBeaches(page: Int = 1, onDataLoadedListener: OnDataLoadedListener<List<Beach>>?)
}

class BeachesManager constructor(executor: IRequestExecutor, parsingParser: IResponseParser) : IBeachesManager {

    private val mRequestExecutor = executor

    private val mParsingFactory = parsingParser

    override fun loadBeaches(page: Int, onDataLoadedListener: OnDataLoadedListener<List<Beach>>?) {
        val task = TaskFactory.createBeachTask(page, object : TaskListener<String> {
            override fun onTaskCompleted(result: String) {
                val values = mParsingFactory.toBeachList(result)
                val beaches = mutableListOf<Beach>()
                values.forEach {
                    beaches.add(Beach(it.id, it.name, it.url, it.width, it.height))
                }
                onDataLoadedListener?.onDataLoaded(beaches)
            }

            override fun onTaskFailed(message: String?, throwable: Throwable?, errorCode: Int) {
                val errorResponse = mParsingFactory.toErrorResponseModel(message)
                onDataLoadedListener?.onError(ErrorModel(errorResponse.code, errorResponse.errmsg), throwable)
            }
        })
        mRequestExecutor.executeTask(task)
    }
}