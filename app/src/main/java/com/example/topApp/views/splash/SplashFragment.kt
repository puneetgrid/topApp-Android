package com.example.topApp.views.splash

import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.topApp.R
import com.example.topApp.databinding.SplashFragmentBinding
import com.example.topApp.views.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : BaseFragment<SplashFragmentBinding, SplashVM>() {

    override fun setLayout() = R.layout.splash_fragment

    override fun setViewModel() = SplashVM::class.java

    override fun bindViews(viewModel: SplashVM) {
        binding.viewModel = viewModel
        checkUsersLoginStatusAndNavigate()
    }

    var gotoNextScreenJob: Job? = null
    private fun checkUsersLoginStatusAndNavigate() {
        gotoNextScreenJob?.cancel()
        gotoNextScreenJob = lifecycleScope.launch(Dispatchers.Main) {
            delay(1500)
            val currentUser: FirebaseUser? = viewModel.mAuth?.getCurrentUser()
            if(currentUser!=null){
                navigate(R.id.splashFragment,destinationId=R.id.action_splashFragment_to_homeFragment)
            }else{
                navigate(R.id.splashFragment,destinationId=R.id.action_splashFragment_to_loginFragment)

            }

        }
    }
}