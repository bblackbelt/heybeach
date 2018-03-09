package com.blackbelt.heybeach.view.intro.viewmodel

import android.databinding.Bindable
import com.blackbelt.heybeach.BR
import com.blackbelt.heybeach.R
import com.blackbelt.heybeach.domain.OnDataLoadedListener
import com.blackbelt.heybeach.domain.beaches.IBeachesManager
import com.blackbelt.heybeach.domain.beaches.model.Beach
import com.blackbelt.heybeach.view.misc.viewmodel.BaseViewModel
import com.blackbelt.heybeach.view.misc.viewmodel.ProgressLoader
import com.blackbelt.heybeach.widgets.AndroidItemBinder
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


class IntroViewModel(beachesManager: IBeachesManager) : BaseViewModel() {

    private val mBeachesManager = beachesManager

    private val mItems = mutableListOf<BeachItemViewModel>()

    private val mTemplates: Map<Class<*>, AndroidItemBinder> =
            hashMapOf(ProgressLoader::class.java to AndroidItemBinder(R.layout.loading_progress, BR.progressLoader),
                    BeachItemViewModel::class.java to AndroidItemBinder(R.layout.beach_item, BR.beach))

    override fun onCreate() {
        mBeachesManager.loadBeaches(1, object : OnDataLoadedListener<List<Beach>> {
            override fun onDataLoaded(data: List<Beach>) {
                data.forEach { mItems.add(BeachItemViewModel(it)) }
                launch(UI) {
                    notifyPropertyChanged(BR.beaches)
                }
            }

            override fun onError(message: String?, throwable: Throwable?) {
            }
        })
    }

    @Bindable
    fun getTemplates(): Map<Class<*>, AndroidItemBinder> = mTemplates

    @Bindable
    fun getBeaches(): List<Any> = mItems
}