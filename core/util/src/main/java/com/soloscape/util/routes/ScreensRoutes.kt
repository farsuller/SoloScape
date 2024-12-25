package com.soloscape.util.routes

import com.soloscape.util.Constants.Routes.DASHBOARD_ROUTE
import com.soloscape.util.Constants.Routes.FELT_ROUTE
import com.soloscape.util.Constants.Routes.IDEA_ROUTE
import com.soloscape.util.Constants.Routes.NOTE_COLOR_ARG_KEY
import com.soloscape.util.Constants.Routes.NOTE_IDEA_ROUTE
import com.soloscape.util.Constants.Routes.NOTE_ID_ARG_KEY
import com.soloscape.util.Constants.Routes.WRITE_ID_ARG_KEY
import com.soloscape.util.Constants.Routes.WRITE_FELT_ROUTE

sealed class ScreensRoutes(val route: String) {
    data object DashboardRoute : ScreensRoutes(route = DASHBOARD_ROUTE)

    data object FeltRoute : ScreensRoutes(route = FELT_ROUTE)
    data object WriteFeltRoute : ScreensRoutes(route = "$WRITE_FELT_ROUTE?$WRITE_ID_ARG_KEY={$WRITE_ID_ARG_KEY}") {
        fun passWriteId(writeId: Int) = "$WRITE_FELT_ROUTE?$WRITE_ID_ARG_KEY=$writeId"
    }

    data object IdeaRoute : ScreensRoutes(route = IDEA_ROUTE)
    data object NoteIdeaRoute : ScreensRoutes(route = "$NOTE_IDEA_ROUTE?$NOTE_ID_ARG_KEY={$NOTE_ID_ARG_KEY}&$NOTE_COLOR_ARG_KEY={$NOTE_COLOR_ARG_KEY}"){
        fun passNoteId(noteId: Int?, noteColor: Int) = "$NOTE_IDEA_ROUTE?$NOTE_ID_ARG_KEY=$noteId&$NOTE_COLOR_ARG_KEY=$noteColor"
    }
}
