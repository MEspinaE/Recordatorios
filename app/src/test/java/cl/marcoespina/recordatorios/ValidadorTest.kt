package cl.marcoespina.recordatorios

import cl.marcoespina.recordatorios.data.RepositorioSimulado
import cl.marcoespina.recordatorios.model.Categoria
import cl.marcoespina.recordatorios.model.EstadoRecordatorio
import cl.marcoespina.recordatorios.model.Prioridad
import cl.marcoespina.recordatorios.model.ResultadoValidacion
import cl.marcoespina.recordatorios.model.ValidadorRecordatorio
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ValidadorTest {
    private val cats = RepositorioSimulado.categorias
    private val hoy = LocalDate.of(2026, 11, 12)

    @Test fun setNoPermiteCategoriasRepetidas() = assertEquals(5, cats.size)

    @Test fun tituloCortoEsInvalido() {
        val r = ValidadorRecordatorio.crear(1, "ab", RepositorioSimulado.HOGAR, Prioridad.Baja, cats, hoy = hoy)
        assertTrue(r is ResultadoValidacion.Invalido)
    }

    @Test fun categoriaNoRegistradaEsInvalida() {
        val r = ValidadorRecordatorio.crear(1, "Gimnasio", Categoria("Deporte"), Prioridad.Media, cats, hoy = hoy)
        assertTrue(r is ResultadoValidacion.Invalido)
    }

    @Test fun fechaPasadaEnPendienteEsInvalida() {
        val r = ValidadorRecordatorio.crear(1, "Llamar", RepositorioSimulado.PERSONAL, Prioridad.Media, cats,
            fechaLimite = hoy.minusDays(1), hoy = hoy)
        assertTrue(r is ResultadoValidacion.Invalido)
    }

    @Test fun datosCorrectosSonValidos() {
        val r = ValidadorRecordatorio.crear(1, "Estudiar Kotlin", RepositorioSimulado.ESTUDIOS, Prioridad.Alta, cats,
            estado = EstadoRecordatorio.Pendiente, fechaLimite = hoy.plusDays(3), hoy = hoy)
        assertTrue(r is ResultadoValidacion.Valido)
    }

    @Test fun filtroPorPrioridad() {
        val altas = RepositorioSimulado.filtrar(RepositorioSimulado.recordatorios, prioridad = Prioridad.Alta)
        assertTrue(altas.all { it.prioridad == Prioridad.Alta })
    }
}
