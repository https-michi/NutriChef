package com.mich.nutrichef.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mich.nutrichef.data.remote.firebase.tips.TipNutricional
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseTipsService(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val tipsRef = db.collection("tips_nutricionales")

    suspend fun agregarTip(tip: TipNutricional): Boolean = withContext(Dispatchers.IO) {
        try {
            val docRef = tipsRef.document()
            val tipConId = tip.copy(idTip = docRef.id)
            docRef.set(tipConId).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun obtenerTips(): List<TipNutricional> = withContext(Dispatchers.IO) {
        try {
            val snapshot = tipsRef
                .orderBy("orden", Query.Direction.ASCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(TipNutricional::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}