package com.example.githubuserapi

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserMainModel(application: Application) : AndroidViewModel(application) {
    val user = MutableLiveData<DetailUserRespond>()

    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase? = UserDatabase.getDatabase(application)

    init {
        userDao = userDb?.favoriteUserDao()
    }

    fun setUserDetail(username : String){
        RetrofitClient.apiInstance
            .getUserDetail(username)
            .enqueue(object : Callback<DetailUserRespond>{
                override fun onResponse(
                    call: Call<DetailUserRespond>,
                    response: Response<DetailUserRespond>
                ) {
                    if (response.isSuccessful){
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailUserRespond>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    fun getUserDetail(): LiveData<DetailUserRespond>{
        return user
    }

    fun addToFavorite(username: String, id : Int, avatar: String){
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                username,
                id,
                avatar
            )
            userDao?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFromFavorite(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(id)
        }
    }
}