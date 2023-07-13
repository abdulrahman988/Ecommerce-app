package com.abdulrahman.ecommerce.fragments.launchApp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.abdulrahman.ecommerce.R
import com.abdulrahman.ecommerce.databinding.FragmentAcountOptionsBinding


class AccountOptionsFragment : Fragment() {
    private lateinit var binding: FragmentAcountOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAcountOptionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegisterAccountOptionsFragment.setOnClickListener {
            findNavController().navigate(R.id.action_acountOptionsFragment_to_registerFragment)

        }
        binding.buttonLoginAccountOptionsFragment.setOnClickListener {
            findNavController().navigate(R.id.action_acountOptionsFragment_to_loginFragment)

        }

    }
}