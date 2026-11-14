# Recordatorios — Actividad 1 · Programación Mobile

Primera versión de una app Android de recordatorios contextuales, hecha con **Kotlin**, **Jetpack Compose** y **Material 3**. Muestra datos simulados validados (sin persistencia ni red todavía).

**Autor:** Marco Antonio Espina

## Configuración

| Elemento | Valor |
|---|---|
| IDE | Android Studio (Ladybug o superior) |
| Lenguaje | Kotlin 2.0.21 (plugin Compose Compiler) |
| Android Gradle Plugin | 8.7.3 · Gradle 8.11.1 |
| compileSdk / targetSdk | 35 (Android 15) |
| minSdk | 26 (Android 8.0, para usar `java.time`) |
| UI | Compose BOM 2024.12.01 · Material 3 |
| JDK | 17 |

## Estructura

```
app/src/main/java/cl/marcoespina/recordatorios/
├── MainActivity.kt              Punto de entrada; log de evidencias en Logcat
├── model/
│   ├── Modelos.kt               data classes inmutables + sealed classes
│   └── Validador.kt             reglas de validación (ResultadoValidacion)
├── data/
│   └── RepositorioSimulado.kt   List / Set / Map con datos simulados y filtros
└── ui/
    ├── PantallaInicio.kt        Scaffold + filtros + LazyColumn
    ├── components/TarjetaRecordatorio.kt
    └── theme/Theme.kt           Tema Material 3 (color dinámico en Android 12+)
app/src/test/.../ValidadorTest.kt  Pruebas unitarias de validación y filtros
```

## Modelo (consigna 2)

- `Recordatorio` (data class inmutable): id, título, categoría, prioridad, estado y campos **opcionales** `descripcion`, `fechaLimite`, `ubicacion` (nullable).
- `Categoria`, `Ubicacion`, `EstiloCategoria`: data classes.
- `Prioridad` (Alta/Media/Baja), `EstadoRecordatorio` (Pendiente/En progreso/Completado/Vencido), `EstadoUi` (Cargando/Éxito/Vacío/Error) y `ResultadoValidacion` (Válido/Inválido): sealed classes/interfaces.

### Reglas de validación
1. El id debe ser positivo.
2. El título debe tener entre 3 y 60 caracteres (se recortan espacios).
3. La categoría debe existir en el `Set` de categorías registradas.
4. La descripción no supera 200 caracteres.
5. Un recordatorio activo (pendiente o en progreso) no puede tener una fecha límite pasada.

Los datos simulados incluyen 3 casos inválidos a propósito; el validador los rechaza y quedan registrados en Logcat (`Rechazado por validación`).

## Colecciones (consigna 3)

- **List**: `recordatorios` (lista inmutable).
- **Set**: `categorias`; "Trabajo" se agrega dos veces y queda solo una.
- **Map**: `estilosPorCategoria` asocia cada categoría con una etiqueta y un color; `conteoPorEstado` devuelve un `Map<Estado, Int>`.
- **Filtros**: `filtrar(lista, estado, prioridad)` con `filter` + `sortedByDescending`.

## Cómo ejecutar

1. Abrir la carpeta en Android Studio → esperar a que termine **Gradle Sync**.
2. Crear un emulador en **Device Manager** (por ejemplo Pixel 7, API 35) o conectar un teléfono con depuración USB.
3. Ejecutar con ▶ **Run 'app'**.
4. En Logcat, filtrar por `tag:Recordatorios` para ver las categorías, el total de recordatorios válidos y los rechazados.
5. Pruebas: clic derecho en `ValidadorTest` → *Run*, o `./gradlew test`.

## Registro de problemas

| # | Etapa | Problema | Corrección | Verificación |
|---|---|---|---|---|
| 1 | | | | |

(Completar con los problemas reales que aparezcan al sincronizar, compilar, instalar o ejecutar.)
