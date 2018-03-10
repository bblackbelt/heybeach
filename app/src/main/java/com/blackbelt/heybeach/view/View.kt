package com.blackbelt.heybeach.view

import com.blackbelt.heybeach.domain.model.ErrorModel

interface View<T> {
    fun onDataLoaded(data: T)
    fun onError(message: ErrorModel?, throwable: Throwable?, resId: Int = -1)
}