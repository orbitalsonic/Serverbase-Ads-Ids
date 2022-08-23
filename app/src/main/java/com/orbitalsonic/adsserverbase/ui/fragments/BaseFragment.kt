package com.orbitalsonic.adsserverbase.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.orbitalsonic.adsserverbase.helpers.di.DIComponent


open class BaseFragment<T : ViewDataBinding> : Fragment() {

    var hasInitializedRootView = false
    private var rootView: View? = null
    lateinit var binding: T
    val diComponent = DIComponent()

    fun getPersistentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
        layout: Int
    ): View? {
        if (rootView == null) {
            binding = DataBindingUtil.inflate(inflater, layout, container, false)
            rootView = binding.root
        } else {

            (rootView?.parent as? ViewGroup)?.removeView(rootView)
        }

        return rootView
    }

}