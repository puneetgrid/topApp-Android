package com.example.topApp.views.login

import android.widget.Toast
import com.example.topApp.R
import com.example.topApp.databinding.LoginFragmentBinding
import com.example.topApp.interfaces.DialogCallback
import com.example.topApp.utils.Utility
import com.example.topApp.views.base.BaseFragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit


class LoginFragment : BaseFragment<LoginFragmentBinding, LoginVM>() {

    override fun setLayout() = R.layout.login_fragment

    override fun setViewModel() = LoginVM::class.java
    private var mResendToken: ForceResendingToken? = null
    var verificationId = ""

    override fun bindViews(viewModel: LoginVM) {
        binding.viewModel = viewModel

        binding.otpButton.setOnClickListener {
            viewModel.isProgressShow.set(true)
            viewModel.getOtp()
        }
        bindObserver()
    }

    private fun bindObserver() {

        viewModel.successLiveData.observe(viewLifecycleOwner) {
            viewModel.isProgressShow.set(false)
            if (it != null) {
                if (it == true) {
                    sendVerificationCode()
                }
                viewModel.successLiveData.postValue(null)
            }
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            viewModel.isProgressShow.set(false)
            if (it != null) {
                when (it) {
                    Utility.errorMessage -> {
                        Utility.showDialog(requireContext(),
                            viewModel.resourceProvider.getString(R.string.app_name),
                            viewModel.errorMessage,
                            viewModel.resourceProvider.getString(R.string.okay),
                            false,
                            object : DialogCallback {
                                override fun callback(any: Any) {}
                            }
                        )
                    }
                    Utility.networkError -> {
                        Utility.networkErrorDialog(
                            requireContext(),
                            viewModel.resourceProvider.getString(R.string.app_name),
                            viewModel.resourceProvider.getString(R.string.check_internet),
                            viewModel.resourceProvider.getString(R.string.error_retry),
                            viewModel.resourceProvider.getString(R.string.cancel),
                            false,
                            object : DialogCallback {
                                override fun callback(any: Any) {
                                    if (any == 1) {
                                        viewModel.getOtp()
                                    }
                                }
                            }
                        )
                    }
                }
                viewModel.errorLiveData.postValue(null)
            }

        }
    }

    private fun sendVerificationCode() {
        // this method is used for getting
        // OTP on user phone number.
        val options = viewModel.phone.get()?.let {
            viewModel.mAuth?.let { it1 ->
                PhoneAuthOptions.newBuilder(it1)
                    .setPhoneNumber("+91" + it.trim()) // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(requireActivity()) // Activity (for callback binding)
                    .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
                    .build()
            }
        }
        options?.let { PhoneAuthProvider.verifyPhoneNumber(it) }
    }

    private val mCallBack: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            // below method is used when
            // OTP is sent from Firebase
            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                // when we receive the OTP it
                // contains a unique id which
                // we are storing in our string
                // which we have already created.
                verificationId = s
                mResendToken = forceResendingToken;
                val directions = LoginFragmentDirections.actionLoginFragmentToOtpFragment(
                    viewModel.phone.get().toString(), verificationId
                )
                navigate(R.id.loginFragment, directions = directions)
            }

            // this method is called when user
            // receive OTP from Firebase.
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                // below line is used for getting OTP code
                // which is sent in phone auth credentials.
                val code = phoneAuthCredential.smsCode
            }

            // this method is called when firebase doesn't
            // sends our OTP code due to any error or issue.
            override fun onVerificationFailed(e: FirebaseException) {
                // displaying error message with firebase exception.
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }

}