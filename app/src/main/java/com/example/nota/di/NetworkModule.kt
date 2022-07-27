package com.example.nota.di

import com.example.nota.api.UserApi
import com.example.nota.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit():Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL).build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit:Retrofit):UserApi{
        return retrofit.create(UserApi::class.java)

    }
}