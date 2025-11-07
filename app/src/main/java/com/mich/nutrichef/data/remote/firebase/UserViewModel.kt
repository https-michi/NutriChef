package com.mich.nutrichef.data.remote.firebase

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.mich.nutrichef.data.remote.firebase.user.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var userListener: ListenerRegistration? = null

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val uid = auth.currentUser?.uid ?: return

        _isLoading.value = true

        userListener = firestore.collection("usuarios")
            .document(uid)
            .addSnapshotListener { snapshot, error ->
                _isLoading.value = false

                if (error != null) {
                    Log.e("UserViewModel", "Error al obtener perfil", error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val profile = snapshot.toObject(UserProfile::class.java)
                    _userProfile.value = profile ?: UserProfile()
                }
            }
    }

    fun updatePeso(nuevoPeso: Double) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("usuarios").document(uid)
            .update("peso", nuevoPeso)
            .addOnFailureListener { e -> Log.e("UserViewModel", "Error al actualizar peso", e) }
    }

    fun updateAltura(nuevaAltura: Double) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("usuarios").document(uid)
            .update("altura", nuevaAltura)
            .addOnFailureListener { e -> Log.e("UserViewModel", "Error al actualizar altura", e) }
    }

    override fun onCleared() {
        super.onCleared()
        userListener?.remove()
    }
}
