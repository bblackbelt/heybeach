package com.blackbelt.heybeach.view.intro.viewmodel

import com.blackbelt.heybeach.domain.beaches.model.Beach
import com.blackbelt.heybeach.view.misc.model.ImageInfo

class BeachItemViewModel(beach: Beach) : ImageInfo {

    override var width: Int = beach.width?.toInt() ?: 0

    override var height: Int = beach.height?.toInt() ?: 0

    override var aspectRatio: Float =
            height.toFloat() / width.toFloat()
             //Math.max(width.toFloat(), height.toFloat()) / Math.min(width.toFloat(), height.toFloat())

    private val mBeach = beach

    fun getName() = mBeach.name

    fun getBeachUrl() = mBeach.url
}