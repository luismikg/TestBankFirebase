package com.luis.storibanck.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideAuthService(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    fun provideDatabaseService(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }
}