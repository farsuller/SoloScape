package com.soloscape.util.routes

import com.soloscape.util.Constants.NOTE_SCREEN_ARG_KEY

sealed class ScreensRoutes(val route: String) {
    data object Authentication : ScreensRoutes(route = "auth_screen")
    data object FeltRoute : ScreensRoutes(route = "felt_route")
    data object WriteFeltRoute : ScreensRoutes(route = "write_route?$NOTE_SCREEN_ARG_KEY={$NOTE_SCREEN_ARG_KEY}") {
        fun passWriteId(noteId: Int) = "write_route?$NOTE_SCREEN_ARG_KEY=$noteId"
    }
    data object DashboardRoute : ScreensRoutes(route = "dashboard_route")
}
