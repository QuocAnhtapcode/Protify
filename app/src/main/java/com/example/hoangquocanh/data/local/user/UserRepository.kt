package com.example.hoangquocanh.data.local.user

import android.content.Context
import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {
    fun addUser(newUser: User){
        userDao.addUser(newUser)
    }
    fun getUserByUsername(username: String, password: String): LiveData<User?>{
        return userDao.getUserByUsername(username,password)
    }
    fun getUserByEmail(email: String, password: String): LiveData<User?>{
        return userDao.getUserByEmail(email,password)
    }
    fun updateUser(user: User){
        userDao.updateUser(user)
    }
}
fun getUserInfo(context: Context): User? {
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getLong("userId", 0)
    val userAvatar = sharedPreferences.getString("userAvatar", null)
    val userName = sharedPreferences.getString("userName", null)
    val userEmail = sharedPreferences.getString("userEmail", null)
    val userPhoneNumber = sharedPreferences.getString("userPhoneNumber", null)
    val userPassword = sharedPreferences.getString("userPassword", null)
    return if (userAvatar != null && userName != null
        && userEmail != null && userPhoneNumber != null && userPassword != null) {
        User(userId, userAvatar, userName, userEmail, userPhoneNumber, userPassword)
    } else {
        null
    }
}

fun clearUserInfo(context: Context) {
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.clear()
    editor.apply()
}
