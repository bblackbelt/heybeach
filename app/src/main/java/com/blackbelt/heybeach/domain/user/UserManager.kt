package com.blackbelt.heybeach.domain.user

import com.blackbelt.heybeach.data.RequestExecutor
import com.blackbelt.heybeach.data.ResponseParser
import com.blackbelt.heybeach.data.TaskFactory
import com.blackbelt.heybeach.data.TaskListener
import com.blackbelt.heybeach.data.model.SignUpRequestModel
import com.blackbelt.heybeach.domain.OnDataLoadedListener
import com.blackbelt.heybeach.domain.model.ErrorModel
import com.blackbelt.heybeach.domain.user.model.SignUpModel

interface IUserManager {

    fun signUp(email: String, password: String, listener: OnDataLoadedListener<SignUpModel>?)
}

class UserManager constructor(executor: RequestExecutor, parsingFactory: ResponseParser) : IUserManager {

    private val mRequestExecutor = executor

    private val mParsingFactory = parsingFactory

    override fun signUp(email: String, password: String, listener: OnDataLoadedListener<SignUpModel>?) {
        
        mRequestExecutor.executeTask(TaskFactory.createSignUpTask(SignUpRequestModel(email, password), object : TaskListener<String> {
            override fun onTaskCompleted(result: String) {
                val responseModel = mParsingFactory.toSignupResonseModel(result)
                listener?.onDataLoaded(SignUpModel(responseModel.id, responseModel.email))
            }

            override fun onTaskFailed(message: String?, throwable: Throwable?) {
                val errorResponse = mParsingFactory.toErrorResponseModel(message)
                listener?.onError(ErrorModel(errorResponse.code, errorResponse.errmsg), throwable)
            }
        }))
    }

}