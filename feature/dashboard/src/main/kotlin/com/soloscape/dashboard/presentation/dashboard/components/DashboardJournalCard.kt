package com.soloscape.dashboard.presentation.dashboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soloscape.database.domain.model.Journal
import com.soloscape.ui.Reaction
import com.soloscape.ui.theme.Elevation
import com.soloscape.ui.theme.robotoBoldFontFamily
import com.soloscape.util.Constants.TestTags.JOURNAL_ITEM
import com.soloscape.util.clickableWithoutRipple

@Composable
fun DashboardJournalCard(write: Journal, onJournalClick: () -> Unit = {}) {
    Surface(
        modifier = Modifier
            .width(220.dp)
            .clickableWithoutRipple { onJournalClick() }
            .wrapContentHeight()
            .testTag(JOURNAL_ITEM),
        tonalElevation = Elevation.level1,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
        ) {
            JournalHeader(write = write)

            Text(
                modifier = Modifier.padding(all = 14.dp),
                text = write.content,
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun JournalHeader(write: Journal) {
    val reaction by remember { mutableStateOf(Reaction.valueOf(write.mood.orEmpty())) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(reaction.containerColor)
            .padding(horizontal = 14.dp, vertical = 7.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = reaction.icon),
                    contentDescription = "Mood Icon",
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = reaction.name,
                    color = reaction.contentColor,
                    style = TextStyle(
                        fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                        fontSize = MaterialTheme.typography.labelMedium.fontSize,
                        fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                    ),
                )
            }

            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = write.title,
                color = reaction.contentColor,
                style = TextStyle(
                    fontFamily = robotoBoldFontFamily,
                    fontSize = MaterialTheme.typography.labelLarge.fontSize,
                    fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
                ),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun JournalCardPreview() {
    DashboardJournalCard(
        write = Journal(
            id = 0,
            title = "My Write",
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur",
            mood = Reaction.Happy.name,
            date = 0,
        ),
    )
}
