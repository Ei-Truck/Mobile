package com.example.eitruck.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eitruck.model.User
import com.example.eitruck.data.remote.repository.postgres.UserRepository
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private var repository: UserRepository? = null

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun setToken(token: String) {
        repository = UserRepository(token)
    }

    fun getUser(id: Int) {
        viewModelScope.launch {
            try {
                repository?.let {
                    val response = it.getUser(id)
                    _user.value = response
                } ?: throw IllegalStateException("Token não definido!")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}