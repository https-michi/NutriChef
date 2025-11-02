package com.mich.nutrichef.data.remote.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    data class Authenticated(val user: FirebaseUser) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val authService = FirebaseAuthService()
    private val auth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        _authState.value = if (user != null) {
            AuthState.Authenticated(user)
        } else {
            AuthState.Unauthenticated
        }
    }

    init {
        auth.addAuthStateListener(authStateListener)
        val currentUser = auth.currentUser
        _authState.value = if (currentUser != null) {
            AuthState.Authenticated(currentUser)
        } else {
            AuthState.Unauthenticated
        }
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authStateListener)
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun getUserName(): String {
        val user = auth.currentUser
        return user?.displayName
            ?: user?.email?.substringBefore("@")
            ?: "Usuario"
    }

    fun login(email: String, password: String, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authService.loginUser(email, password)
            _isLoading.value = false
            onResult(result)
        }
    }

    fun register(
        fullName: String,
        email: String,
        password: String,
        onResult: (Result<Unit>) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authService.registerUser(fullName, email, password)
            _isLoading.value = false
            onResult(result)
        }
    }

    fun resetPassword(email: String, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authService.resetPassword(email)
            _isLoading.value = false
            onResult(result)
        }
    }

    fun logout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    fun isProfileComplete(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = authService.isProfileComplete()
            onResult(result.getOrDefault(false))
        }
    }

    fun completeProfile(weight: Double, height: Double, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authService.completeProfile(weight, height)
            _isLoading.value = false
            onResult(result)
        }
    }
}