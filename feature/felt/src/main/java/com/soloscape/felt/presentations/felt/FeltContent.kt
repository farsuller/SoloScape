package com.soloscape.felt.presentations.felt

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soloscape.database.domain.model.Write
import com.soloscape.felt.presentations.felt.components.EmptyPage
import com.soloscape.felt.presentations.felt.components.WriteCard
import java.time.LocalDate

@Composable
internal fun FeltContent(
    paddingValues: PaddingValues,
    writes: Map<LocalDate, List<Write>>? = null,
    onClick: (Int?) -> Unit,
) {
    if (writes != null) {
        if (writes.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .navigationBarsPadding()
                    .padding(top = paddingValues.calculateTopPadding()),
            ) {
                writes.forEach { (localDate, write) ->
                    stickyHeader(key = localDate) { DateHeader(localDate = localDate) }
                    items(items = write, key = { w -> w.id.toString() }) {
                        WriteCard(write = it, onClick = onClick)
                    }
                }
            }
        } else {
            EmptyPage()
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
                style =
                TextStyle(
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    fontSize = 25.sp,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                ),
                text = String.format("%02d", localDate.dayOfMonth),
            )

            Text(
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight
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
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight
                ),
                text = localDate.month.toString().lowercase().replaceFirstChar { it.titlecase() },
            )

            Text(
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight
                ),
                text = "${localDate.year}",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )
        }
    }
}

@SuppressLint("NewApi")
@Composable
@Preview(showBackground = true)
internal fun DateHeaderPreview() {
    DateHeader(localDate = LocalDate.now())
}
