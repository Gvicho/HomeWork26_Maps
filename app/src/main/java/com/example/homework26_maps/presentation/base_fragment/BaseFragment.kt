package com.example.homework26_maps.presentation.base_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding



typealias Inflater<VB> = (LayoutInflater, ViewGroup?, Boolean) -> VB  // lambda function

abstract class BaseFragment<VB: ViewBinding>(private val inflate : Inflater<VB>): Fragment() {

    // This property is only valid between onCreateView and onDestroyView.
    private var _binding: VB? = null

    // binding will always get the value from _binding each time it gets accessed, so same for this too
    // binding doesn't hold any referance to _binding, each time we access binding it retreavs value from _binding
    val binding: VB get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        bindViewActionListeners()
        bindObservers()
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun loadData(){}

    open fun bind(){}
    open fun initData(savedInstanceState: Bundle?){}
    open fun bindViewActionListeners(){}
    open fun bindObservers(){}
}