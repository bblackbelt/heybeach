package com.blackbelt.heybeach.view.intro.viewmodel

import com.blackbelt.heybeach.data.BASE_URL
import com.blackbelt.heybeach.data.generateUrl
import com.blackbelt.heybeach.domain.beaches.model.Beach

class BeachItemViewModel(beach: Beach) {

    private val mBeach = beach

    fun getName() = mBeach.name

    fun getBeachUrl(): String {
        mBeach.url ?: return ""
        return BASE_URL.generateUrl(listOf(mBeach.url))
    }
}