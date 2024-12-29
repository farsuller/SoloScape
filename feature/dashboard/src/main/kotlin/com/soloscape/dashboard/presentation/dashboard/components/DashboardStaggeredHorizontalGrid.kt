package com.soloscape.dashboard.presentation.dashboard.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soloscape.dashboard.model.JournalNoteItem
import com.soloscape.dashboard.model.sampleCombinedList
import com.soloscape.ui.R
import com.soloscape.ui.theme.SoloScapeTheme
import com.soloscape.ui.theme.robotoBoldFontFamily
import kotlinx.coroutines.delay

@Composable
fun DashboardStaggeredHorizontalGrid(
    combinedList: List<JournalNoteItem>,
    onNoteClick: () -> Unit = {},
    onJournalClick: () -> Unit = {},
) {
    val visibleItems = remember { mutableStateListOf<Int>() }

    LaunchedEffect(combinedList) {
        combinedList.forEachIndexed { index, _ ->
            delay(100L * index)
            visibleItems.add(index)
        }
    }
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Text(
            text = stringResource(R.string.organize_your_day_title),
            style = TextStyle(
                fontFamily = robotoBoldFontFamily,
                fontSize = MaterialTheme.typography.labelLarge.fontSize,
                fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
            ),
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = stringResource(R.string.organize_your_day_subtitle),
            style = TextStyle(
                fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
            ),
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis,
        )

        if (combinedList.isNotEmpty()) {
            LazyHorizontalStaggeredGrid(
                rows = StaggeredGridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalItemSpacing = 10.dp,
            ) {
                itemsIndexed(items = combinedList, key = { index, item ->
                    when (item) {
                        is JournalNoteItem.NoteItem -> "note-${item.note.id ?: 0}-$index"
                        is JournalNoteItem.WriteItem -> "write-${item.write.id ?: 0}-$index"
                    }
                }) { index, item ->

                    AnimatedVisibility(
                        visible = index in visibleItems,
                        enter = fadeIn(animationSpec = tween(durationMillis = 600)) +
                            slideInHorizontally(
                                initialOffsetX = { it }, // Start from the right edge
                                animationSpec = tween(durationMillis = 600), // Adjust duration
                            ),
                        exit = fadeOut(animationSpec = tween(durationMillis = 600)) +
                            slideOutHorizontally(
                                targetOffsetX = { it }, // Exit to the right edge
                                animationSpec = tween(durationMillis = 600),
                            ),
                    ) {
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
        } else {
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier
                        .size(190.dp)
                        .padding(10.dp)
                        .align(Alignment.TopStart),
                    painter = painterResource(R.drawable.svg_notes),
                    contentDescription = null,
                )
                Image(
                    modifier = Modifier
                        .size(170.dp)
                        .padding(10.dp)
                        .graphicsLayer(scaleX = -1f)
                        .align(Alignment.BottomEnd),
                    painter = painterResource(R.drawable.svg_journal),
                    contentDescription = null,
                )
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
