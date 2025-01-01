package com.compose.soloscape.screens

import androidx.activity.ComponentActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.compose.soloscape.TestActivity
import com.soloscape.dashboard.presentation.dashboard.DashboardScreen
import com.soloscape.dashboard.presentation.dashboard.JournalNoteState
import com.soloscape.dashboard.presentation.dashboard.YourNameState
import com.soloscape.database.domain.model.Journal
import com.soloscape.database.domain.model.Note
import com.soloscape.database.domain.model.Note.Companion.noteColors
import com.soloscape.felt.presentations.felt.FeltScreen
import com.soloscape.felt.presentations.felt.components.FeltState
import com.soloscape.felt.presentations.write.WriteScreen
import com.soloscape.felt.presentations.write.components.WriteState
import com.soloscape.idea.presentations.idea.IdeaScreen
import com.soloscape.idea.presentations.idea.components.IdeaState
import com.soloscape.idea.presentations.note.NoteScreen
import com.soloscape.idea.presentations.note.components.NoteState
import com.soloscape.ui.Reaction
import com.soloscape.ui.theme.SoloScapeTheme
import com.soloscape.util.Constants.Routes.NOTE_COLOR_ARG_KEY
import com.soloscape.util.Constants.Routes.NOTE_ID_ARG_KEY
import com.soloscape.util.Constants.Routes.WRITE_ID_ARG_KEY
import com.soloscape.util.Constants.TestTags.BACK_PRESSED
import com.soloscape.util.Constants.TestTags.CONTENT_TEXT_FIELD
import com.soloscape.util.Constants.TestTags.GENERIC_FAB_ADD
import com.soloscape.util.Constants.TestTags.GENERIC_FAB_NAVIGATE
import com.soloscape.util.Constants.TestTags.HI
import com.soloscape.util.Constants.TestTags.JOURNAL_CARD
import com.soloscape.util.Constants.TestTags.JOURNAL_ITEM
import com.soloscape.util.Constants.TestTags.NOTE_CARD
import com.soloscape.util.Constants.TestTags.NOTE_ITEM
import com.soloscape.util.Constants.TestTags.TITLE_TEXT_FIELD
import com.soloscape.util.Constants.TestTags.YOUR_NAME_TEXT_FIELD
import com.soloscape.util.routes.ScreensRoutes
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@HiltAndroidTest
class SoloscapeEndToEndTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<TestActivity>()

    private var yourNameState by mutableStateOf(YourNameState())

    private var feltState by mutableStateOf(FeltState())
    private var writeState by mutableStateOf(WriteState())

    private var ideaState by mutableStateOf(IdeaState())
    private var noteState by mutableStateOf(NoteState())

    private val dummyNotes = listOf(
        Note(
            id = 1,
            title = "Note 1",
            content = "Note Content 1",
            timestamp = LocalDate.of(2025, 1, 1).toEpochDay(),
            color = noteColors.random().toArgb(),
        ),
        Note(
            id = 2,
            title = "Note 2",
            content = "Note Content 2",
            timestamp = LocalDate.of(2025, 1, 1).toEpochDay(),
            color = noteColors.random().toArgb(),
        ),
        Note(
            id = 3,
            title = "Note 3",
            content = "Note Content 3",
            timestamp = LocalDate.of(2025, 1, 1).toEpochDay(),
            color = noteColors.random().toArgb(),
        ),
    )

    private val dummyJournals = listOf(
        Journal(
            id = 1,
            mood = "Happy",
            title = "A Wonderful Day",
            content = "Today was an amazing day! I went hiking and enjoyed the fresh air.",
            date = LocalDate.of(2025, 1, 1).toEpochDay(),
        ),
        Journal(
            id = 2,
            mood = "Calm",
            title = "Reflections",
            content = "Spent some time thinking about the year ahead and setting goals.",
            date = LocalDate.of(2025, 1, 2).toEpochDay(),
        ),
        Journal(
            id = 3,
            mood = "Neutral",
            title = "New Beginnings",
            content = "Started a new project today, and I can't wait to see where it leads!",
            date = LocalDate.of(2025, 1, 3).toEpochDay(),
        ),
    )

    val journals = mapOf(
        LocalDate.of(2025, 1, 1) to listOf(dummyJournals[0]),
        LocalDate.of(2025, 1, 2) to listOf(dummyJournals[1]),
        LocalDate.of(2025, 1, 3) to listOf(dummyJournals[2]),
    )

    @ExperimentalAnimationApi
    @Before
    fun setUp() {
        hiltRule.inject()

        composeRule.setContent {
            SoloScapeTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ScreensRoutes.DashboardRoute.route,
                ) {
                    composable(route = ScreensRoutes.DashboardRoute.route) {
                        DashboardScreen(
                            navigationToFelt = { navController.navigate(ScreensRoutes.FeltRoute.route) },
                            navigationToIdea = { navController.navigate(ScreensRoutes.IdeaRoute.route) },
                            onValueChange = { yourNameState = yourNameState.copy(text = it) },
                            onFocusChange = {
                                yourNameState = yourNameState.copy(
                                    isHintVisible = !it.isFocused && yourNameState.text.isBlank(),
                                )
                            },
                            onNoteClick = { navController.navigate(ScreensRoutes.IdeaRoute.route) },
                            onJournalClick = { navController.navigate(ScreensRoutes.FeltRoute.route) },
                            yourNameState = yourNameState,
                            journalNoteState = JournalNoteState(),
                        )
                    }

                    composable(route = ScreensRoutes.FeltRoute.route) {
                        FeltScreen(
                            journalState = feltState,
                            navigateToWrite = {
                                navController.navigate(ScreensRoutes.WriteFeltRoute.route)
                            },
                            navigateToWriteWithArgs = {
                                navController.navigate(
                                    ScreensRoutes.WriteFeltRoute.passWriteId(
                                        writeId = it ?: -1,
                                    ),
                                )
                            },
                            dateIsSelected = false,
                            onDateSelected = {},
                            onDateReset = {},
                            onBackPressed = { navController.popBackStack() },
                            onDeleteAllConfirmed = {},
                        )
                    }

                    composable(
                        route = ScreensRoutes.WriteFeltRoute.route,
                        arguments = listOf(
                            navArgument(name = WRITE_ID_ARG_KEY) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                        ),
                    ) {
                        val pagerState = rememberPagerState(pageCount = { Reaction.entries.size })
                        val pageNumber by remember { derivedStateOf { pagerState.currentPage } }
                        val reaction = Reaction.entries[pageNumber]
                        WriteScreen(
                            onBackPressed = { navController.popBackStack() },
                            pagerState = pagerState,
                            reaction = reaction,
                            onSaveClicked = { navController.popBackStack() },
                            onDateTimeUpdated = {},
                            onDeleteConfirmed = {},
                            writeState = writeState,
                            onValueChangeTitle = { title ->
                                writeState = writeState.copy(title = title)
                            },
                            onFocusChangeTitle = { title ->
                                writeState =
                                    writeState.copy(titleHintVisible = !title.isFocused && writeState.title.isBlank())
                            },
                            onValueChangeContent = { content ->
                                writeState = writeState.copy(content = content)
                            },
                            onFocusChangeContent = { content ->
                                writeState =
                                    writeState.copy(contentHintVisible = !content.isFocused && writeState.content.isBlank())
                            },
                        )
                    }

                    composable(route = ScreensRoutes.IdeaRoute.route) {
                        IdeaScreen(
                            navigateToNoteWithArgs = {
                                navController.navigate(
                                    ScreensRoutes.NoteIdeaRoute.passNoteId(
                                        noteId = it.noteId,
                                        noteColor = it.noteColor,
                                    ),
                                )
                            },
                            navigateToNote = {
                                navController.navigate(ScreensRoutes.NoteIdeaRoute.route)
                            },
                            onBackPressed = { navController.popBackStack() },
                            ideaState = ideaState,
                            onDeleteClick = { },
                        )
                    }

                    composable(
                        route = ScreensRoutes.NoteIdeaRoute.route,
                        arguments = listOf(
                            navArgument(name = NOTE_ID_ARG_KEY) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                            navArgument(name = NOTE_COLOR_ARG_KEY) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                        ),
                    ) {
                        val color = it.arguments?.getInt(NOTE_COLOR_ARG_KEY) ?: -1

                        NoteScreen(
                            noteColor = color,
                            onBackPressed = { navController.popBackStack() },
                            onSaveClicked = { navController.popBackStack() },
                            noteState = noteState,
                            onValueChangeTitle = { title ->
                                noteState = noteState.copy(title = title)
                            },
                            onFocusChangeTitle = { title ->
                                noteState =
                                    noteState.copy(titleHintVisible = !title.isFocused && noteState.title.isBlank())
                            },
                            onValueChangeContent = { content ->
                                noteState = noteState.copy(content = content)
                            },
                            onFocusChangeContent = { content ->
                                noteState =
                                    noteState.copy(contentHintVisible = !content.isFocused && noteState.content.isBlank())
                            },
                            onChangeColor = { onChangeColor ->
                                noteState = noteState.copy(noteColor = onChangeColor)
                            },
                        )
                    }
                }
            }
        }
    }

    @Test
    fun onClickedNavigateToJournalThenClickBack() {
        composeRule.onNodeWithTag(JOURNAL_CARD).performClick()
        composeRule.onNodeWithTag(BACK_PRESSED).performClick()
    }

    @Test
    fun onNavigateTitleAndContentJournalThenClickBack() {
        composeRule.onNodeWithTag(JOURNAL_CARD).performClick()
        composeRule.onNodeWithTag(GENERIC_FAB_NAVIGATE).performClick()
        composeRule.onNodeWithTag(BACK_PRESSED).performClick()
    }

    @Test
    fun onTextInputTitleAndContentJournalThenClickAdd() {
        composeRule.onNodeWithTag(JOURNAL_CARD).performClick()
        composeRule.onNodeWithTag(GENERIC_FAB_NAVIGATE).performClick()

        composeRule.onNodeWithTag(TITLE_TEXT_FIELD).performTextInput("test-title")
        composeRule.onNodeWithTag(CONTENT_TEXT_FIELD).performTextInput("test-content")

        composeRule.onNodeWithTag(GENERIC_FAB_ADD).performClick()
    }

    @Test
    fun onSelectJournalWithArgs() {
        feltState = feltState.copy(writes = journals)

        composeRule.onNodeWithTag(JOURNAL_CARD).performClick()

        composeRule.onAllNodesWithTag(JOURNAL_ITEM)[0].performClick()
        composeRule.onNodeWithTag(BACK_PRESSED).performClick()

        composeRule.onAllNodesWithTag(JOURNAL_ITEM)[1].performClick()
        composeRule.onNodeWithTag(BACK_PRESSED).performClick()

        composeRule.onAllNodesWithTag(JOURNAL_ITEM)[2].performClick()
        composeRule.onNodeWithTag(BACK_PRESSED).performClick()
    }

    @Test
    fun onClickedNoteCardThenClickBack() {
        composeRule.onNodeWithTag(NOTE_CARD).performClick()
        composeRule.onNodeWithTag(BACK_PRESSED).performClick()
    }

    @Test
    fun onNavigateTitleAndContentNoteThenClickBack() {
        composeRule.onNodeWithTag(NOTE_CARD).performClick()
        composeRule.onNodeWithTag(GENERIC_FAB_NAVIGATE).performClick()
        composeRule.onNodeWithTag(BACK_PRESSED).performClick()
    }

    @Test
    fun onTextInputTitleAndContentNoteThenClickAdd() {
        composeRule.onNodeWithTag(NOTE_CARD).performClick()
        composeRule.onNodeWithTag(GENERIC_FAB_NAVIGATE).performClick()

        composeRule.onNodeWithTag(TITLE_TEXT_FIELD).performTextInput("test-title")
        composeRule.onNodeWithTag(CONTENT_TEXT_FIELD).performTextInput("test-content")

        composeRule.onNodeWithTag(GENERIC_FAB_ADD).performClick()
    }

    @Test
    fun onSelectNoteWithArgs() {
        ideaState = ideaState.copy(notes = dummyNotes)

        composeRule.onNodeWithTag(NOTE_CARD).performClick()

        composeRule.onAllNodesWithTag(NOTE_ITEM)[0].performClick()
        composeRule.onNodeWithTag(BACK_PRESSED).performClick()

        composeRule.onAllNodesWithTag(NOTE_ITEM)[1].performClick()
        composeRule.onNodeWithTag(BACK_PRESSED).performClick()

        composeRule.onAllNodesWithTag(NOTE_ITEM)[2].performClick()
        composeRule.onNodeWithTag(BACK_PRESSED).performClick()
    }
}
