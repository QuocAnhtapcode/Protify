package com.example.hoangquocanh.data.local.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao{
    @Insert
    fun addUser(newUser: User)
    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    fun getUserByUsername(username: String, password: String): LiveData<User?>
    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    fun getUserByEmail(email: String, password: String): LiveData<User?>

    @Update
    fun updateUser(vararg user: User)
}
