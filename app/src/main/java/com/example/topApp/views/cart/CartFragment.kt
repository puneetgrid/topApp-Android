package com.example.topApp.views.cart

import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.topApp.R
import com.example.topApp.databinding.CartFragmentBinding
import com.example.topApp.databinding.SplashFragmentBinding
import com.example.topApp.views.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CartFragment : BaseFragment<CartFragmentBinding, CartVM>() {

    override fun setLayout() = R.layout.cart_fragment

    override fun setViewModel() = CartVM::class.java

    override fun bindViews(viewModel: CartVM) {
        binding.viewModel = viewModel

    }
}