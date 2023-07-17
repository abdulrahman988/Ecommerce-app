package com.abdulrahman.ecommerce.fragments.launchApp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.abdulrahman.ecommerce.R
import com.abdulrahman.ecommerce.activities.ShoppingActivity
import com.abdulrahman.ecommerce.databinding.FragmentIntroductionBinding
import com.google.firebase.auth.FirebaseAuth

class IntroductionFragment : Fragment() {
    private lateinit var binding: FragmentIntroductionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val firebaseAuth = FirebaseAuth.getInstance()

        binding.buttonIntroduction.setOnClickListener {
            if (firebaseAuth.currentUser != null) {
                startActivity(Intent(requireActivity(), ShoppingActivity::class.java))
            } else {
                findNavController().navigate(R.id.action_introductionFragment_to_acountOptionsFragment)

            }
//            findNavController().navigate(R.id.action_introductionFragment_to_acountOptionsFragment)

        }

    }
}
