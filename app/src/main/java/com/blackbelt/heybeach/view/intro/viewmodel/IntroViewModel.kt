package com.blackbelt.heybeach.view.intro.viewmodel

import android.databinding.Bindable
import com.blackbelt.heybeach.BR
import com.blackbelt.heybeach.R
import com.blackbelt.heybeach.domain.OnDataLoadedListener
import com.blackbelt.heybeach.domain.beaches.IBeachesManager
import com.blackbelt.heybeach.domain.beaches.model.Beach
import com.blackbelt.heybeach.domain.model.ErrorModel
import com.blackbelt.heybeach.view.misc.viewmodel.BaseViewModel
import com.blackbelt.heybeach.view.misc.viewmodel.ProgressLoader
import com.blackbelt.heybeach.widgets.AndroidItemBinder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class IntroViewModel(beachesManager: IBeachesManager) : BaseViewModel(), OnDataLoadedListener<List<Beach>> {

    private val mBeachesManager = beachesManager

    private val mItems = mutableListOf<BeachItemViewModel>()

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
    fun getBeaches(): List<BeachItemViewModel> = mItems

    override fun onDataLoaded(data: List<Beach>) {
        data.forEach { mItems.add(BeachItemViewModel(it)) }
        GlobalScope.launch(Dispatchers.Main) {
            notifyPropertyChanged(BR.beaches)
            loading = false
        }
    }

    override fun reload() {
        super.reload()
        onCreate()
    }

    override fun onError(message: ErrorModel?, throwable: Throwable?) {
        handleError(message, throwable)
        loading = false
    }
}