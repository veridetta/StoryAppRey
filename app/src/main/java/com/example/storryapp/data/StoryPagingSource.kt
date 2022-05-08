package com.example.storryapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storryapp.data.model.UserPreference
import com.example.storryapp.data.network.response.StoryResponseItem
import com.example.storryapp.data.network.retrofit.ApiService
import kotlinx.coroutines.flow.first

//class StoryPagingSource (private val userPreference: UserPreference, private val apiService: ApiService): PagingSource<Int, StoryResponseItem>() {
//    private companion object {
//        const val INITIAL_PAGE_INDEX = 1
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponseItem> {
//        return try {
//            val page = params.key ?: INITIAL_PAGE_INDEX
//            val token: String = userPreference.getUser().first().token
//            val responseData = apiService.getAllStories("Bearer $token", page, params.loadSize)
//
//            LoadResult.Page(
//                data = responseData.itemStory,
//                prevKey = if (page == 1) null else page - 1,
//                nextKey = if (responseData.itemStory.isNullOrEmpty()) null else page + 1
//            )
//        } catch (exception: Exception) {
//            return LoadResult.Error(exception)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, StoryResponseItem>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//}