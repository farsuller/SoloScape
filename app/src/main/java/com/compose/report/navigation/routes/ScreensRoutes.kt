package com.compose.report.navigation.routes

import com.compose.report.util.Constants.REPORT_SCREEN_ARG_KEY

sealed class ScreensRoutes(val route: String) {
    object Authentication : ScreensRoutes(route = "auth_screen")
    object Home : ScreensRoutes(route = "home_screen")
    object Report : ScreensRoutes(route = "report_screen?$REPORT_SCREEN_ARG_KEY={$REPORT_SCREEN_ARG_KEY}") {
        fun passReportId(reportId: String) = "report_screen?$REPORT_SCREEN_ARG_KEY=$reportId"
    }
}
