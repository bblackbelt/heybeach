package com.blackbelt.heybeach.view.main.viewmodel

import android.databinding.Bindable
import com.blackbelt.heybeach.BR
import com.blackbelt.heybeach.R
import com.blackbelt.heybeach.domain.OnDataLoadedListener
import com.blackbelt.heybeach.domain.beaches.IBeachesManager
import com.blackbelt.heybeach.domain.beaches.model.Beach
import com.blackbelt.heybeach.domain.model.ErrorModel
import com.blackbelt.heybeach.domain.user.IUserManager
import com.blackbelt.heybeach.view.intro.viewmodel.BeachItemViewModel
import com.blackbelt.heybeach.view.misc.viewmodel.BaseViewModel
import com.blackbelt.heybeach.view.misc.viewmodel.ProgressLoader
import com.blackbelt.heybeach.widgets.AndroidItemBinder
import com.blackbelt.heybeach.widgets.PageDescriptor
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class MainViewModel constructor(userManager: IUserManager, beachesManager: IBeachesManager) : BaseViewModel() {

    private val mUserManager = userManager

    private val mBeachManager = beachesManager

    val items = mutableListOf<Any>()
        @Bindable get

    var firstLoading: Boolean = false
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.firstLoading)
        }

    private var mError = false

    private var mPageDescriptor = PageDescriptor.PageDescriptorBuilder
            .setPageSize(6)
            .setStartPage(1)
            .setThreshold(2)
            .build()

    private val mProgressLoader: ProgressLoader = ProgressLoader()

    val templates: Map<Class<*>, AndroidItemBinder> =
            hashMapOf(ProgressLoader::class.java to AndroidItemBinder(R.layout.loading_progress, BR.progressLoader),
                    BeachItemViewModel::class.java to AndroidItemBinder(R.layout.beach_item, BR.beach))

    @Bindable
    fun getNextPage() = mPageDescriptor

    fun setNextPage(pageDescriptor: PageDescriptor) {
        handleLoading(true)
        mBeachManager.loadBeaches(pageDescriptor.getCurrentPage(), object : OnDataLoadedListener<List<Beach>> {
            override fun onDataLoaded(data: List<Beach>) {
                data.forEach { items.add(BeachItemViewModel(it)) }
                launch(UI) {
                    notifyPropertyChanged(BR.items)
                }
                handleLoading(false)
            }

            override fun onError(message: ErrorModel?, throwable: Throwable?) {
            }
        })
    }

    private fun handleLoading(loading: Boolean) {
        if (mPageDescriptor.getCurrentPage() == 1) {
            firstLoading = loading
        } else {
            setLoading(loading)
        }
    }

    private fun setLoading(loading: Boolean) {
        if (loading && !items.contains(mProgressLoader)) {
            items.add(mProgressLoader)
        } else if (!loading) {
            items.remove(mProgressLoader)
        }
        notifyPropertyChanged(BR.items)
    }

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