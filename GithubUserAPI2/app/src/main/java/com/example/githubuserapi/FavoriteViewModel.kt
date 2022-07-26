package com.example.githubuserapi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase? = UserDatabase.getDatabase(application)

    init {
        userDao = userDb?.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>?{
        return userDao?.getFavoriteUser()
    }
}