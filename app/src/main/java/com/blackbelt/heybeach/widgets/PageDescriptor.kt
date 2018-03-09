package com.blackbelt.heybeach.widgets

data class PageDescriptor private constructor(private val builder: PageDescriptorBuilder) {

    private var mStartPage = 1

    private var mPageSize = 20

    private var mThreshold = 5

    private var mCurrentPage: Int

    companion object PageDescriptorBuilder {

        private var mStartPage = 1

        private var mPageSize = 20

        private var mThreshold = 5

        fun setStartPage(startPage: Int): PageDescriptorBuilder {
            mStartPage = startPage
            return this
        }

        fun setPageSize(pageSize: Int): PageDescriptorBuilder {
            mPageSize = pageSize
            return this
        }

        fun setThreshold(threshold: Int): PageDescriptorBuilder {
            mThreshold = threshold
            return this
        }

        fun build(): PageDescriptor = PageDescriptor(this)
    }

    init {
        mStartPage = builder.mStartPage
        mPageSize = builder.mPageSize
        mThreshold = builder.mThreshold
        mCurrentPage = mStartPage
    }

    fun setCurrentPage(page: Int) {
        mCurrentPage = page
    }

    fun getCurrentPage(): Int = mCurrentPage

    fun getThreshold(): Int = mThreshold

    fun getPageSize(): Int = mPageSize

    fun getStartPage(): Int = mStartPage
}