package com.mich.nutrichef.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mich.nutrichef.data.remote.firebase.user.UserProfile
import kotlinx.coroutines.tasks.await

class FirebaseAuthService(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("UID nulo")

            val profile = UserProfile(
                idUsuario = uid,
                nombre = fullName,
                email = email,
                isProfileComplete = false
            )
            firestore.collection("usuarios")
                .document(uid)
                .set(profile)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- esto estaa para cambiar de lugar
    suspend fun completeProfile(weight: Double, height: Double): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("No hay usuario")

            firestore.collection("usuarios")
                .document(uid)
                .update(
                    mapOf(
                        "peso" to weight,
                        "altura" to height,
                        "isProfileComplete" to true
                    )
                )
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isProfileComplete(): Result<Boolean> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("No hay usuario")
            val doc = firestore.collection("usuarios")
                .document(uid)
                .get()
                .await()

            val isComplete = doc.getBoolean("isProfileComplete") ?: false
            Result.success(isComplete)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    ///
    suspend fun loginUser(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUser() = auth.currentUser
}