package cl.marcoespina.recordatorios.model

import java.time.LocalDate

/**
 * Categoría de un recordatorio. Es una data class inmutable para que
 * dos categorías con el mismo nombre sean iguales (equals/hashCode) y
 * un Set<Categoria> no permita repetidas.
 */
data class Categoria(val nombre: String) {
    init {
        require(nombre.isNotBlank()) { "La categoría no puede estar vacía" }
    }
}

/** Prioridad del recordatorio. Sealed class: el conjunto de valores es cerrado. */
sealed class Prioridad(val nivel: Int, val etiqueta: String) {
    data object Alta : Prioridad(3, "Alta")
    data object Media : Prioridad(2, "Media")
    data object Baja : Prioridad(1, "Baja")

    companion object {
        val todas: List<Prioridad> get() = listOf(Alta, Media, Baja)
    }
}

/** Estado del ciclo de vida de un recordatorio. */
sealed class EstadoRecordatorio(val etiqueta: String) {
    data object Pendiente : EstadoRecordatorio("Pendiente")
    data object EnProgreso : EstadoRecordatorio("En progreso")
    data object Completado : EstadoRecordatorio("Completado")
    data object Vencido : EstadoRecordatorio("Vencido")

    companion object {
        val todos: List<EstadoRecordatorio> get() = listOf(Pendiente, EnProgreso, Completado, Vencido)
    }
}

/** Ubicación opcional asociada (se usará en módulos siguientes). */
data class Ubicacion(val nombre: String, val latitud: Double, val longitud: Double) {
    init {
        require(latitud in -90.0..90.0) { "Latitud fuera de rango" }
        require(longitud in -180.0..180.0) { "Longitud fuera de rango" }
    }
}

/**
 * Recordatorio inmutable. Los campos opcionales (descripción, fecha límite,
 * ubicación) son nullable porque no todos los recordatorios los tienen.
 * Para "modificar" un recordatorio se usa copy(), que crea una nueva instancia.
 */
data class Recordatorio(
    val id: Int,
    val titulo: String,
    val categoria: Categoria,
    val prioridad: Prioridad,
    val estado: EstadoRecordatorio = EstadoRecordatorio.Pendiente,
    val descripcion: String? = null,
    val fechaLimite: LocalDate? = null,
    val ubicacion: Ubicacion? = null
)

/** Información visual de cada categoría (etiqueta corta + color en ARGB). */
data class EstiloCategoria(val etiqueta: String, val colorArgb: Long)

/** Estados de la pantalla. */
sealed interface EstadoUi {
    data object Cargando : EstadoUi
    data class Exito(val recordatorios: List<Recordatorio>) : EstadoUi
    data object Vacio : EstadoUi
    data class Error(val mensaje: String) : EstadoUi
}
