package com.abdulrahman.ecommerce.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.abdulrahman.ecommerce.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class HeadphonePagingSource  (
    private val db: FirebaseFirestore
) : PagingSource<QuerySnapshot, Product>() {

    override suspend fun load(params: PagingSource.LoadParams<QuerySnapshot>): PagingSource.LoadResult<QuerySnapshot, Product> {
        return try {
            val currentPage = params.key ?: db.collection("Headphones")
                .limit(10).get().await()

            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]

            val nextPage = db.collection("Headphones")
                .limit(10).startAfter(lastDocumentSnapshot).get().await()

            PagingSource.LoadResult.Page(
                data = currentPage.toObjects(Product::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            PagingSource.LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Product>): QuerySnapshot? {
        return null
    }
}
