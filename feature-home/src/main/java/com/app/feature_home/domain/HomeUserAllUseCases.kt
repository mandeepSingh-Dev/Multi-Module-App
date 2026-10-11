package com.app.feature_home.domain

import com.app.feature_home.domain.repository.HomeUserRepository
import com.app.feature_home.remote.model.response.DailyStreakResponse
import com.app.feature_home.remote.model.response.HomeDataUserResponse
import com.app.feature_home.remote.model.response.HostDetailsDataResponse
import com.app.feature_home.remote.model.response.TrackedVisitorResponse
import com.uae.core_network.networkUtils.NetworkResult
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

data class HomeUserAllUseCases @Inject constructor(
    val getHomeDataUseCase: GetHomeDataUseCase,
    val getHostDetailsUseCase: GetHostDetailsUseCase,
    val trackVisitorsUseCase: TrackVisitorsUseCase,
    val getDailyStreakUseCase: GetDailyStreakUseCase,
)

class GetHomeDataUseCase @Inject constructor(private val homeUserRepository: HomeUserRepository) {
    operator fun invoke(): Flow<NetworkResult<HomeDataUserResponse>> {
        return homeUserRepository.getHomeData()
    }
}

class GetDailyStreakUseCase @Inject constructor(private val homeUserRepository: HomeUserRepository) {
    operator fun invoke(): Flow<NetworkResult<DailyStreakResponse>> {
        return homeUserRepository.getDailyStreak()
    }
}



class GetHostDetailsUseCase @Inject constructor(private val homeUserRepository: HomeUserRepository) {
    operator fun invoke(id : String?): Flow<NetworkResult<HostDetailsDataResponse>> {
        return homeUserRepository.getHostDetails(
            id = id
        )
    }
}




class TrackVisitorsUseCase @Inject constructor(private val homeUserRepository: HomeUserRepository) {
    operator fun invoke(id : String?): Flow<NetworkResult<TrackedVisitorResponse>> {
        return homeUserRepository.trackVisitors(
            id = id
        )
    }
}

