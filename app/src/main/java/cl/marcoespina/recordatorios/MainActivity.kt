package cl.marcoespina.recordatorios

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.marcoespina.recordatorios.data.RepositorioSimulado
import cl.marcoespina.recordatorios.ui.PantallaInicio
import cl.marcoespina.recordatorios.ui.theme.RecordatoriosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Evidencia en Logcat (filtrar por "Recordatorios")
        val datos = RepositorioSimulado.recordatorios
        Log.d(TAG, "Categorías únicas (Set): ${RepositorioSimulado.categorias.map { it.nombre }}")
        Log.d(TAG, "Recordatorios válidos: ${datos.size}")
        RepositorioSimulado.rechazados.forEach { Log.w(TAG, "Rechazado por validación: $it") }

        setContent {
            RecordatoriosTheme { PantallaInicio() }
        }
    }

    companion object { private const val TAG = "Recordatorios" }
}
