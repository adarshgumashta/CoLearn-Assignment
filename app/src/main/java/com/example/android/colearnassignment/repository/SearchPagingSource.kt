package com.example.android.colearnassignment.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android.colearnassignment.model.remote.UnSplashImage

class SearchPagingSource(private val repository: Repository, private val searchQuery: MutableMap<String, String>) :
    PagingSource<Int, UnSplashImage>() {
    override fun getRefreshKey(state: PagingState<Int, UnSplashImage>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnSplashImage> {
        return try {
            val nextPage = params.key ?: 1
            val response = repository.searchImage(searchQuery, nextPage)
            LoadResult.Page(
                response,
                if (nextPage == 1) null else nextPage - 1,
                if (nextPage < response.size) nextPage + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}