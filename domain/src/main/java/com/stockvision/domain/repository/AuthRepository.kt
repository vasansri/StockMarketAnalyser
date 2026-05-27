package com.stockvision.domain.repository

import com.stockvision.core.util.ResultState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginWithGoogle(idToken: String): Flow<ResultState<Unit>>
    fun loginWithEmail(email: String, pass: String): Flow<ResultState<Unit>>
    fun logout()
    fun isUserLoggedIn(): Boolean
    fun isUserPremium(): Flow<Boolean>
}
