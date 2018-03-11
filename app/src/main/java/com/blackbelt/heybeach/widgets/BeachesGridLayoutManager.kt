package com.blackbelt.heybeach.widgets

import android.content.Context
import android.content.res.Resources
import android.support.v7.widget.GridLayoutManager
import com.blackbelt.heybeach.view.misc.model.ImageInfo


class BeachesGridLayoutManager(context: Context) : GridLayoutManager(context, 1) {

    private var mDataSet: MutableList<ImageInfo> = mutableListOf()

    private var mMaxWidth = 0

    private val mSpanSizeLookup: SpanSizeLookup by lazy {
        object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position >= mDataSet.size) {
                    return 0
                }

                return when (mDataSet[position].width < mMaxWidth) {
                    true -> mDataSet[position].width
                    false -> mMaxWidth
                }
            }
        }
    }

    init {
        mMaxWidth = Resources.getSystem().displayMetrics.widthPixels
        spanCount = mMaxWidth
        spanSizeLookup = mSpanSizeLookup
    }

    private fun applyAspects(imageList: List<ImageInfo>) {
        var aspectRatioSum = 0f
        var widthSum = 0
        val tempList = mutableListOf<ImageInfo>()
        for (i in imageList) {
            widthSum += i.width
            tempList.add(i)
            aspectRatioSum += i.aspectRatio
            if (widthSum > mMaxWidth) {
                val pop = tempList.removeAt(tempList.size - 1)
                aspectRatioSum -= pop.aspectRatio
                calculateImageBucket(tempList, (mMaxWidth) / aspectRatioSum)
                tempList.clear()
                tempList.add(pop)
                widthSum = pop.width
                aspectRatioSum = pop.aspectRatio
            }
        }

        calculateImageBucket(tempList, mMaxWidth / aspectRatioSum)
    }

    fun setDataSet(imageList: List<ImageInfo>) {
        mDataSet.addAll(imageList)
        applyAspects(imageList)
    }

    private fun calculateImageBucket(imageList: List<ImageInfo>, rowHeight: Float) {
        for (i in 0 until imageList.size) {
            val image = imageList[i]
            val ratio = image.aspectRatio
            image.width = (rowHeight * ratio).toInt()
            image.height = rowHeight.toInt()
        }
    }
}