package com.mich.nutrichef.data.remote.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mich.nutrichef.data.remote.firebase.plato.Ingrediente
import com.mich.nutrichef.data.remote.firebase.plato.Plato
import com.mich.nutrichef.data.remote.firebase.plato.Presupuesto
import com.mich.nutrichef.data.remote.firebase.plato.ValorNutricional
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlatoViewModel : ViewModel() {
    private val service = FirebasePlatoService()

    private val _platos = MutableStateFlow<List<Plato>>(emptyList())
    val platos: StateFlow<List<Plato>> = _platos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _platoSeleccionado = MutableStateFlow<Plato?>(null)
    val platoSeleccionado: StateFlow<Plato?> = _platoSeleccionado.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun cargarPlatos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val resultado = service.obtenerPlatos()
                _platos.value = resultado
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun seleccionarPlato(plato: Plato) {
        _platoSeleccionado.value = plato
        viewModelScope.launch {
            service.incrementarVistas(plato.idPlato)
        }
    }

    fun limpiarPlatoSeleccionado() {
        _platoSeleccionado.value = null
    }


    fun cargarPlatoPorId(idPlato: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val plato = service.obtenerPlatoPorId(idPlato)
                _platoSeleccionado.value = plato

                // Incrementar vistas
                if (plato != null) {
                    service.incrementarVistas(idPlato)
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}

//fun agregarEjemplos() {
//        viewModelScope.launch {
//            val ejemplos = listOf(
//                Plato(
//                    nombre = "Bowl de Salmón Teriyaki",
//                    descripcion = "Bowl nutritivo con salmón fresco, arroz integral y vegetales",
//                    imagen = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800",
//                    categoria = "Plato principal",
//                    porcionesBase = 1,
//                    tiempoPreparacion = 30,
//                    nivelDificultad = "Fácil",
//                    ingredientes = listOf(
//                        Ingrediente("Salmón fresco", 200.0, 1000.0, "gr", 0.0, 0.0),
//                        Ingrediente("Arroz integral", 100.0, 1000.0, "gr", 0.0, 0.0),
//                        Ingrediente("Brócoli", 80.0, 1000.0, "gr", 0.0, 0.0),
//                        Ingrediente("Zanahoria", 50.0, 1000.0, "gr", 0.0, 0.0)
//                    ),
//                    pasos = listOf(
//                        "Cocinar el arroz integral según instrucciones",
//                        "Marinar el salmón con salsa teriyaki",
//                        "Sellar el salmón en sartén caliente",
//                        "Saltear los vegetales",
//                        "Montar el bowl con todos los ingredientes"
//                    ),
//                    valorNutricional = ValorNutricional(
//                        calorias = 520.0,
//                        proteinas = 35.0,
//                        grasas = 18.0,
//                        carbohidratos = 45.0
//                    ),
//                    presupuesto = Presupuesto(15.0, "2025-11-01"),
//                    vistas = 245,
//                    favoritos = 48,
//                    creadorId = "demo",
//                    fechaCreacion = "2025-11-01T10:00:00",
//                    ultimaActualizacion = "2025-11-01T10:00:00"
//                ),
//                Plato(
//                    nombre = "Tostada de Aguacate y Huevo",
//                    descripcion = "Desayuno saludable con pan integral, aguacate y huevo pochado",
//                    imagen = "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=800",
//                    categoria = "Desayuno",
//                    porcionesBase = 1,
//                    tiempoPreparacion = 15,
//                    nivelDificultad = "Fácil",
//                    ingredientes = listOf(
//                        Ingrediente("Pan integral", 2.0, 1.0, "rebanadas", 0.0, 0.0),
//                        Ingrediente("Aguacate", 1.0, 1.0, "unidad", 0.0, 0.0),
//                        Ingrediente("Huevo", 1.0, 1.0, "unidad", 0.0, 0.0),
//                        Ingrediente("Tomate cherry", 4.0, 1.0, "unidades", 0.0, 0.0)
//                    ),
//                    pasos = listOf(
//                        "Tostar el pan integral",
//                        "Machacar el aguacate con sal y limón",
//                        "Pochar el huevo en agua hirviendo",
//                        "Untar aguacate en el pan",
//                        "Colocar el huevo pochado encima"
//                    ),
//                    valorNutricional = ValorNutricional(
//                        calorias = 340.0,
//                        proteinas = 15.0,
//                        grasas = 18.0,
//                        carbohidratos = 32.0
//                    ),
//                    presupuesto = Presupuesto(8.0, "2025-11-01"),
//                    vistas = 189,
//                    favoritos = 35,
//                    creadorId = "demo",
//                    fechaCreacion = "2025-11-01T10:00:00",
//                    ultimaActualizacion = "2025-11-01T10:00:00"
//                ),
//                Plato(
//                    nombre = "Ensalada Mediterránea",
//                    descripcion = "Ensalada fresca con vegetales, queso feta y aceitunas",
//                    imagen = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=800",
//                    categoria = "Ensalada",
//                    porcionesBase = 1,
//                    tiempoPreparacion = 15,
//                    nivelDificultad = "Fácil",
//                    ingredientes = listOf(
//                        Ingrediente("Lechuga romana", 100.0, 1000.0, "gr", 0.0, 0.0),
//                        Ingrediente("Tomate", 80.0, 1000.0, "gr", 0.0, 0.0),
//                        Ingrediente("Pepino", 60.0, 1000.0, "gr", 0.0, 0.0),
//                        Ingrediente("Queso feta", 40.0, 1000.0, "gr", 0.0, 0.0)
//                    ),
//                    pasos = listOf(
//                        "Lavar y cortar todos los vegetales",
//                        "Mezclar en un bowl grande",
//                        "Agregar queso feta desmenuzado",
//                        "Aliñar con aceite de oliva y limón"
//                    ),
//                    valorNutricional = ValorNutricional(
//                        calorias = 290.0,
//                        proteinas = 12.0,
//                        grasas = 18.0,
//                        carbohidratos = 20.0
//                    ),
//                    presupuesto = Presupuesto(10.0, "2025-11-01"),
//                    vistas = 167,
//                    favoritos = 29,
//                    creadorId = "demo",
//                    fechaCreacion = "2025-11-01T10:00:00",
//                    ultimaActualizacion = "2025-11-01T10:00:00"
//                ),
//                Plato(
//                    nombre = "Risotto de Champiñones",
//                    descripcion = "Cremoso risotto italiano con champiñones frescos",
//                    imagen = "https://images.unsplash.com/photo-1476124369491-f1a05fdd2b55?w=800",
//                    categoria = "Plato principal",
//                    porcionesBase = 1,
//                    tiempoPreparacion = 40,
//                    nivelDificultad = "Intermedio",
//                    ingredientes = listOf(
//                        Ingrediente("Arroz arborio", 100.0, 1000.0, "gr", 0.0, 0.0),
//                        Ingrediente("Champiñones", 150.0, 1000.0, "gr", 0.0, 0.0),
//                        Ingrediente("Caldo de vegetales", 500.0, 1000.0, "ml", 0.0, 0.0),
//                        Ingrediente("Queso parmesano", 30.0, 1000.0, "gr", 0.0, 0.0)
//                    ),
//                    pasos = listOf(
//                        "Saltear los champiñones",
//                        "Tostar el arroz arborio",
//                        "Agregar caldo gradualmente",
//                        "Revolver constantemente hasta cremoso",
//                        "Agregar queso parmesano al final"
//                    ),
//                    valorNutricional = ValorNutricional(
//                        calorias = 540.0,
//                        proteinas = 18.0,
//                        grasas = 12.0,
//                        carbohidratos = 85.0
//                    ),
//                    presupuesto = Presupuesto(12.0, "2025-11-01"),
//                    vistas = 198,
//                    favoritos = 42,
//                    creadorId = "demo",
//                    fechaCreacion = "2025-11-01T10:00:00",
//                    ultimaActualizacion = "2025-11-01T10:00:00"
//                )
//            )
//
//            ejemplos.forEach { plato ->
//                service.agregarPlato(plato)
//            }
//
//            cargarPlatos()
//        }
//    }

//-----------------------------------------------

//package com.mich.nutrichef.data.remote.firebase
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.mich.nutrichef.data.remote.firebase.plato.Ingrediente
//import com.mich.nutrichef.data.remote.firebase.plato.Plato
//import com.mich.nutrichef.data.remote.firebase.plato.Presupuesto
//import com.mich.nutrichef.data.remote.firebase.plato.ValorNutricional
//import kotlinx.coroutines.launch
//
//class PlatoViewModel : ViewModel() {
//    private val service = FirebasePlatoService()
//
//    fun agregarEjemplo() {
//        viewModelScope.launch {
//            val ejemplo = Plato(
//                nombre = "Shambar",
//                descripcion = "Preparación tradicional de La Libertad",
//                imagen = "https://firebasestorage.googleapis.com/...",
//                categoria = "Sopa típica",
//                porcionesBase = 1,
//                tiempoPreparacion = 90,
//                nivelDificultad = "Intermedio",
//                ingredientes = listOf(
//                    Ingrediente("Trigo resbalado", 500.0, 1000.0, "gr", 0.0, 0.0),
//                    Ingrediente("Pellejo de cerdo", 250.0, 1000.0, "ml", 0.0, 0.0)
//                ),
//                pasos = listOf(
//                    "Sancochar el trigo junto con las menestras.",
//                    "Agregar las carnes y cocinar hasta ablandar.",
//                    "Incorporar ají panca y condimentos.",
//                    "Servir caliente acompañado de pan."
//                ),
//                valorNutricional = ValorNutricional(
//                    calorias = 450.0,
//                    proteinas = 25.0,
//                    grasas = 15.0,
//                    carbohidratos = 50.0
//                ),
//                presupuesto = Presupuesto(8.50, "2025-10-22"),
//                vistas = 150,
//                favoritos = 27,
//                creadorId = "idUsuarioDemo",
//                fechaCreacion = "2025-10-22T18:30:00",
//                ultimaActualizacion = "2025-10-22T18:45:00"
//            )
//
//            val ok = service.agregarPlato(ejemplo)
//            println("Plato agregado: $ok")
//        }
//    }
//}