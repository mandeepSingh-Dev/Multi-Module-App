package com.app.feature_withdraw.remote

import com.app.core_common.UserManager
import com.app.feature_withdraw.domain.repository.WithdrawRepository
import com.app.feature_withdraw.remote.model.response.BeanPacksListResponse
import com.app.feature_withdraw.remote.model.response.WithdrawRequestResponse
import com.app.feature_withdraw.remote.model.response.WithdrawalSummaryDetailsResponse
import com.app.feature_withdraw.remote.model.response.WithdrawalVerificationStatusResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WithdrawRepositoryImpl  @Inject constructor(
    private val withdrawService: WithdrawService,
    private val networkHelper: NetworkHelper,
    private val userManager: UserManager
) : WithdrawRepository {


    override   fun withdrawalVerificationStatus() : Flow<NetworkResult<WithdrawalVerificationStatusResponse>> {


        return try {
            networkHelper.executeWithFlow<WithdrawalVerificationStatusResponse>(
                call = {
                    withdrawService.withdrawalVerificationStatus()
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override  fun getBeanPackages() : Flow<NetworkResult<BeanPacksListResponse>> {
        return try {
            networkHelper.executeWithFlow<BeanPacksListResponse>(
                call = {
                    withdrawService.getBeanPackages()
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override  fun withdrawalSummary() : Flow<NetworkResult<WithdrawalSummaryDetailsResponse>> {

        return try {

            networkHelper.executeWithFlow<WithdrawalSummaryDetailsResponse>(
                call = {
                    withdrawService.withdrawalSummary()
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override  fun mobileVerificationStart() : Flow<NetworkResult<ApiResponse2>> {


        return try {
            networkHelper.executeWithFlow<ApiResponse2>(
                call = {
                    withdrawService.mobileVerificationStart()
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override  fun mobileVerify(otp : String?) : Flow<NetworkResult<ApiResponse2>> {


        return try {
            val body = hashMapOf(ApiConstants.otp to otp)
            networkHelper.executeWithFlow<ApiResponse2>(
                call = {
                    withdrawService.mobileVerify(body = body)
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override   fun faceVerification(url : String?): Flow<NetworkResult<ApiResponse2>> {


        return try {

            val body = hashMapOf(ApiConstants.providerToken to url)
            networkHelper.executeWithFlow<ApiResponse2>(
                call = {
                    withdrawService.faceVerification(body = body)
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override  fun faceVerificationComplete(url : String?): Flow<NetworkResult<ApiResponse2>> {


        return try {

            val body = hashMapOf(ApiConstants.providerToken to url)
            networkHelper.executeWithFlow<ApiResponse2>(
                call = {
                    withdrawService.faceVerificationComplete(body = body)
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override   fun voiceVerification(url : String?) : Flow<NetworkResult<ApiResponse2>> {


        return try {

            val body = hashMapOf(ApiConstants.providerToken to url)
            networkHelper.executeWithFlow<ApiResponse2>(
                call = {
                    withdrawService.voiceVerification(body = body)
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override   fun voiceVerificationComplete(url : String?) : Flow<NetworkResult<ApiResponse2>> {


        return try {

            val body = hashMapOf(ApiConstants.providerToken to url)
            networkHelper.executeWithFlow<ApiResponse2>(
                call = {
                    withdrawService.voiceVerificationComplete(body = body)
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override   fun withdraw(amount : Int?) : Flow<NetworkResult<WithdrawRequestResponse>> {


        return try {

            val body = hashMapOf(ApiConstants.amount to amount)
            networkHelper.executeWithFlow<WithdrawRequestResponse>(
                call = {
                    withdrawService.withdraw(body = body)
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

}
