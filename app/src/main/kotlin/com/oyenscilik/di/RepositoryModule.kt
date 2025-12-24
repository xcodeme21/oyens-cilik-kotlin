package com.oyenscilik.di

import com.oyenscilik.data.repository.AuthRepositoryImpl
import com.oyenscilik.data.repository.ContentRepositoryImpl
import com.oyenscilik.data.repository.PreferencesRepositoryImpl
import com.oyenscilik.data.repository.UserRepositoryImpl
import com.oyenscilik.domain.repository.AuthRepository
import com.oyenscilik.domain.repository.ContentRepository
import com.oyenscilik.domain.repository.PreferencesRepository
import com.oyenscilik.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindContentRepository(
        impl: ContentRepositoryImpl
    ): ContentRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(
        impl: PreferencesRepositoryImpl
    ): PreferencesRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}
