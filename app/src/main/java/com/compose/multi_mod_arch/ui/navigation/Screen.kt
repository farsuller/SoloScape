package com.compose.multi_mod_arch.ui.navigation

import com.compose.multi_mod_arch.util.Constants.REPORT_SCREEN_ARG_KEY

sealed class Screen (val route: String){
    object Authentication:Screen(route = "auth_screen")
    object Home:Screen(route = "home_screen")
    object Report:Screen(route = "report_screen?$REPORT_SCREEN_ARG_KEY ={$REPORT_SCREEN_ARG_KEY}"){
        fun passReportId(reportId:String) = "report_screen?$REPORT_SCREEN_ARG_KEY=$reportId"
    }
}
