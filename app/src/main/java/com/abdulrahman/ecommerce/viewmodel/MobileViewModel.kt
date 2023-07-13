package com.abdulrahman.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.abdulrahman.ecommerce.paging.LaptopPagingSource
import com.abdulrahman.ecommerce.paging.MobilePagingSource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MobileViewModel @Inject constructor(private val db: FirebaseFirestore):ViewModel() {

    val mobile = Pager(PagingConfig(10)){
        MobilePagingSource(db)
    }.flow.cachedIn(viewModelScope)

}