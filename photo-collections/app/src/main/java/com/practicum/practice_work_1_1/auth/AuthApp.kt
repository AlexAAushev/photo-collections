package com.practicum.practice_work_1_1.auth

import android.net.Uri
import androidx.core.net.toUri
import net.openid.appauth.*
import kotlin.coroutines.suspendCoroutine

object AuthApp {

    private val serviceConfiguration = AuthorizationServiceConfiguration(
        Uri.parse(AuthConfig.AUTH_URI),
        Uri.parse(AuthConfig.TOKEN_URI),
        null,
        Uri.parse(AuthConfig.END_SESSION_URI)
    )

    fun getAuthRequest(): AuthorizationRequest {
        val redirectUri = AuthConfig.CALLBACK_URL.toUri()
        return AuthorizationRequest.Builder(
            serviceConfiguration,
            AuthConfig.CLIENT_ID,
            AuthConfig.RESPONSE_TYPE,
            redirectUri
        )
            .setScope(AuthConfig.SCOPE)
            .build()
    }

    fun getEndSessionRequest(): EndSessionRequest {
        return EndSessionRequest.Builder(serviceConfiguration)
            .setPostLogoutRedirectUri(AuthConfig.LOGOUT_CALLBACK_URL.toUri())
            .build()
    }

    suspend fun performTokenRequestSuspend(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
    ): TokensModel {
        return suspendCoroutine { continuation ->
            authService.performTokenRequest(
                tokenRequest,
                getClientAuthentication()
            ) { response, ex ->
                when {
                    response != null -> {
                        val tokens = TokensModel(
                            accessToken = response.accessToken.orEmpty(),
                            refreshToken = response.refreshToken.orEmpty(),
                            idToken = response.idToken.orEmpty()
                        )
                        continuation.resumeWith(Result.success(tokens))
                    }
                    ex != null -> {
                        continuation.resumeWith(Result.failure(ex))
                    }
                    else -> error("unreachable")
                }
            }
        }
    }

    private fun getClientAuthentication(): ClientAuthentication {
        return ClientSecretPost(AuthConfig.CLIENT_SECRET)
    }

    object AuthConfig {
        const val CLIENT_ID = "cV3g50CciYu389eqGLrkwEtFLbNOT65C5N1NFI44FGE"
        const val CLIENT_SECRET = "odv0LJOfHeBP_PhaWtBc9hRaABe9zBGZlV9YTa8_22g"
        const val AUTH_URI = "https://unsplash.com/oauth/authorize"
        const val TOKEN_URI = "https://unsplash.com/oauth/token"
        const val END_SESSION_URI = "https://unsplash.com/logout"
        const val RESPONSE_TYPE = ResponseTypeValues.CODE
        const val SCOPE =
            "read_user" +
                    " public" +
                    " write_user" +
                    " read_photos" +
                    " write_photos" +
                    " write_likes" +
                    " write_followers" +
                    " read_collections" +
                    " write_collections"
        const val CALLBACK_URL = "com.practice.work://callback"
        const val LOGOUT_CALLBACK_URL = "com.practice.work://callback"
    }
}