package com.blackbelt.heybeach.view.intro.viewmodel

import com.blackbelt.heybeach.domain.beaches.model.Beach

class BeachItemViewModel(beach: Beach) {

    private val mBeach = beach

    fun getName() = mBeach.name

    fun getBeachUrl() = mBeach.url
}