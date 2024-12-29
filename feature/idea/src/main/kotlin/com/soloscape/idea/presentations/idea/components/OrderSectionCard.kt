package com.soloscape.idea.presentations.idea.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soloscape.ui.theme.SoloScapeTheme
import com.soloscape.util.NoteOrder
import com.soloscape.util.OrderType

@Composable
fun OrderSectionCard(
    modifier: Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChange: (NoteOrder) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DefaultChipButton(
                text = "Title",
                isSelected = noteOrder is NoteOrder.Title,
                onSelectionChanged = { onOrderChange(NoteOrder.Title(noteOrder.orderType)) },
            )
            DefaultChipButton(
                text = "Date",
                isSelected = noteOrder is NoteOrder.Date,
                onSelectionChanged = { onOrderChange(NoteOrder.Date(noteOrder.orderType)) },
            )
            DefaultChipButton(
                text = "Color",
                isSelected = noteOrder is NoteOrder.Color,
                onSelectionChanged = { onOrderChange(NoteOrder.Color(noteOrder.orderType)) },
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DefaultChipButton(
                text = "Ascending",
                isSelected = noteOrder.orderType is OrderType.Ascending,
                onSelectionChanged = {
                    onOrderChange(noteOrder.copy(OrderType.Ascending))
                },
            )
            DefaultChipButton(
                text = "Descending",
                isSelected = noteOrder.orderType is OrderType.Descending,
                onSelectionChanged = {
                    onOrderChange(noteOrder.copy(OrderType.Descending))
                },
            )
        }
    }
}

@Preview
@Composable
fun OrderSectionPreview() {
    SoloScapeTheme {
        Surface {
            OrderSectionCard(
                onOrderChange = {},
            )
        }
    }
}
