package com.example.storryapp.view.viewmodel

import androidx.lifecycle.ViewModel
import com.example.storryapp.data.network.response.ListStoryItem

class DetailStoryViewModel: ViewModel() {
    lateinit var storyItem: ListStoryItem

    fun setDetailStory(story: ListStoryItem) : ListStoryItem {
        storyItem = story
        return storyItem
    }

}