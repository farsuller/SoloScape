package com.soloscape.felt.screen

import android.content.Context
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.compose.ui.test.swipeUp
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.soloscape.database.data.local.ScapeDatabase
import com.soloscape.database.data.repository.JournalRepositoryImpl
import com.soloscape.database.domain.usecase.JournalUseCases
import com.soloscape.database.domain.usecase.journal.AddJournal
import com.soloscape.database.domain.usecase.journal.DeleteAllJournal
import com.soloscape.database.domain.usecase.journal.DeleteJournal
import com.soloscape.database.domain.usecase.journal.GetJournalByFiltered
import com.soloscape.database.domain.usecase.journal.GetJournalById
import com.soloscape.database.domain.usecase.journal.GetJournals
import com.soloscape.felt.presentations.felt.JournalScreen
import com.soloscape.felt.presentations.felt.JournalViewModel
import com.soloscape.felt.presentations.felt.components.JournalEvent
import com.soloscape.felt.presentations.write.WriteScreen
import com.soloscape.felt.presentations.write.WriteViewModel
import com.soloscape.felt.presentations.write.components.WriteEvent
import com.soloscape.ui.Reaction
import com.soloscape.ui.theme.SoloScapeTheme
import com.soloscape.util.Constants.Routes.WRITE_ID_ARG_KEY
import com.soloscape.util.Constants.TestTags.BACK_PRESSED
import com.soloscape.util.Constants.TestTags.CONTENT_TEXT_FIELD
import com.soloscape.util.Constants.TestTags.DELETE
import com.soloscape.util.Constants.TestTags.DELETE_YES_DIALOG
import com.soloscape.util.Constants.TestTags.GENERIC_FAB_ADD
import com.soloscape.util.Constants.TestTags.GENERIC_FAB_NAVIGATE
import com.soloscape.util.Constants.TestTags.HORIZONTAL_PAGER
import com.soloscape.util.Constants.TestTags.MORE_VERT_CLICKED
import com.soloscape.util.Constants.TestTags.TITLE_TEXT_FIELD
import com.soloscape.util.Constants.TestTags.VERTICAL_SCROLL
import com.soloscape.util.routes.ScreensRoutes
import com.soloscape.util.toEpochMilliOrNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class JournalScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var journalViewModel: JournalViewModel
    private lateinit var writeViewModel: WriteViewModel

    private lateinit var writeUseCases: JournalUseCases

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, ScapeDatabase::class.java)
            .build()

        val journalDao = db.journalDao
        val repository = JournalRepositoryImpl(journalDao)

        writeUseCases = JournalUseCases(
            getJournals = GetJournals(repository),
            getJournalById = GetJournalById(repository),
            getJournalByFiltered = GetJournalByFiltered(repository),
            addJournal = AddJournal(repository),
            deleteJournal = DeleteJournal(repository),
            deleteAllJournal = DeleteAllJournal(repository),
        )

        journalViewModel = JournalViewModel(writeUseCases = writeUseCases)
        writeViewModel = WriteViewModel(
            writeUseCases = writeUseCases,
            savedStateHandle = createSavedStateHandle(),
        )
    }

    private fun createSavedStateHandle(writeId: Int = 1) =
        SavedStateHandle(mapOf(WRITE_ID_ARG_KEY to writeId))

    private fun setupJournalScreen() {
        composeRule.setContent {
            SoloScapeTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ScreensRoutes.FeltRoute.route,
                ) {
                    composable(route = ScreensRoutes.FeltRoute.route) {
                        val journalState by journalViewModel.journalState.collectAsState()

                        JournalScreen(
                            journalState = journalState,
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
                            onDeleteAllConfirmed = { journalViewModel.onEvent(JournalEvent.DeleteAll) },
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
                        val writeState by writeViewModel.writeState.collectAsState()
                        val pagerState = rememberPagerState(pageCount = { Reaction.entries.size })
                        val pageNumber by remember { derivedStateOf { pagerState.currentPage } }
                        val reaction = Reaction.entries[pageNumber]

                        WriteScreen(
                            onBackPressed = { navController.popBackStack() },
                            pagerState = pagerState,
                            reaction = reaction,
                            onSaveClicked = { writeViewModel.onEvent(WriteEvent.UpsertWriteItem(reaction = reaction, onSuccess = { navController.popBackStack() })) },
                            onDateTimeUpdated = { writeViewModel.onEvent(WriteEvent.SelectedDateTime(date = it.toEpochMilliOrNull())) },
                            onDeleteConfirmed = { write -> writeViewModel.onEvent(WriteEvent.DeleteWriteItem(writeItem = write, onSuccess = { navController.popBackStack() })) },
                            writeState = writeState,
                            onValueChangeTitle = { writeViewModel.onEvent(WriteEvent.EnteredTitle(it)) },
                            onFocusChangeTitle = { writeViewModel.onEvent(WriteEvent.ChangeTitleFocus(it)) },
                            onValueChangeContent = { writeViewModel.onEvent(WriteEvent.EnteredContent(it)) },
                            onFocusChangeContent = { writeViewModel.onEvent(WriteEvent.ChangeContentFocus(it)) },
                        )
                    }
                }
            }
        }
    }

    @Test
    fun clickAddButtonThenClickBack() {
        setupJournalScreen()
        with(composeRule) {
            onNodeWithTag(GENERIC_FAB_NAVIGATE).performClick()
            onNodeWithTag(BACK_PRESSED).performClick()
        }
    }

    @Test
    fun addJournalItemThenClickSave() {
        setupJournalScreen()
        with(composeRule) {
            onNodeWithTag(GENERIC_FAB_NAVIGATE).performClick()
            onNodeWithTag(TITLE_TEXT_FIELD).performTextInput("test-title")
            onNodeWithTag(CONTENT_TEXT_FIELD).performTextInput("test-content")
            onNodeWithTag(GENERIC_FAB_ADD).performClick()
        }
    }

    @Test
    fun addJournalItemThenClickSaveThenDeleteAll() {
        setupJournalScreen()
        with(composeRule) {
            onNodeWithTag(GENERIC_FAB_NAVIGATE).performClick()

            onNodeWithTag(TITLE_TEXT_FIELD).performTextInput("test-title")
            onNodeWithTag(CONTENT_TEXT_FIELD).performTextInput("test-content")
            onNodeWithTag(GENERIC_FAB_ADD).performClick()

            onNodeWithTag(MORE_VERT_CLICKED).performClick()
            onNodeWithTag(DELETE).performClick()
            onNodeWithTag(DELETE_YES_DIALOG).performClick()
        }
    }

    @Test
    fun addJournalItemEditJournalThenClickSave() {
        setupJournalScreen()
        with(composeRule) {
            onNodeWithTag(GENERIC_FAB_NAVIGATE).performClick()
            onNodeWithTag(TITLE_TEXT_FIELD).performTextInput("test-title")
            onNodeWithTag(CONTENT_TEXT_FIELD).performTextInput("test-content")
            onNodeWithTag(GENERIC_FAB_ADD).performClick()

            onNodeWithText("test-title").performClick()

            onNodeWithTag(TITLE_TEXT_FIELD).performTextInput("2")
            onNodeWithTag(CONTENT_TEXT_FIELD).performTextInput("2")
            onNodeWithTag(GENERIC_FAB_ADD).performClick()
        }
    }

    @Test
    fun swipeReactionsAndThenAddJournalItemThenSave() {
        setupJournalScreen()
        with(composeRule) {
            onNodeWithTag(GENERIC_FAB_NAVIGATE).performClick()
            repeat(Reaction.entries.size - 1) {
                onNodeWithTag(HORIZONTAL_PAGER).performTouchInput {
                    swipeLeft(startX = width * 0.9f, endX = width * 0.1f, durationMillis = 300)
                }
            }

            onNodeWithTag(TITLE_TEXT_FIELD).performTextInput("swipe-reaction-title")
            onNodeWithTag(CONTENT_TEXT_FIELD).performTextInput("swipe-reaction-content")
            onNodeWithTag(GENERIC_FAB_ADD).performClick()
        }
    }

    @Test
    fun editSwipeReactionsThenAddJournalItemThenSave() {
        setupJournalScreen()
        with(composeRule) {
            onNodeWithTag(GENERIC_FAB_NAVIGATE).performClick()
            repeat(Reaction.entries.size - 1) {
                onNodeWithTag(HORIZONTAL_PAGER).performTouchInput {
                    swipeLeft(startX = width * 0.9f, endX = width * 0.1f, durationMillis = 300)
                }
            }

            onNodeWithTag(TITLE_TEXT_FIELD).performTextInput("swipe-reaction-title")
            onNodeWithTag(CONTENT_TEXT_FIELD).performTextInput("swipe-reaction-content")
            onNodeWithTag(GENERIC_FAB_ADD).performClick()

            onNodeWithText("swipe-reaction-title").performClick()

            repeat(Reaction.entries.size - 1) {
                onNodeWithTag(HORIZONTAL_PAGER).performTouchInput {
                    swipeRight(startX = width * 0.1f, endX = width * 0.9f, durationMillis = 300)
                }
            }

            onNodeWithTag(TITLE_TEXT_FIELD).performTextClearance()
            onNodeWithTag(CONTENT_TEXT_FIELD).performTextClearance()

            onNodeWithTag(TITLE_TEXT_FIELD).performTextInput("swipe-reaction-title-edited")
            onNodeWithTag(CONTENT_TEXT_FIELD).performTextInput("swipe-reaction-title-edited")
            onNodeWithTag(GENERIC_FAB_ADD).performClick()
        }
    }

    @Test
    fun swipeThroughReactionsAndAddJournal() {
        setupJournalScreen()
        with(composeRule) {
            onNodeWithTag(GENERIC_FAB_NAVIGATE).performClick()

            Reaction.entries.forEachIndexed { index, reaction ->
                val title = "Title-${reaction.name}"
                val content = "Content for ${reaction.name}"

                waitForIdle()

                onNodeWithTag(TITLE_TEXT_FIELD).performTextClearance()
                onNodeWithTag(CONTENT_TEXT_FIELD).performTextClearance()

                waitForIdle()

                onNodeWithTag(TITLE_TEXT_FIELD).performTextInput(title)
                onNodeWithTag(CONTENT_TEXT_FIELD).performTextInput(content)

                onNodeWithTag(GENERIC_FAB_ADD).performClick()

                waitForIdle()

                val scrollRepetitions = if (index > 6) 5 else 2
                repeat(scrollRepetitions) {
                    onNodeWithTag(VERTICAL_SCROLL).performTouchInput {
                        swipeUp(startY = height * 0.99f, endY = 0f, durationMillis = 600)
                    }
                    waitForIdle()
                }

                onNodeWithTag(GENERIC_FAB_NAVIGATE).performClick()

                if (index < Reaction.entries.size - 1) {
                    onNodeWithTag(HORIZONTAL_PAGER).performTouchInput {
                        swipeLeft(startX = width * 0.9f, endX = width * 0.1f, durationMillis = 300)
                    }
                }

                waitForIdle()
            }
        }
    }
}
