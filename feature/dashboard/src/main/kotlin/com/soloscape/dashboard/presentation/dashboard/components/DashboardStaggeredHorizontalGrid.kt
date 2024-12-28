package com.soloscape.dashboard.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soloscape.dashboard.model.JournalNoteItem
import com.soloscape.dashboard.model.sampleCombinedList
import com.soloscape.ui.theme.SoloScapeTheme
import com.soloscape.ui.theme.robotoBoldFontFamily

@Composable
fun DashboardStaggeredHorizontalGrid(
    combinedList: List<JournalNoteItem>,
    onNoteClick: () -> Unit = {},
    onJournalClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Text(
            text = "Organize Your Day",
            style = TextStyle(
                fontFamily = robotoBoldFontFamily,
                fontSize = MaterialTheme.typography.labelLarge.fontSize,
                fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
            ),
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = "All your notes and journals in one place.",
            style = TextStyle(
                fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
            ),
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis,
        )

        LazyHorizontalStaggeredGrid(
            rows = StaggeredGridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth().height(300.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalItemSpacing = 10.dp,
        ) {
            items(combinedList) { item ->
                when (item) {
                    is JournalNoteItem.NoteItem -> DashboardNoteCard(
                        note = item.note,
                        onNoteClick = onNoteClick,
                    )

                    is JournalNoteItem.WriteItem -> DashboardJournalCard(
                        write = item.write,
                        onJournalClick = onJournalClick,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
internal fun DashboardStaggeredHorizontalGridPreview() {
    SoloScapeTheme {
        Surface {
            DashboardStaggeredHorizontalGrid(combinedList = sampleCombinedList)
        }
    }
}
