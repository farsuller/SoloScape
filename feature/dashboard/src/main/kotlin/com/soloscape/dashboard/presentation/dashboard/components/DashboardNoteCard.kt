package com.soloscape.dashboard.presentation.dashboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.soloscape.database.domain.model.Note
import com.soloscape.ui.theme.SoloScapeTheme
import com.soloscape.ui.theme.robotoBoldFontFamily
import com.soloscape.util.Constants.TestTags.NOTE_ITEM
import com.soloscape.util.clickableWithoutRipple

@Composable
fun DashboardNoteCard(
    note: Note,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    onNoteClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .wrapContentHeight()
            .clickableWithoutRipple { onNoteClick() }
            .testTag(NOTE_ITEM),
    ) {
        Canvas(
            modifier = Modifier.matchParentSize(),
        ) {
            val clipPath = Path().apply {
                lineTo(size.width - cutCornerSize.toPx(), 0f)
                lineTo(size.width, cutCornerSize.toPx())
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }

            clipPath(clipPath) {
                drawRoundRect(
                    color = Color(note.color),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx()),
                )
                drawRoundRect(
                    color = Color(ColorUtils.blendARGB(note.color, 0x000000, 0.2f)),
                    topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                    size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                    cornerRadius = CornerRadius(cornerRadius.toPx()),
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text(
                modifier = Modifier.width(140.dp),
                text = note.title,
                style = TextStyle(
                    fontFamily = robotoBoldFontFamily,
                    fontSize = MaterialTheme.typography.labelLarge.fontSize,
                    fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
                ),
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = note.content,
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                ),
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
            )
        }
    }
}

@Preview
@Composable
fun NoteItemPreview() {
    SoloScapeTheme {
        Surface {
            DashboardNoteCard(
                note = Note(
                    title = "Test",
                    content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                        "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur",
                    timestamp = 0,
                    color = 0,
                ),
            )
        }
    }
}
