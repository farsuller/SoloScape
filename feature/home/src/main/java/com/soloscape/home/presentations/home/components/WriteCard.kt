package com.soloscape.home.presentations.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soloscape.database.domain.model.Write
import com.soloscape.ui.Reaction
import com.soloscape.ui.theme.Elevation
import com.soloscape.util.clickableWithoutRipple
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun WriteCard(write: Write, onClick: (Int?) -> Unit) {
    val localDensity = LocalDensity.current
    var componentHeight by remember { mutableStateOf(0.dp) }

    Row(
        modifier = Modifier.clickableWithoutRipple { onClick(write.id) },
    ) {
        Spacer(modifier = Modifier.width(14.dp))
        Surface(
            modifier = Modifier
                .width(2.dp)
                .height(componentHeight + 14.dp),
            tonalElevation = Elevation.level1,
        ) {
        }
        Spacer(modifier = Modifier.width(20.dp))

        Surface(
            modifier = Modifier
                .clip(shape = Shapes().medium)
                .onGloballyPositioned {
                    componentHeight = with(localDensity) { it.size.height.toDp() }
                },
            tonalElevation = Elevation.level1,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                WriteHeader(write = write)

                Text(
                    modifier = Modifier.padding(all = 14.dp),
                    text = write.content,
                    style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
fun WriteHeader(write: Write) {
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
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = reaction.icon),
                        contentDescription = "Mood Icon",
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = reaction.name,
                        color = reaction.contentColor,
                        style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                    )
                }

                Text(
                    style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                    color = reaction.contentColor,
                    text = SimpleDateFormat("hh:mm a", Locale.US).format(write.date),
                )
            }

            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = write.title,
                color = reaction.contentColor,
                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun WriteCardPreview() {
    WriteCard(
        write = Write(
            title = "My Write",
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur",
            mood = Reaction.Happy.name,
            date = 0,
        ),
        onClick = {},
    )
}
