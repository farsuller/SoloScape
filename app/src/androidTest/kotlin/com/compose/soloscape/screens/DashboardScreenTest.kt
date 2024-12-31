package com.compose.soloscape.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.compose.soloscape.TestActivity
import com.soloscape.dashboard.presentation.dashboard.DashboardScreen
import com.soloscape.dashboard.presentation.dashboard.JournalNoteState
import com.soloscape.dashboard.presentation.dashboard.YourNameState
import com.soloscape.util.Constants.TestTags.HI
import com.soloscape.util.Constants.TestTags.YOUR_NAME_TEXT_FIELD
import com.soloscape.util.routes.ScreensRoutes
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class DashboardScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<TestActivity>()

    @ExperimentalAnimationApi
    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = ScreensRoutes.DashboardRoute.route
            ) {
                composable(route = ScreensRoutes.DashboardRoute.route) {
                    DashboardScreen(
                        yourNameState = YourNameState(
                            text = "",
                            hint = "",
                            isHintVisible = true
                        ), journalNoteState = JournalNoteState()
                    )
                }
            }
        }
    }

    @Test
    fun greetingsAndYourNameIsDisplayed() {
        composeRule.onNodeWithTag(HI).assertIsDisplayed()

        composeRule
            .onNodeWithTag(YOUR_NAME_TEXT_FIELD)
            .performTextInput("test-your-name")
    }
}