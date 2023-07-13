package com.abdulrahman.ecommerce.fragments.launchApp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abdulrahman.ecommerce.R
import com.abdulrahman.ecommerce.data.User
import com.abdulrahman.ecommerce.databinding.FragmentRegisterBinding
import com.abdulrahman.ecommerce.util.LoginRegisterValidationResult
import com.abdulrahman.ecommerce.util.Resource
import com.abdulrahman.ecommerce.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import android.view.View as View1

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()
    private lateinit var user: User
    private lateinit var password: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View1 {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View1, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonRegisterRegister.setOnClickListener {
            user = User(
                binding.etFirstNameRegister.text.toString().trim(),
                binding.etLastNameRegister.text.toString().trim(),
                binding.etEmailRegister.text.toString().trim()
            )
            password = binding.etPasswordRegister.text.toString()
            viewModel.createAccountWithEmailAndPassword(user, password)
        }

        lifecycleScope.launch {
            viewModel.emailValidation.collect {
                when (it) {
                    is LoginRegisterValidationResult.EmailError -> {
                        binding.etEmailRegister.apply {
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
                        binding.etPasswordRegister.apply {
                            requestFocus()
                            error = it.errorMessage
                        }
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.register.collect {
                when (it) {
                    is Resource.Loading -> {
                        Toast.makeText(binding.root.context, "Loading", Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Success -> {
                        Toast.makeText(binding.root.context, "Success", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, it.data?.email.toString())

                    }

                    is Resource.Error -> {
                        Toast.makeText(binding.root.context, "Error", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, it.message.toString())
                    }

                    else -> Unit
                }
            }
        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

        }

    }

    companion object {
        const val TAG = "register_fragment"
    }
}