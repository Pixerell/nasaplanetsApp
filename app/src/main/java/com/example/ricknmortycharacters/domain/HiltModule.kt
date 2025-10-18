package com.example.ricknmortycharacters.domain

import android.content.Context
import com.example.ricknmortycharacters.data.api.RetrofitClient
import com.example.ricknmortycharacters.data.api.RickAndMortyApi
import com.example.ricknmortycharacters.data.db.CharacterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRickAndMortyApi(): RickAndMortyApi {
        return RetrofitClient.api
    }

    @Provides
    @Singleton
    fun provideCharactersRepository(
        api: RickAndMortyApi,
        dao: CharacterDao
    ): CharactersRepository {
        return CharactersRepository(api, dao)
    }

    @Provides
    @Singleton
    fun provideNetworkUtils(@ApplicationContext context: Context): NetworkUtils {
        return NetworkUtils(context)
    }
}