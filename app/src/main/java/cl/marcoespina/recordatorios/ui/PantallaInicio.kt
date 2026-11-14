package cl.marcoespina.recordatorios.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.marcoespina.recordatorios.R
import cl.marcoespina.recordatorios.data.RepositorioSimulado
import cl.marcoespina.recordatorios.model.EstadoRecordatorio
import cl.marcoespina.recordatorios.model.EstadoUi
import cl.marcoespina.recordatorios.model.Prioridad
import cl.marcoespina.recordatorios.ui.components.TarjetaRecordatorio
import cl.marcoespina.recordatorios.ui.theme.RecordatoriosTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio() {
    var filtroEstado by remember { mutableStateOf<EstadoRecordatorio?>(null) }
    var filtroPrioridad by remember { mutableStateOf<Prioridad?>(null) }

    val todos = RepositorioSimulado.recordatorios
    val filtrados = RepositorioSimulado.filtrar(todos, filtroEstado, filtroPrioridad)
    val estadoUi: EstadoUi = if (filtrados.isEmpty()) EstadoUi.Vacio else EstadoUi.Exito(filtrados)
    val conteo = RepositorioSimulado.conteoPorEstado(todos)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.titulo_pantalla)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            Text(
                text = "Total: ${todos.size} · " + EstadoRecordatorio.todos.joinToString(" · ") {
                    "${it.etiqueta}: ${conteo[it] ?: 0}"
                },
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Text(stringResource(R.string.filtro_estado), style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(horizontal = 16.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(selected = filtroEstado == null, onClick = { filtroEstado = null },
                        label = { Text(stringResource(R.string.todos)) })
                }
                items(EstadoRecordatorio.todos) { estado ->
                    FilterChip(
                        selected = filtroEstado == estado,
                        onClick = { filtroEstado = if (filtroEstado == estado) null else estado },
                        label = { Text(estado.etiqueta) }
                    )
                }
            }

            Text(stringResource(R.string.filtro_prioridad), style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(horizontal = 16.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(selected = filtroPrioridad == null, onClick = { filtroPrioridad = null },
                        label = { Text(stringResource(R.string.todos)) })
                }
                items(Prioridad.todas) { p ->
                    FilterChip(
                        selected = filtroPrioridad == p,
                        onClick = { filtroPrioridad = if (filtroPrioridad == p) null else p },
                        label = { Text(p.etiqueta) }
                    )
                }
            }

            when (estadoUi) {
                is EstadoUi.Cargando -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                is EstadoUi.Vacio -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.sin_resultados))
                }
                is EstadoUi.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(estadoUi.mensaje, color = MaterialTheme.colorScheme.error)
                }
                is EstadoUi.Exito -> LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(estadoUi.recordatorios, key = { it.id }) { r ->
                        TarjetaRecordatorio(r)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PantallaInicioPreview() {
    RecordatoriosTheme(dynamicColor = false) { PantallaInicio() }
}
