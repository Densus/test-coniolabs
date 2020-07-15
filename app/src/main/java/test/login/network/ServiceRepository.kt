package test.login.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import test.login.model.Auth
import test.login.model.LoginUser

interface ServiceRepository {
    @POST("api/v1/auth/signin")
    fun login(
        @Body request: LoginUser
    ): Call<Auth>
}