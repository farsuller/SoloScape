package com.soloscape.home.presentations.home.components

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soloscape.database.domain.model.Write
import com.soloscape.ui.Mood
import com.soloscape.ui.theme.Elevation
import java.text.SimpleDateFormat
import java.util.Locale
import com.soloscape.ui.components.Gallery

@Composable
fun ReportHolder(report: Write, onClick: (Int) -> Unit) {
    val context = LocalContext.current
    val localDensity = LocalDensity.current
    var componentHeight by remember { mutableStateOf(0.dp) }
    var galleryOpened by remember { mutableStateOf(false) }
    val downloadedImages = remember { mutableStateListOf<Uri>() }
    var galleryLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = galleryOpened) {
        if (galleryOpened && downloadedImages.isEmpty()) {
            galleryLoading = true

        }
    }
    Row(
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember {
                MutableInteractionSource()
            },
        ) {
            onClick(report.id)
        },
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
                ReportHeader(moodName = report.mood.orEmpty(), time = report.date)

                Text(
                    modifier = Modifier.padding(all = 14.dp),
                    text = report.description.orEmpty(),
                    style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                )

//                if (report.images.isNotEmpty()) {
//                    ShowGalleryButton(
//                        galleryLoading = galleryLoading,
//                        galleryOpened = galleryOpened,
//                        onClick = {
//                            galleryOpened = !galleryOpened
//                        },
//                    )
//                }

                AnimatedVisibility(
                    visible = galleryOpened && !galleryLoading,
                    enter = fadeIn() + expandVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow,
                        ),
                    ),
                ) {
                    Column(modifier = Modifier.padding(all = 14.dp)) {
                        Gallery(images = downloadedImages)
                    }
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun ReportHeader(moodName: String, time: Long) {
    val mood by remember { mutableStateOf(Mood.valueOf(moodName)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(mood.containerColor)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = mood.icon),
                contentDescription = "Mood Icon",
            )

            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = mood.name,
                color = mood.contentColor,
                style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
            )
        }
        Text(
            style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
            color = mood.contentColor,
            text = SimpleDateFormat("hh:mm a", Locale.US).format(time),
        )
    }
}

@Composable
fun ShowGalleryButton(
    galleryOpened: Boolean,
    galleryLoading: Boolean,
    onClick: () -> Unit,
) {
    TextButton(onClick = onClick) {
        Text(
            text = if (galleryOpened) {
                if (galleryLoading) "Loading" else "Hide Gallery"
            } else {
                "Show Gallery"
            },
            style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ReportHolderPreview() {
    ReportHolder(
        report = Write(
            title = "My Write",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                    "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur",
            mood = Mood.Happy.name,
            date = 0
        ),
        onClick = {},
    )
}
