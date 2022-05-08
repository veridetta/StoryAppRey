package com.example.storryapp.view.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storryapp.data.StoryRepository
import com.example.storryapp.data.network.response.ListStoryItem

class ListStoryViewModel(
    private val storyRepository: StoryRepository,
) : ViewModel() {

    fun getStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return storyRepository.getPagingStories(token).cachedIn(viewModelScope).asLiveData()
    }
}
