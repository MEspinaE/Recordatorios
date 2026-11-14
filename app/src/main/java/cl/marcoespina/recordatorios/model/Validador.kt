package cl.marcoespina.recordatorios.model

import java.time.LocalDate

/** Resultado de validar los datos de un recordatorio. */
sealed interface ResultadoValidacion {
    data class Valido(val recordatorio: Recordatorio) : ResultadoValidacion
    data class Invalido(val errores: List<String>) : ResultadoValidacion
}

/**
 * Reglas de negocio iniciales. Se valida ANTES de crear el objeto, así
 * nunca existe un Recordatorio en estado inválido.
 */
object ValidadorRecordatorio {

    const val LARGO_MIN_TITULO = 3
    const val LARGO_MAX_TITULO = 60
    const val LARGO_MAX_DESCRIPCION = 200

    fun crear(
        id: Int,
        titulo: String,
        categoria: Categoria,
        prioridad: Prioridad,
        categoriasPermitidas: Set<Categoria>,
        estado: EstadoRecordatorio = EstadoRecordatorio.Pendiente,
        descripcion: String? = null,
        fechaLimite: LocalDate? = null,
        ubicacion: Ubicacion? = null,
        hoy: LocalDate = LocalDate.now()
    ): ResultadoValidacion {
        val errores = mutableListOf<String>()
        val tituloLimpio = titulo.trim()

        if (id <= 0) errores += "El id debe ser positivo"
        if (tituloLimpio.length < LARGO_MIN_TITULO)
            errores += "El título debe tener al menos $LARGO_MIN_TITULO caracteres"
        if (tituloLimpio.length > LARGO_MAX_TITULO)
            errores += "El título no puede superar $LARGO_MAX_TITULO caracteres"
        if (categoria !in categoriasPermitidas)
            errores += "La categoría '${categoria.nombre}' no está registrada"
        if (descripcion != null && descripcion.length > LARGO_MAX_DESCRIPCION)
            errores += "La descripción no puede superar $LARGO_MAX_DESCRIPCION caracteres"
        if (fechaLimite != null && fechaLimite.isBefore(hoy) &&
            estado != EstadoRecordatorio.Completado && estado != EstadoRecordatorio.Vencido
        ) errores += "Un recordatorio activo no puede tener una fecha límite pasada"

        return if (errores.isEmpty()) {
            ResultadoValidacion.Valido(
                Recordatorio(
                    id = id,
                    titulo = tituloLimpio,
                    categoria = categoria,
                    prioridad = prioridad,
                    estado = estado,
                    descripcion = descripcion?.trim()?.ifBlank { null },
                    fechaLimite = fechaLimite,
                    ubicacion = ubicacion
                )
            )
        } else {
            ResultadoValidacion.Invalido(errores)
        }
    }
}
