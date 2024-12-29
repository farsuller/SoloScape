package com.soloscape.felt.presentations.felt

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soloscape.database.domain.model.Write
import com.soloscape.felt.presentations.felt.components.JournalCard
import com.soloscape.ui.R
import com.soloscape.ui.components.EmptyListContainer
import com.soloscape.util.clickableWithoutRipple
import kotlinx.coroutines.delay
import java.time.LocalDate

@Composable
internal fun FeltContent(
    paddingValues: PaddingValues,
    writes: Map<LocalDate, List<Write>>? = null,
    onClickCard: (Int?) -> Unit,
) {
    val visibleHeaders = remember { mutableStateListOf<LocalDate>() }
    val visibleItems = remember { mutableStateListOf<Int>() }

    if (writes != null) {
        LaunchedEffect(writes) {
            writes.keys.forEachIndexed { index, localDate ->
                delay(30L * index)
                visibleHeaders.add(localDate)
            }

            writes.values.flatten().forEachIndexed { index, _ ->
                delay(100L * index)
                visibleItems.add(index)
            }
        }

        if (writes.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .navigationBarsPadding()
                    .padding(top = paddingValues.calculateTopPadding()),
            ) {
                writes.forEach { (localDate, write) ->
                    stickyHeader(key = localDate) {
                        AnimatedVisibility(
                            visible = visibleHeaders.contains(localDate),
                            enter = fadeIn(animationSpec = tween(durationMillis = 1000)) +
                                    slideInVertically(
                                        initialOffsetY = { -it }, // Slide in from above
                                        animationSpec = tween(durationMillis = 1200), // Adjust the duration as needed
                                    ),
                        ) {
                            DateHeader(localDate = localDate)
                        }
                    }
                    itemsIndexed(items = write, key = { _, w -> w.id.toString() }) { index, item ->
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
                            JournalCard(
                                write = item,
                                modifier = Modifier.clickableWithoutRipple { onClickCard(item.id) },
                            )
                        }
                    }
                }
            }
        } else {
            EmptyListContainer(
                title = stringResource(R.string.feeling_something_title),
                subtitle = stringResource(R.string.feeling_something_subtitle)
            )
        }
    }
}

@Composable
internal fun DateHeader(localDate: LocalDate) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(horizontalAlignment = Alignment.End) {
            Text(
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    fontSize = 25.sp,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                ),
                text = localDate.dayOfMonth.toString(),
            )

            Text(
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                ),
                text = localDate.dayOfWeek.toString().take(3),
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(horizontalAlignment = Alignment.Start) {
            Text(
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                ),
                text = localDate.month.toString().lowercase().replaceFirstChar { it.titlecase() },
            )

            Text(
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                ),
                text = localDate.year.toString(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
internal fun DateHeaderPreview() {
    DateHeader(localDate = LocalDate.now())
}
