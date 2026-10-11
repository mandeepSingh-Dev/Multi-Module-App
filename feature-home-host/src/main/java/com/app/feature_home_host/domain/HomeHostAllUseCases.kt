package com.app.feature_home_host.domain

import com.app.feature_home_host.domain.repository.HomeHostRepository
import com.app.feature_home_host.remote.DashboardStatsApiResponse
import com.app.feature_home_host.remote.model.response.VisitorDetailsResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

data class HomeHosAllUseCases @Inject constructor(
    val toggleOnlineOfflineUseCase : ToggleOnlineOfflineUseCase,
    val getDashboardStatsUseCase : GetDashboardStatsUseCase,
    val getVisitorDetailsUseCase : GetVisitorDetailsUseCase,
)

class ToggleOnlineOfflineUseCase @Inject constructor(private val homeHostRepository: HomeHostRepository) {
    operator fun invoke(isOnline : Int): Flow<NetworkResult<ApiResponse2>> {
        return homeHostRepository.toggleOnlineOffline(isOnline =isOnline)
    }
}

class GetDashboardStatsUseCase @Inject constructor(private val homeHostRepository: HomeHostRepository) {
    operator fun invoke(): Flow<NetworkResult<DashboardStatsApiResponse>> {
        return homeHostRepository.getDashboardStats()
    }
}

class GetVisitorDetailsUseCase @Inject constructor(private val homeHostRepository: HomeHostRepository) {
    operator fun invoke(visitorId : String?): Flow<NetworkResult<VisitorDetailsResponse>> {
        return homeHostRepository.getVisitorDetails(
            visitorID = visitorId
        )
    }
}
