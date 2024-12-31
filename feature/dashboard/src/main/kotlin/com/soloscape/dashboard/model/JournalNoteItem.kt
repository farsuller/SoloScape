package com.soloscape.dashboard.model

import com.soloscape.database.domain.model.Journal
import com.soloscape.database.domain.model.Note
import com.soloscape.ui.Reaction

sealed class JournalNoteItem {
    data class NoteItem(val note: Note) : JournalNoteItem()
    data class WriteItem(val write: Journal) : JournalNoteItem()
}

val sampleCombinedList: List<JournalNoteItem> = listOf(
    JournalNoteItem.NoteItem(
        note = Note(
            title = "Meeting Notes",
            content = "Discuss project roadmap and next steps.",
            timestamp = 1672531200000L, // Example timestamp
            color = 0xFFFFD700.toInt(), // Gold
            id = 1,
        ),
    ),
    JournalNoteItem.WriteItem(
        write = Journal(
            id = 1,
            mood = Reaction.Happy.name,
            title = "Weekend Trip",
            content = "Had a wonderful weekend trip to the mountains!",
            date = 1672617600000L, // Example timestamp
        ),
    ),
    JournalNoteItem.NoteItem(
        note = Note(
            title = "Grocery List",
            content = "Milk, Bread, Eggs, Butter, Vegetables.",
            timestamp = 1672704000000L, // Example timestamp
            color = 0xFF00FF00.toInt(), // Green
            id = 2,
        ),
    ),
    JournalNoteItem.WriteItem(
        write = Journal(
            id = 2,
            mood = Reaction.Angry.name,
            title = "Daily Reflection",
            content = "Today, I focused on personal growth and mindfulness.",
            date = 1672790400000L, // Example timestamp
        ),
    ),
    JournalNoteItem.NoteItem(
        note = Note(
            title = "App Design Ideas",
            content = "Sketch ideas for a new mobile app.",
            timestamp = 1672876800000L, // Example timestamp
            color = 0xFF0000FF.toInt(), // Blue
            id = 3,
        ),
    ),
)
