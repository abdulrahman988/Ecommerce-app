package com.abdulrahman.ecommerce.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdulrahman.ecommerce.R
import com.abdulrahman.ecommerce.databinding.FragmentHeadPhoneCategoryBinding
import com.abdulrahman.ecommerce.databinding.FragmentMainCategoryBinding
import com.abdulrahman.ecommerce.paging.PagingAdapter
import com.abdulrahman.ecommerce.viewmodel.HeadphoneViewModel
import com.abdulrahman.ecommerce.viewmodel.LaptopViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HeadPhoneCategoryFragment : Fragment() {
    private lateinit var binding: FragmentHeadPhoneCategoryBinding
    private val viewModel by viewModels<HeadphoneViewModel>()
    private lateinit var headphoneAdapter: PagingAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHeadPhoneCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProductRecyclerView()

        lifecycleScope.launch {
            viewModel.headphone.collect {
                headphoneAdapter.submitData(it)
            }
        }
    }


    private fun setupProductRecyclerView() {
        headphoneAdapter = PagingAdapter(PagingAdapter.OnClickListener{
            val bundle = Bundle().apply {
                putParcelable("product", it)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_productDetailsFragment,
                bundle
            )
        })
        binding.rvHeadphone.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = headphoneAdapter
        }
    }
}