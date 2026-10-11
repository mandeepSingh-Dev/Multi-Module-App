package com.app.feature_review.di

import com.app.feature_review.domain.repository.ReviewRepository
import com.app.feature_review.remote.ReviewRatingService
import com.app.feature_review.remote.ReviewRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideReviewService(retrofit : Retrofit) : ReviewRatingService {
        return retrofit.create(ReviewRatingService::class.java)
    }
    @Provides
    fun provideReviewRepository(reviewRepositoryImpl : ReviewRepositoryImpl) : ReviewRepository = reviewRepositoryImpl

}