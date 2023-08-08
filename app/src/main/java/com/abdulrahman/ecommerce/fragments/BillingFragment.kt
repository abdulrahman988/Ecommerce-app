package com.abdulrahman.ecommerce.fragments

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdulrahman.ecommerce.adapters.AddressRecyclerViewAdapter
import com.abdulrahman.ecommerce.adapters.CheckoutProductRecyclerViewAdapter
import com.abdulrahman.ecommerce.databinding.FragmentBillingBinding
import com.abdulrahman.ecommerce.payment.NetworkPayment
import com.abdulrahman.ecommerce.util.Constants
import com.abdulrahman.ecommerce.util.Resource
import com.abdulrahman.ecommerce.viewmodel.BillingViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BillingFragment : Fragment() {
    private lateinit var binding: FragmentBillingBinding
    private val viewModel by viewModels<BillingViewModel>()
    private lateinit var addressAdapter: AddressRecyclerViewAdapter
    private lateinit var checkoutProductsAdapter: CheckoutProductRecyclerViewAdapter

    private lateinit var paymentSheet: PaymentSheet


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        PaymentConfiguration.init(requireContext(), Constants.PUBLISHABLE_KEY)
        paymentSheet = PaymentSheet(this) {
            onPaymentSheetResult(it)
        }


        // Inflate the layout for this fragment
        binding = FragmentBillingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAddressRecyclerView()
        setupCheckoutProductsRecyclerView()


        binding.btnPlaceOlder.setOnClickListener {
            paymentFlow()
        }

        lifecycleScope.launch {
            viewModel.productPrice.collect {
                it?.let {
                    NetworkPayment.startPaymentFlow(requireContext(), it)
                    binding.tvTotalprice.text = "$ ${String.format("%.2f", (it))}"
                }
            }
        }




        lifecycleScope.launch {
            viewModel.address.collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let { it1 -> addressAdapter.submitList(it1) }
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                            .show()

                    }

                    else -> {}
                }
            }
        }
        lifecycleScope.launch {
            viewModel.product.collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let { it1 -> checkoutProductsAdapter.submitList(it1) }
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setupAddressRecyclerView() {
        addressAdapter = AddressRecyclerViewAdapter()
        binding.rvAdresses.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = addressAdapter
        }
    }


    private fun setupCheckoutProductsRecyclerView() {
        checkoutProductsAdapter = CheckoutProductRecyclerViewAdapter()
        binding.rvProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = checkoutProductsAdapter
        }
    }


    private fun onPaymentSheetResult(it: PaymentSheetResult) {
        if (it is PaymentSheetResult.Completed) {
            Toast.makeText(requireContext(), "Payment Success", Toast.LENGTH_SHORT).show()
        }
    }

    private fun paymentFlow() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(3000)
            paymentSheet.presentWithPaymentIntent(
                NetworkPayment.clientSecret, PaymentSheet.Configuration(
                    "asdasd",
                    PaymentSheet.CustomerConfiguration(
                        NetworkPayment.customerId,
                        NetworkPayment.ephemeralKey
                    )
                )
            )
        }
    }
}


























