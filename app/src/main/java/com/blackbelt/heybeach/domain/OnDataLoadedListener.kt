package com.blackbelt.heybeach.domain

interface OnDataLoadedListener<T> {
    fun onDataLoaded(data: T)
    fun onError(message: String?, throwable: Throwable?)
}