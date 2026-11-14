package cl.marcoespina.recordatorios.data

import cl.marcoespina.recordatorios.model.Categoria
import cl.marcoespina.recordatorios.model.EstadoRecordatorio
import cl.marcoespina.recordatorios.model.EstiloCategoria
import cl.marcoespina.recordatorios.model.Prioridad
import cl.marcoespina.recordatorios.model.Recordatorio
import cl.marcoespina.recordatorios.model.ResultadoValidacion
import cl.marcoespina.recordatorios.model.Ubicacion
import cl.marcoespina.recordatorios.model.ValidadorRecordatorio
import java.time.LocalDate

/**
 * Datos simulados (sin persistencia ni red todavía).
 * - Set  -> categorías únicas
 * - Map  -> categoría a etiqueta/color
 * - List -> recordatorios
 */
object RepositorioSimulado {

    val TRABAJO = Categoria("Trabajo")
    val ESTUDIOS = Categoria("Estudios")
    val SALUD = Categoria("Salud")
    val HOGAR = Categoria("Hogar")
    val PERSONAL = Categoria("Personal")

    /** Set: aunque "Trabajo" se agregue dos veces, queda una sola. */
    val categorias: Set<Categoria> = setOf(TRABAJO, ESTUDIOS, SALUD, HOGAR, PERSONAL, Categoria("Trabajo"))

    /** Map: cada categoría asociada a una etiqueta corta y un color. */
    val estilosPorCategoria: Map<Categoria, EstiloCategoria> = mapOf(
        TRABAJO to EstiloCategoria("TRA", 0xFF1565C0),
        ESTUDIOS to EstiloCategoria("EST", 0xFF6A1B9A),
        SALUD to EstiloCategoria("SAL", 0xFF2E7D32),
        HOGAR to EstiloCategoria("HOG", 0xFFEF6C00),
        PERSONAL to EstiloCategoria("PER", 0xFF00838F)
    )

    private val ESTILO_POR_DEFECTO = EstiloCategoria("OTR", 0xFF757575)

    fun estiloDe(categoria: Categoria): EstiloCategoria =
        estilosPorCategoria[categoria] ?: ESTILO_POR_DEFECTO

    /** Lista inmutable construida pasando cada dato por el validador. */
    val recordatorios: List<Recordatorio> by lazy { construirDatos(LocalDate.now()) }

    /** Intentos descartados por el validador (se muestran en Logcat). */
    val rechazados: MutableList<String> = mutableListOf()

    private fun construirDatos(hoy: LocalDate): List<Recordatorio> {
        val intentos = listOf(
            ValidadorRecordatorio.crear(1, "Entregar Actividad 1", ESTUDIOS, Prioridad.Alta, categorias,
                descripcion = "Programación Mobile: PDF con evidencias", fechaLimite = hoy.plusDays(2), hoy = hoy),
            ValidadorRecordatorio.crear(2, "Reunión de planificación", TRABAJO, Prioridad.Media, categorias,
                estado = EstadoRecordatorio.EnProgreso, fechaLimite = hoy.plusDays(1),
                ubicacion = Ubicacion("Oficina central", -33.4489, -70.6693), hoy = hoy),
            ValidadorRecordatorio.crear(3, "Control médico anual", SALUD, Prioridad.Alta, categorias,
                fechaLimite = hoy.plusDays(10), hoy = hoy),
            ValidadorRecordatorio.crear(4, "Pagar cuentas del mes", HOGAR, Prioridad.Media, categorias,
                estado = EstadoRecordatorio.Completado, fechaLimite = hoy.minusDays(3), hoy = hoy),
            ValidadorRecordatorio.crear(5, "Comprar regalo de cumpleaños", PERSONAL, Prioridad.Baja, categorias, hoy = hoy),
            ValidadorRecordatorio.crear(6, "Renovar licencia de software", TRABAJO, Prioridad.Alta, categorias,
                estado = EstadoRecordatorio.Vencido, fechaLimite = hoy.minusDays(1), hoy = hoy),
            ValidadorRecordatorio.crear(7, "Leer capítulo de Kotlin", ESTUDIOS, Prioridad.Baja, categorias,
                estado = EstadoRecordatorio.EnProgreso, descripcion = "Colecciones: List, Set y Map", hoy = hoy),
            // --- Casos inválidos: el validador los rechaza ---
            ValidadorRecordatorio.crear(8, "Ok", HOGAR, Prioridad.Baja, categorias, hoy = hoy),
            ValidadorRecordatorio.crear(9, "Ir al gimnasio", Categoria("Deporte"), Prioridad.Media, categorias, hoy = hoy),
            ValidadorRecordatorio.crear(10, "Llamar al banco", PERSONAL, Prioridad.Media, categorias,
                fechaLimite = hoy.minusDays(5), hoy = hoy)
        )

        return intentos.mapNotNull { resultado ->
            when (resultado) {
                is ResultadoValidacion.Valido -> resultado.recordatorio
                is ResultadoValidacion.Invalido -> {
                    rechazados += resultado.errores.joinToString("; ")
                    null
                }
            }
        }
    }

    // ---------- Filtros ----------
    fun filtrar(
        lista: List<Recordatorio>,
        estado: EstadoRecordatorio? = null,
        prioridad: Prioridad? = null
    ): List<Recordatorio> = lista
        .filter { estado == null || it.estado == estado }
        .filter { prioridad == null || it.prioridad == prioridad }
        .sortedByDescending { it.prioridad.nivel }

    /** Map de conteo por estado, útil para el resumen superior. */
    fun conteoPorEstado(lista: List<Recordatorio>): Map<EstadoRecordatorio, Int> =
        lista.groupingBy { it.estado }.eachCount()
}
