package test.login.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceClient {
    companion object{
        fun build() : Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://api.snaptig.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}