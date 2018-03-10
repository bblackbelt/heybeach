package com.blackbelt.heybeach.view.main.viewmodel

import com.blackbelt.heybeach.domain.OnDataLoadedListener
import com.blackbelt.heybeach.domain.beaches.IBeachesManager
import com.blackbelt.heybeach.domain.model.ErrorModel
import com.blackbelt.heybeach.domain.user.IUserManager
import com.blackbelt.heybeach.view.misc.viewmodel.BaseViewModel

class MainViewModel constructor(userManager: IUserManager, beachesManager: IBeachesManager) : BaseViewModel() {

    private val mUserManager = userManager
    private val mBeachManager = beachesManager

    fun logOut() {
        mUserManager.logout(object : OnDataLoadedListener<Boolean> {
            override fun onDataLoaded(data: Boolean) {
                mListener?.onDataLoaded(data)
            }

            override fun onError(message: ErrorModel?, throwable: Throwable?) {
                mListener?.onError(message, throwable)
            }
        })
    }
}