package com.mich.nutrichef.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mich.nutrichef.data.remote.firebase.plato.Plato
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebasePlatoService(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val platosRef = db.collection("platos")

    suspend fun agregarPlato(plato: Plato): Boolean = withContext(Dispatchers.IO) {
        try {
            val docRef = platosRef.document()
            val platoConId = plato.copy(idPlato = docRef.id)
            docRef.set(platoConId).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun obtenerPlatos(): List<Plato> = withContext(Dispatchers.IO) {
        try {
            val snapshot = platosRef
                .orderBy("vistas", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Plato::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun obtenerPlatosPorCategoria(categoria: String): List<Plato> =
        withContext(Dispatchers.IO) {
            try {
                val snapshot = platosRef
                    .whereEqualTo("categoria", categoria)
                    .get()
                    .await()

                snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Plato::class.java)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }

    suspend fun buscarPlatos(query: String): List<Plato> = withContext(Dispatchers.IO) {
        try {
            val snapshot = platosRef
                .orderBy("nombre")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Plato::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun obtenerPlatoPorId(idPlato: String): Plato? = withContext(Dispatchers.IO) {
        try {
            val doc = platosRef.document(idPlato).get().await()
            doc.toObject(Plato::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun incrementarVistas(idPlato: String): Boolean = withContext(Dispatchers.IO) {
        try {
            platosRef.document(idPlato)
                .update("vistas", com.google.firebase.firestore.FieldValue.increment(1))
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}