package com.abdulrahman.ecommerce.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abdulrahman.ecommerce.R
import com.abdulrahman.ecommerce.databinding.FragmentAddressSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressSettingFragment : Fragment() {

    private lateinit var binding: FragmentAddressSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


    }

}