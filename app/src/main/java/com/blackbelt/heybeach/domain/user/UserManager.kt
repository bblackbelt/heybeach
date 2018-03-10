package com.blackbelt.heybeach.domain.user

import android.content.SharedPreferences
import com.blackbelt.heybeach.data.RequestExecutor
import com.blackbelt.heybeach.data.ResponseParser
import com.blackbelt.heybeach.data.TaskFactory
import com.blackbelt.heybeach.data.TaskListener
import com.blackbelt.heybeach.data.model.SignUpRequestModel
import com.blackbelt.heybeach.domain.OnDataLoadedListener
import com.blackbelt.heybeach.domain.model.ErrorModel
import com.blackbelt.heybeach.domain.user.model.SignUpModel
import com.blackbelt.heybeach.domain.user.model.UserModel

interface IUserManager {

    fun signUp(email: String, password: String, listener: OnDataLoadedListener<SignUpModel>?)

    fun signIn(email: String, password: String, listener: OnDataLoadedListener<SignUpModel>?)

    fun logout(listener: OnDataLoadedListener<Boolean>? = null)

    fun getAuthToken(): String

    fun getUser(listener: OnDataLoadedListener<UserModel>?)
}

private const val X_AUTH_TOKEN_KEY = "X_AUTH_TOKEN_KEY"

class UserManager constructor(executor: RequestExecutor, parsingFactory: ResponseParser, sharedPreferences: SharedPreferences) : IUserManager {

    private val mRequestExecutor = executor

    private val mParsingFactory = parsingFactory

    private val mSharedPreferences = sharedPreferences

    override fun signUp(email: String, password: String, listener: OnDataLoadedListener<SignUpModel>?) {
        mRequestExecutor.executeTask(TaskFactory.createSignUpTask(SignUpRequestModel(email, password), object : TaskListener<String> {
            override fun onTaskCompleted(result: String, token: String?) {
                saveAuthToken(token)
                val responseModel = mParsingFactory.toSignupResonseModel(result)
                listener?.onDataLoaded(SignUpModel(responseModel.id, responseModel.email))
            }

            override fun onTaskFailed(message: String?, throwable: Throwable?, errorCode: Int) {
                val errorResponse = mParsingFactory.toErrorResponseModel(message)
                listener?.onError(ErrorModel(errorResponse.code ?: errorCode, errorResponse.errmsg), throwable)
            }
        }))
    }

    fun saveAuthToken(token: String?) {
        if (token == null) {
            mSharedPreferences.edit().remove(X_AUTH_TOKEN_KEY).apply()
        } else {
            mSharedPreferences.edit().putString(X_AUTH_TOKEN_KEY, token).apply()
        }
    }

    override fun getAuthToken(): String = mSharedPreferences.getString(X_AUTH_TOKEN_KEY, "")

    override fun signIn(email: String, password: String, listener: OnDataLoadedListener<SignUpModel>?) {
        mRequestExecutor.executeTask(TaskFactory.createSignInTask(SignUpRequestModel(email, password), object : TaskListener<String> {
            override fun onTaskCompleted(result: String, token: String?) {
                saveAuthToken(token)
                val responseModel = mParsingFactory.toSignupResonseModel(result)
                listener?.onDataLoaded(SignUpModel(responseModel.id, responseModel.email))
            }

            override fun onTaskFailed(message: String?, throwable: Throwable?, errorCode: Int) {
                val errorResponse = mParsingFactory.toErrorResponseModel(message)
                listener?.onError(ErrorModel(errorResponse.code ?: errorCode, errorResponse.errmsg), throwable)
            }
        }))
    }

    override fun logout(listener: OnDataLoadedListener<Boolean>?) {
        val token = mSharedPreferences.getString(X_AUTH_TOKEN_KEY, "")
        saveAuthToken(null)
        mRequestExecutor.executeTask(TaskFactory.createLogoutTask(object : TaskListener<String> {
            override fun onTaskCompleted(result: String) {
                listener?.onDataLoaded(true)
            }

            override fun onTaskFailed(message: String?, throwable: Throwable?, errorCode: Int) {
                val errorResponse = mParsingFactory.toErrorResponseModel(message)
                listener?.onError(ErrorModel(errorResponse.code ?: errorCode, errorResponse.errmsg), throwable)
            }
        }, token))
    }

    override fun getUser(listener: OnDataLoadedListener<UserModel>?) {
        mRequestExecutor.executeTask(TaskFactory.createUserTask(object : TaskListener<String> {
            override fun onTaskCompleted(result: String) {
                val model = mParsingFactory.toUserResponseMode(result)
                listener?.onDataLoaded(UserModel(model.id, model.email))
            }

            override fun onTaskFailed(message: String?, throwable: Throwable?, errorCode: Int) {
                val errorResponse = mParsingFactory.toErrorResponseModel(message)
                listener?.onError(ErrorModel(errorResponse.code ?: errorCode, errorResponse.errmsg), throwable)
            }
        }, getAuthToken()))
    }
}