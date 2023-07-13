package com.abdulrahman.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSourceFactory
import androidx.paging.cachedIn
import com.abdulrahman.ecommerce.data.Product
import com.abdulrahman.ecommerce.paging.LaptopPagingSource
import com.abdulrahman.ecommerce.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaptopViewModel @Inject constructor(
    private val db: FirebaseFirestore,
) : ViewModel() {

     val laptop = Pager(PagingConfig(10)){
        LaptopPagingSource(db)
    }.flow.cachedIn(viewModelScope)


}

