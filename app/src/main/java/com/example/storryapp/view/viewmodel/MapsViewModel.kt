package com.example.storryapp.view.viewmodel

import androidx.lifecycle.ViewModel
import com.example.storryapp.data.StoryRepository

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel(){
    fun getStories(token: String) = storyRepository.getStoryMap(token)
}