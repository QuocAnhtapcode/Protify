package com.example.hoangquocanh.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.example.hoangquocanh.MyApplication
import com.example.hoangquocanh.data.local.user.User
import com.example.hoangquocanh.data.local.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {
    private val repository: UserRepository
    init {
        val userDao = MyApplication.getInstance().database.userDAO()
        repository = UserRepository(userDao)
    }
    internal fun addUser(newUser: User){
        viewModelScope.launch (Dispatchers.IO){
            repository.addUser(newUser)
        }
    }
    internal fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }
    internal fun getUser(username: String, password: String): LiveData<User?> {
        val result = MediatorLiveData<User?>()
        val usernameSource = repository.getUserByUsername(username, password)
        val emailSource = repository.getUserByEmail(username, password)

        result.addSource(usernameSource) { user ->
            if (user != null) {
                result.value = user
                result.removeSource(usernameSource)
                result.removeSource(emailSource)
            } else {
                result.addSource(emailSource) { emailUser ->
                    result.value = emailUser
                    result.removeSource(emailSource)
                }
            }
        }
        return result
    }
}