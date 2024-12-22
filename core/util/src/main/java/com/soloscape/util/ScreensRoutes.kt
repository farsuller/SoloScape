package com.soloscape.util

import com.soloscape.util.Constants.NOTE_SCREEN_ARG_KEY

sealed class ScreensRoutes(val route: String) {
    data object Authentication : ScreensRoutes(route = "auth_screen")
    data object Home : ScreensRoutes(route = "home_screen")
    data object Note : ScreensRoutes(route = "note_screen?$NOTE_SCREEN_ARG_KEY={$NOTE_SCREEN_ARG_KEY}") {
        fun passNoteId(noteId: Int) = "note_screen?$NOTE_SCREEN_ARG_KEY=$noteId"
    }
}
