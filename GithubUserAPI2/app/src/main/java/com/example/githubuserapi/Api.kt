package com.example.githubuserapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    @Headers("Authorization: token " + BuildConfig.GithubAPI)
    fun getSearchUser(
        @Query("q") query: String
    ): Call<UserRespond>

    @GET("users/{username}")
    @Headers("Authorization: token " + BuildConfig.GithubAPI)
    fun getUserDetail(
        @Path("username") username :String
    ): Call<DetailUserRespond>

    @GET("users/{username}/followers")
    @Headers("Authorization: token " + BuildConfig.GithubAPI)
    fun getFollowers(
        @Path("username") username :String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token " + BuildConfig.GithubAPI)
    fun getFollowing(
        @Path("username") username :String
    ): Call<ArrayList<User>>
}