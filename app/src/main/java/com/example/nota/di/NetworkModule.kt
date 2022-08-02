package com.example.nota.di

import com.example.nota.api.AuthInterceptor
import com.example.nota.api.NotesApi
import com.example.nota.api.UserApi
import com.example.nota.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitBuilder():Retrofit.Builder{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofitBuilder:Retrofit.Builder):UserApi{
        return retrofitBuilder.build().create(UserApi::class.java)

    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor:AuthInterceptor):OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideNotesApi(retrofitBuilder:Retrofit.Builder,okHttpClient: OkHttpClient):NotesApi{
        return retrofitBuilder.client(okHttpClient).build().create(NotesApi::class.java)
    }
}