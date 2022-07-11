package com.example.topApp.views.otp


import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.topApp.R
import com.example.topApp.databinding.OtpFragmentBinding
import com.example.topApp.interfaces.DialogCallback
import com.example.topApp.utils.Utility
import com.example.topApp.views.base.BaseFragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class OtpFragment : BaseFragment<OtpFragmentBinding, OtpVM>() {

    override fun setLayout() = R.layout.otp_fragment

    override fun setViewModel() = OtpVM::class.java

    override fun bindViews(viewModel: OtpVM) {
        binding.viewModel = viewModel
        val bundle = arguments ?: return
        val args = OtpFragmentArgs.fromBundle(bundle)
        viewModel.verificationId = args.verificationId ?: ""
        viewModel.phone = args.phone ?: ""
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvResend.setOnClickListener {
            viewModel.isProgressShow.set(true)
            resendVerificationCode()
        }
        binding.submitButton.setOnClickListener {
            viewModel.isProgressShow.set(true)
            viewModel.checkOtp()
        }
        bindObserver()
    }

    private fun bindObserver() {
        viewModel.successsLiveData.observe(viewLifecycleOwner) {
            viewModel.isProgressShow.set(false)
            if (it != null) {
                if (it == true) {
                    viewModel.otpField.get()?.let { it1 -> verifyCode(it1) }
                }
                viewModel.successsLiveData.postValue(null)
            }
        }
        viewModel.userExistLiveData.observe(viewLifecycleOwner) {
            viewModel.isProgressShow.set(false)
            if (it != null) {
                if (it == true) {
                    navigate(
                        R.id.otpFragment,
                        destinationId = R.id.action_otpFragment_to_homeFragment
                    )
                } else if (it == false) {

                    val directions =
                        OtpFragmentDirections.actionOtpFragmentToUserInfoFragment(viewModel.uid)
                    navigate(R.id.otpFragment, directions = directions)

                    viewModel.successsLiveData.postValue(null)
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
                                            viewModel.checkOtp()
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
    }

    // below method is use to verify code from Firebase.
    private fun verifyCode(code: String) {
        // below line is used for getting
        // credentials from our verification id and code.
        val credential = PhoneAuthProvider.getCredential(viewModel.verificationId, code)

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential)
    }

    private fun resendVerificationCode() {
        val options =
            viewModel.mAuth?.let { it1 ->
                PhoneAuthOptions.newBuilder(it1)
                    .setPhoneNumber("+91" + viewModel.phone.trim()) // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(requireActivity()) // Activity (for callback binding)
                    .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
                    .build()

            }
        options?.let { PhoneAuthProvider.verifyPhoneNumber(it) }
    }


    private val mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            // below method is used when
            // OTP is sent from Firebase
            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                viewModel.isProgressShow.set(false)
                viewModel.verificationId = s
            }

            // this method is called when user
            // receive OTP from Firebase.
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                // below line is used for getting OTP code
                // which is sent in phone auth credentials.
                val code = phoneAuthCredential.smsCode
                viewModel.isProgressShow.set(false)
            }

            // this method is called when firebase doesn't
            // sends our OTP code due to any error or issue.
            override fun onVerificationFailed(e: FirebaseException) {
                // displaying error message with firebase exception.
                viewModel.isProgressShow.set(false)
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        viewModel.mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // if the code is correct and the task is successful
                    // we are sending our user to new activity.
                    val user = task.result.user
                    if (user != null) {
                        Utility.printLog("user", user)
                    }
                    user?.let { viewModel.uid = it.uid }
                    viewModel.isProgressShow.set(true)
                    viewModel.checkUser()
                } else {
                    // if the code is not correct then we are
                    // displaying an error message to the user.
                    Toast.makeText(
                        requireContext(),
                        task.exception?.message,
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
    }

}