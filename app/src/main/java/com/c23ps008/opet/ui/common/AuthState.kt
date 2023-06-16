package com.c23ps008.opet.ui.common

sealed class AuthState {
    object Loading: AuthState()

    object Authorized: AuthState()

    object Unauthorized: AuthState()

    data class Error(val message: String): AuthState()
}