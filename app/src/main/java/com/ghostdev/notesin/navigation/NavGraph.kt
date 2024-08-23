package com.ghostdev.notesin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ghostdev.notesin.presentation.home.HomeComponent
import com.ghostdev.notesin.presentation.notes.NotesComponent
import com.ghostdev.notesin.presentation.onboarding.OnboardingComponent
import com.ghostdev.notesin.utilities.PreferencesHelper

@Composable
fun NavGraph() {
    val controller = rememberNavController()
    val context = LocalContext.current.applicationContext
    val isFirstTime = PreferencesHelper.isFirstTime(context)

    NavHost(navController = controller, startDestination = if (isFirstTime) Destinations.Onboard.toString() else Destinations.Home.toString()) {
        composable(route = Destinations.Onboard.toString()) {
            OnboardingComponent(controller)
        }

        composable(route = Destinations.Home.toString()) {
            HomeComponent(controller)
        }

        composable(route = "${Destinations.ViewNote}/edit={edit}") { backStackEntry ->
            val edit = backStackEntry.arguments?.getString("edit")?.toBoolean() ?: false
            NotesComponent(controller = controller, edit = edit)
        }
    }
}