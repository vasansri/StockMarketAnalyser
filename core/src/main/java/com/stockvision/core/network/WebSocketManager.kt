package com.stockvision.core.network

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketManager @Inject constructor(
    private val client: OkHttpClient
) {
    private var webSocket: WebSocket? = null
    private val _messages = MutableSharedFlow<String>(replay = 0)
    val messages: SharedFlow<String> = _messages

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var reconnectJob: Job? = null

    sealed class ConnectionState {
        object Connected : ConnectionState()
        object Disconnected : ConnectionState()
        object Connecting : ConnectionState()
        data class Error(val message: String) : ConnectionState()
    }

    fun connect(url: String) {
        if (_connectionState.value == ConnectionState.Connected || _connectionState.value == ConnectionState.Connecting) return

        val request = Request.Builder().url(url).build()
        _connectionState.value = ConnectionState.Connecting

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                _connectionState.value = ConnectionState.Connected
                reconnectJob?.cancel()
                startHeartbeat()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                scope.launch {
                    _messages.emit(text)
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                _connectionState.value = ConnectionState.Disconnected
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _connectionState.value = ConnectionState.Error(t.message ?: "Unknown error")
                attemptReconnect(url)
            }
        })
    }

    private fun attemptReconnect(url: String) {
        reconnectJob?.cancel()
        reconnectJob = scope.launch {
            delay(5000) // Wait 5 seconds before reconnecting
            connect(url)
        }
    }

    private fun startHeartbeat() {
        scope.launch {
            while (isActive && _connectionState.value == ConnectionState.Connected) {
                webSocket?.send("ping") // Generic heartbeat
                delay(30000)
            }
        }
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    fun disconnect() {
        webSocket?.close(1000, "User initiated disconnect")
        _connectionState.value = ConnectionState.Disconnected
        reconnectJob?.cancel()
    }
}
