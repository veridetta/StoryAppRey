package com.example.storryapp.view.viewmodel

import androidx.lifecycle.*
import com.example.storryapp.data.model.UserModel
import com.example.storryapp.data.model.UserPreference
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserPreference) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}