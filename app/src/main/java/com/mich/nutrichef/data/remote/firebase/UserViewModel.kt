package com.mich.nutrichef.data.remote.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.mich.nutrichef.data.remote.firebase.user.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _userName = MutableStateFlow("Usuario")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private var userListener: ListenerRegistration? = null

    init {
        loadUserName()
    }

    private fun loadUserName() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            _isLoading.value = true

            userListener = firestore.collection("usuarios")
                .document(uid)
                .addSnapshotListener { snapshot, error ->
                    _isLoading.value = false

                    if (error != null) {
                        _userName.value = auth.currentUser?.email?.substringBefore("@") ?: "Usuario"
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val profile = snapshot.toObject(UserProfile::class.java)
                        _userName.value = profile?.nombre ?: "Usuario"
                    } else {
                        _userName.value = auth.currentUser?.email?.substringBefore("@") ?: "Usuario"
                    }
                }
        } else {
            _userName.value = "Usuario"
        }
    }

    fun reloadUserName() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val uid = auth.currentUser?.uid ?: return@launch

                val doc = firestore.collection("usuarios")
                    .document(uid)
                    .get()
                    .await()

                val profile = doc.toObject(UserProfile::class.java)
                _userName.value = profile?.nombre ?: "Usuario"
            } catch (e: Exception) {
                _userName.value = auth.currentUser?.email?.substringBefore("@") ?: "Usuario"
            } finally {
                _isLoading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        userListener?.remove()
    }
}