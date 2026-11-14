package cl.marcoespina.recordatorios.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.marcoespina.recordatorios.data.RepositorioSimulado
import cl.marcoespina.recordatorios.model.EstadoRecordatorio
import cl.marcoespina.recordatorios.model.Prioridad
import cl.marcoespina.recordatorios.model.Recordatorio
import cl.marcoespina.recordatorios.ui.theme.RecordatoriosTheme
import java.time.format.DateTimeFormatter

private val formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy")

fun colorPrioridad(p: Prioridad): Color = when (p) {
    Prioridad.Alta -> Color(0xFFC62828)
    Prioridad.Media -> Color(0xFFF9A825)
    Prioridad.Baja -> Color(0xFF558B2F)
}

@Composable
fun TarjetaRecordatorio(recordatorio: Recordatorio, modifier: Modifier = Modifier) {
    val estilo = RepositorioSimulado.estiloDe(recordatorio.categoria)
    val colorCategoria = Color(estilo.colorArgb)
    val completado = recordatorio.estado == EstadoRecordatorio.Completado

    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            // Indicador de categoría (etiqueta + color desde el Map)
            Box(
                modifier = Modifier.size(44.dp).background(colorCategoria, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(estilo.etiqueta, color = Color.White, style = MaterialTheme.typography.labelMedium)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = recordatorio.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (completado) TextDecoration.LineThrough else null
                )
                Text(
                    text = recordatorio.categoria.nombre,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorCategoria
                )
                recordatorio.descripcion?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                recordatorio.fechaLimite?.let {
                    Text("Vence: ${it.format(formatoFecha)}", style = MaterialTheme.typography.bodySmall)
                }
                recordatorio.ubicacion?.let {
                    Text("📍 ${it.nombre}", style = MaterialTheme.typography.bodySmall)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(
                        onClick = {},
                        label = { Text(recordatorio.prioridad.etiqueta) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = colorPrioridad(recordatorio.prioridad).copy(alpha = 0.15f),
                            labelColor = colorPrioridad(recordatorio.prioridad)
                        ),
                        shape = RoundedCornerShape(50)
                    )
                    AssistChip(onClick = {}, label = { Text(recordatorio.estado.etiqueta) })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TarjetaPreview() {
    RecordatoriosTheme(dynamicColor = false) {
        TarjetaRecordatorio(RepositorioSimulado.recordatorios.first())
    }
}
