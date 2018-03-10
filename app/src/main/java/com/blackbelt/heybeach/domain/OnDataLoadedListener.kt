package com.blackbelt.heybeach.domain

import com.blackbelt.heybeach.domain.model.ErrorModel

interface OnDataLoadedListener<T> {
    fun onDataLoaded(data: T)
    fun onError(message: ErrorModel?, throwable: Throwable?)
}