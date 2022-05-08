package com.example.storryapp.view.viewmodel

import androidx.lifecycle.*
import com.example.storryapp.data.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun register(name: String, email: String, pass: String) =
        storyRepository.register(name,email, pass)

}