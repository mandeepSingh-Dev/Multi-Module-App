// SocketIOManager.kt
package com.uae.core_socket

import android.util.Log
import com.app.core_network.BuildConfig
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.URISyntaxException
import kotlin.random.Random


object SocketIOManager {

//  val SOCKET_BASE_URL = "https://appsstart.com:5100"

    const val CALL_USER = "call_user"
    const val CALL_STARTED = "call_started"
    const val CALL_ACCEPTED = "call_accepted"
    const val CALL_CONNECTED = "call_connected"
    const val CALL_REJECTED = "call_rejected"
    const val CALL_CANCELLED = "call_cancelled"
    const val CALL_ENDED = "call_ended"
    const val CALL_BUSY = "call_busy"
    const val CALL_ERROR = "call_error"
    const val CALL_STATUS = "call_status"


    const val INCOMING_CALL = "incoming_call"
    const val END_CALL = "end_call"
    const val ACCEPT_CALL = "accept_call"
    const val REJECT_CALL = "reject_call"
    const val CANCEL_CALL = "cancel_call"
    const val CHAT_JOIN = "chat_join"
    const val CHAT_LEAVE = "chat_leave"


    const val GIFT_RECEIVED = "gift_received"


    const val NEW_MESSAGE = "chat_message"
    const val CHAT_JOINED = "chat_joined"
    const val GET_MESSAGES = "get_messages"
    const val UPDATE_CUSTOMER_LOCATION = "update_customer_location"
    const val WALLET_BALANCE_UPDATED = "wallet_balance_updated"
    const val CURRENT_EARNED = "current_earned"

    sealed class SocketEvent() {
        object Connected : SocketEvent()
        object Disconnected : SocketEvent()
        data class CallUser(val data: Any?) : SocketEvent()
        data class CallStarted(val data: Any?) : SocketEvent()
        data class CallAccepted(val data: Any?) : SocketEvent()
        data class CallConnected(val data: Any?) : SocketEvent()
        data class CallRejected(val data: Any?) : SocketEvent()
        data class CallCancelled(val data: Any?) : SocketEvent()
        data class CallEnded(val data: Any?) : SocketEvent()
        data class CallBusy(val data: Any?) : SocketEvent()
        data class CallError(val data: Any?) : SocketEvent()
        data class CallStatus(val data: Any?) : SocketEvent()
        data class IncomingCall(val data: Any?) : SocketEvent()
        data class EndCall(val data: Any?) : SocketEvent()
        data class AcceptCall(val data: Any?) : SocketEvent()
        data class RejectCall(val data: Any?) : SocketEvent()
        data class CancelCall(val data: Any?) : SocketEvent()
        data class WalletBalanceUpdated(val data: Any?) : SocketEvent()
        data class CurrentEarned(val data: Any?) : SocketEvent()
        data class GiftReceived(val data: Any?) : SocketEvent()

      data class NewMessage(val data: Any?) : SocketEvent()
      data class ChatJoined(val data: Any?) : SocketEvent()
        data class Error(val throwable: Throwable) : SocketEvent()
    }

    // coroutine scope owned by this manager; recreated in clear()
    private var scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val socketMutex = Mutex()
    var socket: Socket? = null

    private val _events = MutableSharedFlow<SocketEvent>(extraBufferCapacity = 64)
    val events: SharedFlow<SocketEvent> = _events

    // reconnection control
    private var manualDisconnect = false
    private var reconnectAttempts = 0

    private fun createOptions(authToken: String?): IO.Options {
        return IO.Options().apply {
            auth = authToken?.let { mapOf("authorization" to it) }
            path = "/kahoji/socket.io"
            reconnection = true
        }
    }

    suspend fun connect(authToken: String? = null, onConnected: () -> Unit = {}) {
        socketMutex.withLock {

            Log.d("fbknfkbnf", authToken.toString())
            Log.d("fbjfnbjf", socket?.connected().toString() + " Socket Connected")
            if (socket != null) {
                if (socket?.connected() == true) {
                    onConnected()
                }
                return
            }
            manualDisconnect = false
            reconnectAttempts = 0

            try {
                val opts = createOptions(authToken)
                val s = IO.socket("wss://bookyourapps.com"   /*BuildConfig.SOCKET_BASE_URL*/, opts)
                socket = s

                // register listeners
                s.on(Socket.EVENT_CONNECT, onConnect)
                s.on(Socket.EVENT_DISCONNECT, onDisconnect)
                s.on(Socket.EVENT_CONNECT_ERROR, onConnectError)

                s.on(NEW_MESSAGE, onNewMessage)
                s.on(CHAT_JOINED, onChatJoined)
                s.on(CALL_STARTED, onCallStarted)
                s.on(CALL_ACCEPTED, onCallAccepted)
                s.on(CALL_CONNECTED, onCallConnected)
                s.on(CALL_REJECTED, onCallRejected)
                s.on(CALL_CANCELLED, onCallCancelled)
                s.on(CALL_ENDED, onCallEnded)
                s.on(CALL_BUSY, onCallBusy)
                s.on(CALL_ERROR, onCallError)
                s.on(CALL_STATUS, onCallStatus)
                s.on(INCOMING_CALL, onIncomingCall)
                s.on(GIFT_RECEIVED, onReceiveGift)
                s.on(ACCEPT_CALL, acceptCall)
                s.on(REJECT_CALL, rejectCall)
                s.on(CANCEL_CALL, cancelCall)
                s.on(WALLET_BALANCE_UPDATED, walletBalanceUpdated)
                s.on(CURRENT_EARNED, currentEarned)


                s.on(Socket.EVENT_CONNECT_ERROR, {
                    it.forEach {
                        Log.d("fbknfkbf", it.toString())
                    }
                    Log.d("kfbnkfnbf", it.firstOrNull().toString())
                })


                val socket = s.connect()

                if (socket.connected()) {
                    onConnected()
                }

            } catch (e: URISyntaxException) {
                Log.d("fkbnkfnbf", e.message.toString())
                scope.launch { _events.emit(SocketEvent.Error(e)) }
            } catch (e: Exception) {
                Log.d("fkbfkbnf", e.message.toString())
            }
        }
    }

    private val onConnect = Emitter.Listener {
        scope.launch {
            Log.d("rkbnjkjnbvf", "onConnect")
            reconnectAttempts = 0
            _events.emit(SocketEvent.Connected)
        }
    }

    private val onCallStarted = Emitter.Listener {
        scope.launch {
            Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " onCallStarted")
            reconnectAttempts = 0
            _events.emit(SocketEvent.CallStarted(data = it.firstOrNull()))
        }
    }


    private val onCallAccepted = Emitter.Listener {
        scope.launch {
            Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " onCallAccepted")
            reconnectAttempts = 0
            _events.emit(SocketEvent.CallAccepted(data = it.firstOrNull()))
        }
    }


    private val onCallConnected = Emitter.Listener {
        scope.launch {
            Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " onCallConnected")
            reconnectAttempts = 0
            _events.emit(SocketEvent.CallConnected(data = it.firstOrNull()))
        }
    }



    private val onCallRejected = Emitter.Listener {
        scope.launch {
            Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " onCallRejected")
            reconnectAttempts = 0
            _events.emit(SocketEvent.CallRejected(data = it.firstOrNull()))
        }
    }


    private val onCallCancelled = Emitter.Listener {
        scope.launch {
            Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " onCallCancelled")
            reconnectAttempts = 0
            _events.emit(SocketEvent.CallCancelled(data = it.firstOrNull()))
        }
    }

    private val onCallEnded = Emitter.Listener {
        scope.launch {
            Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " onCallEnded")
            reconnectAttempts = 0
            _events.emit(SocketEvent.CallEnded(data = it.firstOrNull()))
        }
    }



    private val onCallBusy = Emitter.Listener {
        scope.launch {
            Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " onCallBusy")
            reconnectAttempts = 0
            _events.emit(SocketEvent.CallBusy(data = it.firstOrNull()))
        }
    }


    private val onCallError = Emitter.Listener {
        scope.launch {
            Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " onCallError")
            reconnectAttempts = 0
            _events.emit(SocketEvent.CallError(data = it.firstOrNull()))
        }
    }





    private val onCallStatus = Emitter.Listener {
        scope.launch {
            Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " onCallStatus")
            reconnectAttempts = 0
            _events.emit(SocketEvent.CallStatus(data = it.firstOrNull()))
        }
    }





    private val onIncomingCall = Emitter.Listener {
        scope.launch {
            Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " onIncomingCall")
            reconnectAttempts = 0
            _events.emit(SocketEvent.IncomingCall(data = it.firstOrNull()))
        }
    }


    private val acceptCall = Emitter.Listener {
        scope.launch {
            Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " acceptCall")
            reconnectAttempts = 0
            _events.emit(SocketEvent.AcceptCall(data = it.firstOrNull()))
        }
    }

    private val rejectCall = Emitter.Listener {
        scope.launch {
            Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " rejectCall")
            reconnectAttempts = 0
            _events.emit(SocketEvent.RejectCall(data = it.firstOrNull()))
        }
    }


    private val cancelCall = Emitter.Listener {
        scope.launch {
            Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " cancelCall")
            reconnectAttempts = 0
            _events.emit(SocketEvent.CancelCall(data = it.firstOrNull()))
        }
    }



    private val walletBalanceUpdated = Emitter.Listener {
        scope.launch {
            Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " walletBalanceUpdated")
            reconnectAttempts = 0
            _events.emit(SocketEvent.WalletBalanceUpdated(data = it.firstOrNull()))
        }
    }



    private val currentEarned = Emitter.Listener {
        scope.launch {
            Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " walletBalanceUpdated")
            reconnectAttempts = 0
            _events.emit(SocketEvent.CurrentEarned(data = it.firstOrNull()))
        }
    }





    private val onNewMessage = Emitter.Listener {
        scope.launch {
            Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " onNewMessage")
            reconnectAttempts = 0
            _events.emit(SocketEvent.NewMessage(data = it.firstOrNull()))
        }
    }
    private val onChatJoined = Emitter.Listener {
        scope.launch {
            Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " onNewMessage")
            reconnectAttempts = 0
            _events.emit(SocketEvent.ChatJoined(data = it.firstOrNull()))
        }
    }
    private val onReceiveGift = Emitter.Listener {
        scope.launch {
            Log.d("dkvmknvkdnvd", it.firstOrNull().toString() + " onReceiveGift")
            reconnectAttempts = 0
            _events.emit(SocketEvent.GiftReceived(data = it.firstOrNull()))
        }
    }

    private val onDisconnect = Emitter.Listener { args ->
        scope.launch {
            Log.d("rkbnjkjnbvf", "oNdISCONNECTED")
            _events.emit(SocketEvent.Disconnected)
            maybeReconnect()
        }
    }
    private val onConnectError = Emitter.Listener { args ->
        scope.launch {
            val err = args?.firstOrNull() as? Throwable
                ?: Exception("Unknown socket error: ${args?.joinToString()}")
            _events.emit(SocketEvent.Error(err))

            val error = args.firstOrNull()
            Log.e("Socket43433d3", "Connect Error: $error")
            if (error is Exception) {
                error.printStackTrace()
            }

            maybeReconnect()
        }
    }

    fun addEvent(event: String, listener: Emitter.Listener) {
        scope.launch {
            socket?.on(event, listener)
        }
    }

    // Emit message
    fun emit(event: String, vararg args: Any): Boolean {
        val s = socket ?: return false
        return try {
            s.emit(event, *args)
            true
        } catch (t: Throwable) {
            scope.launch { _events.emit(SocketEvent.Error(t)) }
            false
        }
    }

    // Disconnect (manual indicates user-initiated)
    fun disconnect(manual: Boolean = true) {
        scope.launch {
            socketMutex.withLock {
                manualDisconnect = manual
                socket?.off()
                socket?.disconnect()
                socket?.close()
                socket = null
            }
        }
    }

    // Complete cleanup: cancel coroutines, remove refs
    fun clear() {
        // mark manual to avoid reconnect attempts
        manualDisconnect = true

        // cancel ongoing tasks and recreate scope
        scope.cancel()
        scope = SupervisorJob().let { CoroutineScope(it + Dispatchers.IO) }

        // disconnect socket and null
        runBlocking {
            socketMutex.withLock {
                socket?.let { s ->
                    s.off()
                    s.disconnect()
                    s.close()
                }
                socket = null
            }
        }
        reconnectAttempts = 0
    }

    // Reconnect strategy controlled by us; prevents infinite tight reconnects
    private fun maybeReconnect() {
        scope.launch {
            if (manualDisconnect) return@launch

            reconnectAttempts++
            val max = 10
            if (reconnectAttempts > max) {
                _events.emit(SocketEvent.Error(Exception("Max reconnect attempts reached")))
                return@launch
            }
            val base = 1000L
            val backoff = base * (1L shl reconnectAttempts.coerceAtMost(6))
            val jitter = Random.nextLong(0, base)
            val delayMs = (backoff + jitter).coerceAtMost(60_000L)

            delay(delayMs)

            socketMutex.withLock {
                if (socket == null && !manualDisconnect) {
                    // Try reconnect. If token-refresh needed, caller should call connect(newToken)
                    try {
                        val s = IO.socket(
                            BuildConfig.SOCKET_BASE_URL,
                            createOptions(null)
                        ) // pass fresh token if available
                        socket = s
                        s.on(Socket.EVENT_CONNECT, onConnect)
                        s.on(Socket.EVENT_DISCONNECT, onDisconnect)
                        s.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
                        s.connect()
                    } catch (e: Exception) {
                        _events.emit(SocketEvent.Error(e))
                        maybeReconnect()
                    }
                }
            }
        }
    }
}
