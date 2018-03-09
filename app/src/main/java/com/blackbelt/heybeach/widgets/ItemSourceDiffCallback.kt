package com.blackbelt.heybeach.widgets

import android.support.v7.util.DiffUtil

class ItemSourceDiffCallback(private val mOldList: List<*>?, private val mNewList: List<*>?) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldList?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return mNewList?.size ?: 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return !(mOldList == null || mNewList == null) && mOldList[oldItemPosition] == mNewList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return !(mOldList == null || mNewList == null) && mOldList[oldItemPosition] == mNewList[newItemPosition]
    }
}