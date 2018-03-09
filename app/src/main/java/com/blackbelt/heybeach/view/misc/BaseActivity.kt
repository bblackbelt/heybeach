package com.blackbelt.heybeach.view.misc

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import com.blackbelt.heybeach.view.misc.viewmodel.BaseViewModel

abstract class BaseActivity : AppCompatActivity() {

    var mViewModel: BaseViewModel? = null

    fun setContentView(@LayoutRes layoutId: Int, brVariable: Int, viewModel: BaseViewModel) {
        mViewModel = viewModel
        val dataBiding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, layoutId, null, false)
        dataBiding.setVariable(brVariable, viewModel)
        super.setContentView(dataBiding.root)

        mViewModel?.onCreate()
    }

    override fun onStart() {
        super.onStart()
        mViewModel?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mViewModel?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel?.onDestroy()
    }
}