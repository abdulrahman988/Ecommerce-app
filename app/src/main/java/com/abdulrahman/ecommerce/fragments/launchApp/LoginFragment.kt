package com.abdulrahman.ecommerce.fragments.launchApp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abdulrahman.ecommerce.R
import com.abdulrahman.ecommerce.activities.ShoppingActivity
import com.abdulrahman.ecommerce.databinding.FragmentLoginBinding
import com.abdulrahman.ecommerce.util.BottomSheetCallback
import com.abdulrahman.ecommerce.util.LoginRegisterValidationResult
import com.abdulrahman.ecommerce.util.Resource
import com.abdulrahman.ecommerce.util.showBottomSheetDialog
import com.abdulrahman.ecommerce.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : Fragment(), BottomSheetCallback {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonLoginLoginFragment.setOnClickListener {
                val email = etEmailLogin.text.toString()
                val password = EtPasswordLogin.text.toString()

                viewModel.loginAccountWithEmailAndPassword(email, password)
            }
        }

        binding.tvForgetPasswordLogin.setOnClickListener {
            showBottomSheetDialog(this)

        }

        lifecycleScope.launch {
            viewModel.login.collect {
                when (it) {
                    is Resource.Loading -> {
//                        Toast.makeText(binding.root.context, "Loading", Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Success -> {
                        startActivity(Intent(requireActivity(), ShoppingActivity::class.java))
                        Toast.makeText(binding.root.context, "Loading", Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Error -> {
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.emailValidation.collect {
                when (it) {
                    is LoginRegisterValidationResult.EmailError -> {
                        binding.etEmailLogin.apply {
                            requestFocus()
                            error = it.errorMessage
                        }
                    }

                    else -> Unit
                }
            }
        }
        lifecycleScope.launch {
            viewModel.passwordValidation.collect {
                when (it) {
                    is LoginRegisterValidationResult.PasswordError -> {
                        binding.EtPasswordLogin.apply {
                            requestFocus()
                            error = it.errorMessage
                        }
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.reset.collect{
                when (it) {
                    is Resource.Success -> {
                        Toast.makeText(binding.root.context, "Loading", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

        binding.tvToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


    }

    companion object {
        const val TAG = "login_fragment"
    }

    override fun onDataReceived(data: String) {
        viewModel.resetPassword(data)
    }

}
