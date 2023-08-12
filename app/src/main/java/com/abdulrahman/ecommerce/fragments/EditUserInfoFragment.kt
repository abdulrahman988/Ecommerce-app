package com.abdulrahman.ecommerce.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abdulrahman.ecommerce.databinding.FragmentEditUserInfoBinding
import com.abdulrahman.ecommerce.util.Resource
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditUserInfoFragment : Fragment() {

    private val viewModel by viewModels<EditProfileViewModel>()
    private lateinit var binding: FragmentEditUserInfoBinding



    private var selectedImage = Uri.EMPTY


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditUserInfoBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.profileImg.collect {
                when (it) {
                    is Resource.Success -> {
                        if (it.data?.isNotEmpty() == true){
                            Glide.with(this@EditUserInfoFragment).load(it.data).into(binding.imgUser)
                        }
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
                        binding.edFirstName.setText(it.data)
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
                        binding.edLastName.setText(it.data)
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
                        binding.edEmail.setText(it.data)
                    }
                    is Resource.Error -> {
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        binding.ivCloseCart.setOnClickListener {
            findNavController().navigateUp()
        }



        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    val imageUri = data?.data!!
                    selectedImage = imageUri
                }

            }


        binding.imgEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.type = "image/*"
            resultLauncher.launch(intent)
        }



    binding.apply {
        btnSaveProfile.setOnClickListener {
            val firstName = edFirstName.text.toString()
            val lastName = edLastName.text.toString()
            viewModel.saveUserInfo(firstName, lastName, selectedImage)
        }

    }









    }

}