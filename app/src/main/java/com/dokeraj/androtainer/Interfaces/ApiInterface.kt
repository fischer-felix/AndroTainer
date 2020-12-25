package com.dokeraj.androtainer.Interfaces

import com.dokeraj.androtainer.models.Jwt
import com.dokeraj.androtainer.models.PContainersResponse
import com.dokeraj.androtainer.models.UserCredentials
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @GET
    fun listDockerContainers(
        @Header("Authorization") jwt: String?,
        @Url fullPath: String,
        @Query("all") paramAll: Int,
    ): Call<PContainersResponse>

    //@Headers("Content-Type: application/json")
    @POST
    fun loginRequest(@Body userData: UserCredentials, @Url fullPath: String): Call<Jwt>

}