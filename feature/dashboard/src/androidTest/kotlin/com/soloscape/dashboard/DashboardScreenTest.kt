package com.soloscape.dashboard

import androidx.activity.ComponentActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.soloscape.dashboard.presentation.dashboard.DashboardScreen
import com.soloscape.dashboard.presentation.dashboard.JournalNoteState
import com.soloscape.dashboard.presentation.dashboard.YourNameState
import com.soloscape.ui.theme.SoloScapeTheme
import com.soloscape.util.Constants.TestTags.HI
import com.soloscape.util.Constants.TestTags.YOUR_NAME_TEXT_FIELD
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DashboardScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private var yourNameState by mutableStateOf(YourNameState())

    @ExperimentalAnimationApi
    @Before
    fun setUp() {
        composeRule.setContent {
            SoloScapeTheme {
                DashboardScreen(
                    navigationToFelt = {},
                    navigationToIdea = {},
                    onValueChange = { yourNameState = yourNameState.copy(text = it) },
                    onFocusChange = {
                        yourNameState = yourNameState.copy(
                            isHintVisible = !it.isFocused && yourNameState.text.isBlank(),
                        )
                    },
                    onNoteClick = {},
                    onJournalClick = {},
                    yourNameState = yourNameState,
                    journalNoteState = JournalNoteState(),
                )
            }
        }
    }

    @Test
    fun dashboardYourNameTextFieldInput() {
        with(composeRule) {
            val testInput = "Test Name"
            onNodeWithTag(HI).assertIsDisplayed()
            onNodeWithTag(YOUR_NAME_TEXT_FIELD).performTextInput(testInput)
            onNodeWithTag(YOUR_NAME_TEXT_FIELD).assertTextEquals(testInput)
        }
    }

    @Test
    fun dashboardIdeaAndFeltCardDisplaySpiels() {
        with(composeRule) {
            onNodeWithText("Idea").assertIsDisplayed()
            onNodeWithText("Note down thoughts and inspirations.").assertIsDisplayed()
            onNodeWithText("A notepad to capture your ideas, plans, and creative sparks.").assertIsDisplayed()

            onNodeWithText("Felt").assertIsDisplayed()
            onNodeWithText("Express and reflect on your emotions.").assertIsDisplayed()
            onNodeWithText("A journal designed to help you articulate and process your feelings.").assertIsDisplayed()
        }
    }

    @Test
    fun dashboardStaggeredGridSpiels() {
        with(composeRule) {
            onNodeWithText("Organize Your Day").assertIsDisplayed()
            onNodeWithText("All your notes and journals in one place.").assertIsDisplayed()
        }
    }
}
