package com.example.storryapp.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storryapp.data.StoryRepository
import com.example.storryapp.data.network.response.AddNewStoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    val responseUpload : LiveData<AddNewStoriesResponse> = storyRepository.responseUpload
     fun postStory(
        token: String,
        description: String,
        imgFile: File,
        lat : Float,
        lon : Float
    ) {
        storyRepository.postStory(token, description, imgFile, lat ,lon)
    }
}


