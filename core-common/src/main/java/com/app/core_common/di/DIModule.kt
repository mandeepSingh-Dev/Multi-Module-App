package com.app.core_common.di

import com.app.core_common.datastore.AppDatastoreImpl
import com.app.core_common.datastore.DatastoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DIModule {

    @Provides
    @Singleton
    fun providesDatastore( appDatastoreImpl: AppDatastoreImpl) : DatastoreManager = appDatastoreImpl

}