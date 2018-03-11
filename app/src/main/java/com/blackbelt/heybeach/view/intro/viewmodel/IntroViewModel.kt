package com.blackbelt.heybeach.view.intro.viewmodel

import android.databinding.Bindable
import com.blackbelt.heybeach.BR
import com.blackbelt.heybeach.R
import com.blackbelt.heybeach.domain.OnDataLoadedListener
import com.blackbelt.heybeach.domain.beaches.IBeachesManager
import com.blackbelt.heybeach.domain.beaches.model.Beach
import com.blackbelt.heybeach.domain.model.ErrorModel
import com.blackbelt.heybeach.view.misc.IErrorView
import com.blackbelt.heybeach.view.misc.viewmodel.BaseViewModel
import com.blackbelt.heybeach.view.misc.viewmodel.ProgressLoader
import com.blackbelt.heybeach.widgets.AndroidItemBinder
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.io.IOException


class IntroViewModel(beachesManager: IBeachesManager) : BaseViewModel(), IErrorView, OnDataLoadedListener<List<Beach>> {

    private val mBeachesManager = beachesManager

    private val mItems = mutableListOf<BeachItemViewModel>()

    var error: Int? = null
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.errorText)
            notifyPropertyChanged(BR.errorViewVisible)
        }

    var loading: Boolean = false
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.loading)
        }

    private val mTemplates: Map<Class<*>, AndroidItemBinder> =
            hashMapOf(ProgressLoader::class.java to AndroidItemBinder(R.layout.loading_progress, BR.progressLoader),
                    BeachItemViewModel::class.java to AndroidItemBinder(R.layout.beach_item, BR.beach))

    override fun onCreate() {
        loading = true
        mBeachesManager.loadBeaches(1, this)
    }

    @Bindable
    fun getTemplates(): Map<Class<*>, AndroidItemBinder> = mTemplates

    @Bindable
    fun getBeaches(): List<Any> = mItems

    override fun onDataLoaded(data: List<Beach>) {
        data.forEach { mItems.add(BeachItemViewModel(it)) }
        launch(UI) {
            notifyPropertyChanged(BR.beaches)
            loading = false
        }
    }

    override fun onError(message: ErrorModel?, throwable: Throwable?) {
        error = when (throwable is IOException) {
            true -> R.string.connection_error
            false -> R.string.oops_something_went_wrong
        }
        loading = false
    }

    @Bindable
    override fun getErrorText(): Int? = error

    override fun reload() {
        error = null
        onCreate()
    }

    @Bindable
    override fun isErrorViewVisible(): Boolean =
            getErrorText()?.let {
                it > 0
            } ?: false

}