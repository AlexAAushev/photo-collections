package com.practicum.practice_work_1_1.auth

import com.practicum.practice_work_1_1.editor
import com.practicum.practice_work_1_1.sharedPreferences
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.TokenRequest
import timber.log.Timber

class AuthRepository {

    fun logout() {
        TokenStorage.accessToken = null
        TokenStorage.refreshToken = null
        TokenStorage.idToken = null
    }

    fun getAuthRequest(): AuthorizationRequest {
        return AuthApp.getAuthRequest()
    }

    fun getEndSessionRequest(): EndSessionRequest {
        return AuthApp.getEndSessionRequest()
    }

    suspend fun performTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
    ) {
        val tokens = AuthApp.performTokenRequestSuspend(authService, tokenRequest)
        TokenStorage.accessToken = tokens.accessToken
        TokenStorage.refreshToken = tokens.refreshToken
        TokenStorage.idToken = tokens.idToken
        Timber.tag("Oauth")
            .d("5. Tokens accepted:\n access=${tokens.accessToken}\nrefresh=${tokens.refreshToken}\nidToken=${tokens.idToken}")
        editor = sharedPreferences.edit()
        editor.putString("TOKEN",TokenStorage.accessToken)
        editor.apply()
        println("Токен сохранён")
    }
}

object TokenStorage {
    var accessToken: String? = null
    var refreshToken: String? = null
    var idToken: String? = null
}

data class TokensModel(
    val accessToken: String,
    val refreshToken: String,
    val idToken: String
)