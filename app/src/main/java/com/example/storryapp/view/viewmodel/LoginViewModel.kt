package com.example.storryapp.view.viewmodel

import androidx.lifecycle.*
import com.example.storryapp.data.StoryRepository

class LoginViewModel(private val storyRepository: StoryRepository): ViewModel()  {

    fun login(email: String, pass: String) =
        storyRepository.login(email, pass)

}
