package com.soloscape.idea.screen

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.soloscape.database.data.local.ScapeDatabase
import com.soloscape.database.data.repository.NoteRepositoryImpl
import com.soloscape.database.domain.model.Note
import com.soloscape.database.domain.usecase.NotesUseCases
import com.soloscape.database.domain.usecase.note.AddNote
import com.soloscape.database.domain.usecase.note.DeleteNote
import com.soloscape.database.domain.usecase.note.GetNote
import com.soloscape.database.domain.usecase.note.GetNotes
import com.soloscape.idea.presentations.idea.IdeaScreen
import com.soloscape.idea.presentations.idea.IdeaViewModel
import com.soloscape.idea.presentations.note.NoteScreen
import com.soloscape.idea.presentations.note.NoteViewModel
import com.soloscape.idea.presentations.note.components.NoteEvent
import com.soloscape.ui.theme.SoloScapeTheme
import com.soloscape.util.Constants.Routes.NOTE_COLOR_ARG_KEY
import com.soloscape.util.Constants.Routes.NOTE_ID_ARG_KEY
import com.soloscape.util.Constants.TestTags.BACK_PRESSED
import com.soloscape.util.Constants.TestTags.CONTENT_TEXT_FIELD
import com.soloscape.util.Constants.TestTags.GENERIC_FAB_ADD
import com.soloscape.util.Constants.TestTags.GENERIC_FAB_NAVIGATE
import com.soloscape.util.Constants.TestTags.TITLE_TEXT_FIELD
import com.soloscape.util.routes.ScreensRoutes
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NotesScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var ideaViewModel: IdeaViewModel
    private lateinit var noteViewModel: NoteViewModel

    private lateinit var notesUseCases: NotesUseCases

    @ExperimentalAnimationApi
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, ScapeDatabase::class.java)
            .build()

        val noteDao = db.noteDao
        val repository = NoteRepositoryImpl(noteDao)

        notesUseCases = NotesUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository),
        )

        ideaViewModel = IdeaViewModel(notesUseCases = notesUseCases)
        noteViewModel = NoteViewModel(
            noteUseCases = notesUseCases,
            savedStateHandle = createSavedStateHandle(),
        )
    }

    private fun createSavedStateHandle(noteId: Int = 1) =
        SavedStateHandle(mapOf(NOTE_ID_ARG_KEY to noteId))

    private fun setupNoteScreen() {
        composeRule.setContent {
            SoloScapeTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ScreensRoutes.IdeaRoute.route,
                ) {
                    composable(route = ScreensRoutes.IdeaRoute.route) {
                        val ideaState by ideaViewModel.ideaState.collectAsState()
                        var deleteNoteDialogOpened by remember { mutableStateOf(false) }
                        var noteDeleteSelected by remember { mutableStateOf<Note?>(null) }
                        IdeaScreen(
                            navigateToNoteWithArgs = {
                                navController.navigate(
                                    ScreensRoutes.NoteIdeaRoute.passNoteId(
                                        noteId = it.noteId,
                                        noteColor = it.noteColor,
                                    ),
                                )
                            },
                            navigateToNote = { navController.navigate(ScreensRoutes.NoteIdeaRoute.route) },
                            onBackPressed = { navController.popBackStack() },
                            ideaState = ideaState,
                            onDeleteClick = { note ->
                                noteDeleteSelected = note
                                deleteNoteDialogOpened = true
                            },
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
                    ) { args ->
                        val color = args.arguments?.getInt(NOTE_COLOR_ARG_KEY) ?: -1
                        val noteState by noteViewModel.noteState.collectAsState()

                        NoteScreen(
                            noteColor = color,
                            onBackPressed = { navController.popBackStack() },
                            onSaveClicked = { noteViewModel.onEvent(NoteEvent.SaveNote(onSuccess = { navController.popBackStack() })) },
                            noteState = noteState,
                            onValueChangeTitle = { noteViewModel.onEvent(NoteEvent.EnteredTitle(it)) },
                            onFocusChangeTitle = { noteViewModel.onEvent(NoteEvent.ChangeTitleFocus(it)) },
                            onValueChangeContent = { noteViewModel.onEvent(NoteEvent.EnteredContent(it)) },
                            onFocusChangeContent = { noteViewModel.onEvent(NoteEvent.ChangeContentFocus(it)) },
                            onChangeColor = { noteViewModel.onEvent(NoteEvent.ChangeColor(it)) },
                        )
                    }
                }
            }
        }
    }

    @Test
    fun clickAddButtonThenClickBack() {
        setupNoteScreen()
        with(composeRule) {
            onNodeWithTag(GENERIC_FAB_NAVIGATE).performClick()
            onNodeWithTag(BACK_PRESSED).performClick()
        }
    }

    @Test
    fun addNoteItemThenClickSave() {
        setupNoteScreen()
        with(composeRule) {
            onNodeWithTag(GENERIC_FAB_NAVIGATE).performClick()
            onNodeWithTag(TITLE_TEXT_FIELD).performTextInput("test-title")
            onNodeWithTag(CONTENT_TEXT_FIELD).performTextInput("test-content")
            onNodeWithTag(GENERIC_FAB_ADD).performClick()
        }
    }

    @Test
    fun addNoteItemEditJournalThenClickSave() {
        setupNoteScreen()
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
    fun addMultipleNoteItemClickSave() {
        setupNoteScreen()
        with(composeRule) {
            for (i in 1..7) {
                waitForIdle()
                onNodeWithTag(GENERIC_FAB_NAVIGATE).performClick()

                onNodeWithTag(TITLE_TEXT_FIELD).performTextClearance()
                onNodeWithTag(CONTENT_TEXT_FIELD).performTextClearance()
                waitForIdle()
                onNodeWithTag(TITLE_TEXT_FIELD).performTextInput("$i title")
                onNodeWithTag(CONTENT_TEXT_FIELD).performTextInput("$i content")
                waitForIdle()
                onNodeWithTag(GENERIC_FAB_ADD).performClick()
            }
        }
    }
}
