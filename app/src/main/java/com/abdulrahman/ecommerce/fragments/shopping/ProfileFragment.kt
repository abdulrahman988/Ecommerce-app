package com.abdulrahman.ecommerce.fragments.shopping

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abdulrahman.ecommerce.R
import com.abdulrahman.ecommerce.activities.LoginRegisterActivity
import com.abdulrahman.ecommerce.databinding.FragmentProfileBinding
import com.abdulrahman.ecommerce.util.Resource
import com.abdulrahman.ecommerce.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProfileInfo()

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
//            startActivity(Intent(requireContext(), LoginRegisterActivity::class.java))

            val direction = ProfileFragmentDirections.actionProfileFragmentToIntroductionFragment2()
            findNavController().navigate(direction)

        }


        lifecycleScope.launch {
            viewModel.profileImg.collect {
                when (it) {
                    is Resource.Success -> {
                        Glide.with(this@ProfileFragment).load(it.data).into(binding.ivProfilePicture)
                    }
                    is Resource.Error -> {
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }


        lifecycleScope.launch {
            viewModel.firstName.collect {
                when (it) {
                    is Resource.Success -> {
                        binding.tvFirstName.text = it.data.toString()
                    }
                    is Resource.Error -> {
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }


        lifecycleScope.launch {
            viewModel.lastName.collect {
                when (it) {
                    is Resource.Success -> {
                        binding.tvLastName.text = it.data.toString()
                    }
                    is Resource.Error -> {
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }


        lifecycleScope.launch {
            viewModel.mail.collect {
                when (it) {
                    is Resource.Success -> {
                        binding.tvEmail.text = it.data.toString()
                    }
                    is Resource.Error -> {
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }





    }
}
