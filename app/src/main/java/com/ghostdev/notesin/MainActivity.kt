package com.ghostdev.notesin

import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ghostdev.notesin.data.database.NotesDB
import com.ghostdev.notesin.navigation.NavGraph
import com.ghostdev.notesin.presentation.home.HomeLogic
import com.ghostdev.notesin.presentation.home.HomeLogicFactory
import com.ghostdev.notesin.presentation.home.ProvideLogic
import com.ghostdev.notesin.repository.NoteRepository
import com.ghostdev.notesin.ui.theme.NotesinTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.decorView.getWindowInsetsController()!!
            .setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS)
        setContent {
            NotesApp()
        }
    }
}

@Composable
fun NotesApp() {
    NotesinTheme {
        Surface {
            val context = LocalContext.current.applicationContext
            val noteDB = NotesDB.getInstance(context)
            val notesRepository = NoteRepository(noteDB)
            val homeLogic = viewModel(factory = HomeLogicFactory(notesRepository), modelClass = HomeLogic::class.java)
            ProvideLogic(viewModel = homeLogic) {
                NavGraph()
            }
        }
    }
}