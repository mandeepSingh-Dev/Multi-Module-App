package com.app.feature_notification.di

import com.app.feature_notification.domain.repository.NotificationRepository
import com.app.feature_notification.remote.NotificationRepositoryImpl
import com.app.feature_notification.remote.NotificationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideNotificationService(retrofit : Retrofit) : NotificationService {
        return retrofit.create(NotificationService::class.java)
    }
    @Provides
    fun provideNotificationRepository(notificationRepositoryImpl : NotificationRepositoryImpl) : NotificationRepository = notificationRepositoryImpl

}