package com.luis.storibanck.di

import com.luis.storibanck.data.network.createUser.FirebaseAuthRepositoryImpl
import com.luis.storibanck.data.network.createUser.dataSource.FirebaseAuthDataSource
import com.luis.storibanck.data.network.createUser.dataSource.FirebaseAuthDataSourceImpl
import com.luis.storibanck.domain.createUserRepository.FirebaseAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModel {
    @Binds
    abstract fun bindFirebaseAuthDataSourceImpl(
        firebaseAuthDataSourceImpl: FirebaseAuthDataSourceImpl
    ): FirebaseAuthDataSource

    @Binds
    abstract fun bindFirebaseAuthRepositoryImpl(
        firebaseAuthRepositoryImpl: FirebaseAuthRepositoryImpl
    ): FirebaseAuthRepository
}