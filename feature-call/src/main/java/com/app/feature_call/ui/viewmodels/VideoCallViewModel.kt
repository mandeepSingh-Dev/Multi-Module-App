package com.app.feature_call.ui.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.core_common.UserManager
import com.app.core_common.base.BaseViewModel
import com.app.core_common.utils.Timer
import com.app.core_common.utils.fromJson
import com.app.feature_call.domain.CallAllUseCases
import com.app.feature_call.remote.model.requestBody.GenerateTokenRequestBody
import com.app.feature_call.ui.states.VideoCallScreenEvents
import com.app.feature_call.ui.states.VideoCallScreenUIState
import com.app.feature_gift.domain.GiftAllUseCases
import com.app.feature_gift.remote.model.requestBody.GiftSendRequestBody
import com.app.feature_gift.remote.model.response.GiftListResponse
import com.app.feature_gift.ui.events.GiftEvents
import com.app.feature_wallet.domain.WalletAllUseCases
import com.app.feature_wallet.remote.model.response.WalletResponse
import com.uae.core_network.networkUtils.getCallbacks
import com.uae.core_socket.SocketConstants
import com.uae.core_socket.SocketIOManager
import com.uae.core_socket.model.CallAcceptedResponse
import com.uae.core_socket.model.CallStartedResponse
import com.uae.core_socket.model.CurrentEarnResponse
import com.uae.core_socket.model.WalletBalanceUpdateResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class VideoCallViewModel @Inject constructor(
    private val callAllUseCases: CallAllUseCases,
    private val userManager: UserManager,
    private val walletAllUseCases: WalletAllUseCases,
    private val giftAllUseCases: GiftAllUseCases,
) : BaseViewModel<VideoCallScreenUIState>(VideoCallScreenUIState()) {


    init {
        listenSocketEvents()
        getWallet()
        getProfileDetails()
        getGiftsList()
    }

    var partnerID: String? = null
    var chatID: String?= null
    var callID: String? = null
    var callType : String? = null
    var isFromIncomingCall: Boolean = false

    suspend fun getAuthToken(): String {
        return userManager.getUserToken()
    }
    var isFeedbackScreenShowed = false
    var isCurrentlyOnFeedbackScreen = false

    val timer : Timer by lazy {
        Timer()
    }

    fun getProfileDetails() = viewModelScope.launch{
        val userType = userManager.getUserType()
        updateState { state ->
            state.copy(userType = userType)
        }
    }

    fun listenSocketEvents() = viewModelScope.launch {
        SocketIOManager.events.collectLatest { socketEvent ->
            Log.d("fk343bnfkbnf", socketEvent.toString())
            when (socketEvent) {
                is SocketIOManager.SocketEvent.CallStarted -> {
                    val callStartedData = socketEvent.data.toString().fromJson<CallStartedResponse>()
                    callID = callStartedData?.callId
                }

                is SocketIOManager.SocketEvent.CallEnded -> {
                    onEvent(VideoCallScreenEvents.FinishCall)
                }

                is SocketIOManager.SocketEvent.CallRejected -> {
                    onEvent(VideoCallScreenEvents.CallRejectedFromCallee)
                }

                is SocketIOManager.SocketEvent.CallAccepted -> {
                    val callAcceptedData = socketEvent.data.toString().fromJson<CallAcceptedResponse>()

                    updateState { state ->
                        state.copy(callAcceptedData = callAcceptedData)
                    }

                    onEvent(VideoCallScreenEvents.CallAcceptedFromCallee(callAcceptedData))
                }
                is SocketIOManager.SocketEvent.GiftReceived -> {
                    increaseGiftCount()
                }

                is SocketIOManager.SocketEvent.CallCancelled -> {
                    onEvent(VideoCallScreenEvents.CallCancelledFromCaller)
                }
                is SocketIOManager.SocketEvent.WalletBalanceUpdated -> {
                    val walletBalanceUpdateResponse = socketEvent.data.toString().fromJson<WalletBalanceUpdateResponse>()

                    updateState { state -> state.copy(walletData = (state.walletData ?: WalletResponse.WalletData()).copy(balance = walletBalanceUpdateResponse?.coinsRemaining)) }
                }
                is SocketIOManager.SocketEvent.CurrentEarned -> {
                    val currentEarnResponse = socketEvent.data.toString().fromJson<CurrentEarnResponse>()

                    updateState { state -> state.copy(currentEarnResponse = currentEarnResponse) }
                }

                else -> Unit
            }
            Log.d("fkbnfjbnf", socketEvent.toString())
        }
    }

    fun sendCallUserSocketEvent(
        partnerID: String?,
        chatID: String?,
        callType: String?
    ) {

        val jsonObject = JSONObject()
        jsonObject.put(SocketConstants.CHAT_ID, chatID)
        jsonObject.put(SocketConstants.RECEIVER_ID, partnerID)
        jsonObject.put(SocketConstants.TYPE, callType)

        Log.d("kfnbkfnbf", jsonObject.toString())
        SocketIOManager.emit(SocketIOManager.CALL_USER, jsonObject)
    }

    fun sendCancelCallSocketEvent(
        callID: String?
    ) {

        val jsonObject = JSONObject()
        jsonObject.put(SocketConstants.CALL_ID, callID)
        Log.d("kfnbkfnbf", jsonObject.toString())
        SocketIOManager.emit(SocketIOManager.CANCEL_CALL, jsonObject)
    }

    fun sendRejectCallSocketEvent(
    ) {
        callID?.let {
            Log.d("fkbjfbnf", it.toString())
            val jsonObject = JSONObject()
            jsonObject.put(SocketConstants.CALL_ID, callID)
            jsonObject.put(SocketConstants.REASON, "Declined")
            Log.d("kfnbkfnbf", jsonObject.toString())
            val isSent = SocketIOManager.emit(SocketIOManager.REJECT_CALL, jsonObject)
            Log.d("fkbfknb", isSent.toString())
        }
    }

    fun sendAcceptCallSocketEvent(
        callID: String?
    ) {
        val jsonObject = JSONObject()
        jsonObject.put(SocketConstants.CALL_ID, callID)
        SocketIOManager.emit(SocketIOManager.ACCEPT_CALL, jsonObject)

        updateState { state ->
            state.copy(isIncomingCallAccepted = true, isIncomingCallAcceptingInProgress = false)
        }
    }

    fun sendEndCallSocketEvent() {
        Log.d("flbkfnbf", callID.toString())
        callID?.let {
            val jsonObject = JSONObject()
            jsonObject.put(SocketConstants.CALL_ID, callID)
            Log.d("fkbnfkbnf", jsonObject.toString())
            val d = SocketIOManager.emit(SocketIOManager.END_CALL, jsonObject)
            Log.d("fkbnkfnbf", d.toString())
        }
    }

    suspend fun generateToken(chatID: String?, partnerID: String?) {

        if (chatID.isNullOrEmpty() && partnerID.isNullOrEmpty()) return

        val body = GenerateTokenRequestBody(
            chatId = chatID,
            partnerId = partnerID
        )

        callAllUseCases.generateTokenUseCase(generateTokenRequestBody = body)
            .collectLatest { networkResult ->

                networkResult.getCallbacks(
                    onLoading = {
                        updateState { state ->
                            state.copy(isLoading = true)
                        }
                    },
                    onSuccess = {
                        val rtcToken = it.data?.data?.rtcToken
                        val userId = it.data?.data?.userId
                        val chatID = it.data?.data?.chatId
                        updateState { state ->
                            state.copy(isLoading = false)
                        }

                        onEvent(
                            VideoCallScreenEvents.JoinChannel(
                                rtcToken = rtcToken,
                                chatID = chatID,
                                userId = userId
                            )
                        )
                    },
                    onError = {
                        updateState { state ->
                            state.copy(isLoading = false)
                        }
                        errorEvent(it.error)
                    }
                )
            }
    }


    /*
    fun endCallApi(bookingID: String?) {
        userAllUseCases.endCallBookingUseCase(bookingId = bookingID).onEach { networkResult ->
            when (networkResult) {
                is NetworkResult.Loading -> {
                    updateState { state ->
                        state?.copy(isLoading = true)
                    }
                }

                is NetworkResult.Success -> {
                    updateState { state ->
                        state?.copy(isLoading = false)
                    }
                    onEvent(VideoCallScreenEvents.EndCallSuccess(endCallDetails = networkResult.data?.data?.details))
                }

                is NetworkResult.Error -> {
                    updateState { state ->
                        state?.copy(isLoading = false)
                    }
                    onEvent(CommonUiEvent.ShowError(error = networkResult.error))
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }
*/

    /*
    fun joinedCallApi(id : String?){
        userAllUseCases.joinedCallApiUseCase(id = id).onEach {
        id?.let {
            startRecording(id = id)
        }
        }.launchIn(viewModelScope)
    }
*/

    /*
    fun joinCallDetails(id : String?){
        userAllUseCases.joinedCallDetailsApi(id = id).onEach { networkResult ->

            if(networkResult is NetworkResult.Success) {
                val details = networkResult.data?.data?.details
                networkResult.data?.data?.details?.photo
                updateState { state ->
                    state?.copy(
                        personName = details?.name,
                        image = details?.photo
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
*/
    fun getWallet(){
        walletAllUseCases.GetWalletUseCase().onEach { networkResult ->
            networkResult.getCallbacks(
                onLoading = {
                    updateState { state ->
                        state.copy(isWalletLoading = true)
                    }
                },
                onSuccess = {
                        updateState { state ->
                            state.copy(
                                isWalletLoading = false,
                                walletData = it.data?.data
                            )
                        }
                },
                onError = {
                    updateState { state ->
                        state.copy(isWalletLoading = false)
                    }
                }
            )
        }.launchIn(viewModelScope)
    }

    fun getGiftsList() {
        giftAllUseCases.getGiftListUseCase().onEach { networkResult ->
            networkResult.getCallbacks(
                onLoading = {
                    updateState { state -> state.copy(isGiftsListLoading = true) }
                },
                onSuccess = {
                    updateState { state ->
                        state.copy(
                            isGiftsListLoading = false,
                            giftsList = it.data?.data
                        )
                    }
                },
                onError = {
                    updateState { state -> state.copy(isGiftsListLoading = false) }
                    onEvent(GiftEvents.GiftListFetchError(it.error))
                }
            )
        }
            .launchIn(viewModelScope)
    }

    fun sendGift(receiverId: String?, gift : GiftListResponse.GiftData?) {

        if (receiverId.isNullOrEmpty() || gift?.id.isNullOrEmpty()) return

        if((gift.coinCost ?: 0) > (uiState.value?.walletData?.balance ?: 0)){
            onEvent(GiftEvents.GiftSentError("Insufficient wallet balance"))
            return
        }

        val body = GiftSendRequestBody(
            giftId = gift.id,
            receiverId = receiverId
        )


        giftAllUseCases.sendGiftUseCase(giftSendRequestBody = body).onEach { networkResult ->
            networkResult.getCallbacks(
                onLoading = {
                    updateState { state -> state.copy(isGiftSending = true) }
                },
                onSuccess = {
                    updateState { state -> state.copy(isGiftSending = false) }
                    messageEvent(it.data?.message)

                    onEvent(GiftEvents.GiftSentSuccessfully(gift))
                },
                onError = {
                    updateState { state -> state.copy(isGiftSending = false) }
                    onEvent(GiftEvents.GiftSentError(it.error))
                }
            )
        }
            .launchIn(viewModelScope)
    }


    fun increaseGiftCount(){
        updateState { state -> state.copy(giftReceivedUnreadCount = (state.giftReceivedUnreadCount ?: 0).plus(1)) }
    }
    fun resetGiftCounts(){
        updateState { state -> state.copy(giftReceivedUnreadCount = 0) }
    }



    fun startTimer(){
        timer.startTimer(viewModelScope){timerValue ->
            updateState { state ->
                state.copy(timerValue = timerValue)
            }
        }
    }
    fun cancelTimer(){
        timer.cancelTimer()
    }
    

}
