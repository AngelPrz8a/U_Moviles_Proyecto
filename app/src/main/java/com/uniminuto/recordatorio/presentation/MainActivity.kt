package com.uniminuto.recordatorio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.uniminuto.recordatorio.data.local.EventDatabase
import com.uniminuto.recordatorio.data.repository.EventRepositoryImpl
import com.uniminuto.recordatorio.presentation.event.EventViewModel
import com.uniminuto.recordatorio.presentation.ui.AppFuturistaScaffold
import com.uniminuto.recordatorio.presentation.ui.theme.RecordatorioTheme

class MainActivity : ComponentActivity() {

    // 1. Inicialización de Room (Base de Datos Local)
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            EventDatabase::class.java,
            "futurista_db"
        )
            .allowMainThreadQueries()
            .build()
    }

    // 2. Inicialización del Repositorio (Implementación de RF15)
    private val repository by lazy {
        EventRepositoryImpl(database.eventDao())
    }

    // 3. Inicialización del ViewModel (Inyección manual)
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            EventViewModel.Factory(repository) // ✅ Utilitza la Factory amb el repositori local
        )[EventViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Se asume que AppFuturistaTheme está configurado con los colores neón
            RecordatorioTheme {
                AppFuturistaScaffold(viewModel = viewModel)
            }
        }
    }
}