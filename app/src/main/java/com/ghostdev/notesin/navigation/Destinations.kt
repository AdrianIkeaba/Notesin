package com.ghostdev.notesin.navigation

sealed class Destinations(route: String) {
    data object Onboard: Destinations("onboarding")
    data object Home: Destinations("home")
    data object ViewNote: Destinations("viewNote")
    data object Search: Destinations("search")
}