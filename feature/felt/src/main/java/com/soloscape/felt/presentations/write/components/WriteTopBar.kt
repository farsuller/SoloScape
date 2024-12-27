package com.soloscape.felt.presentations.write.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.soloscape.database.domain.model.Write
import com.soloscape.felt.presentations.common.MoreVertAction
import com.soloscape.ui.Reaction
import com.soloscape.ui.theme.SoloScapeTheme
import com.soloscape.ui.theme.robotoBoldFontFamily
import com.soloscape.util.clickableWithoutRipple
import com.soloscape.util.toZonedDateTimeOrNull
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
internal fun WriteTopBar(
    reaction: Reaction,
    selectedWrite: Write? = null,
    onBackPressed: () -> Unit,
    onDeleteConfirmed: (Write) -> Unit,
    onDateTimeUpdated: (ZonedDateTime) -> Unit,
) {
    val dateDialog = rememberUseCaseState()
    val timeDialog = rememberUseCaseState()
    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    var currentTime by remember { mutableStateOf(LocalTime.now()) }

    val formattedDate = remember(key1 = currentDate) {
        DateTimeFormatter.ofPattern("dd MMM yyyy")
            .format(currentDate).uppercase()
    }

    val formattedTime = remember(key1 = currentTime) {
        DateTimeFormatter.ofPattern("hh:mm a")
            .format(currentTime).uppercase()
    }

    var dateTimeUpdated by remember { mutableStateOf(false) }

    val selectedReportDateTime = remember(selectedWrite) {
        selectedWrite?.date?.toZonedDateTimeOrNull()?.let { zonedDateTime ->
            DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
                .format(zonedDateTime)
                .uppercase()
        } ?: "Unknown"
    }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = reaction.containerColor),
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Arrow Icon",
                    tint = reaction.contentColor,
                )
            }
        },
        title = {
            Column(
                modifier = Modifier.padding(start = 10.dp),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = reaction.name,
                    style = TextStyle(
                        fontFamily = robotoBoldFontFamily,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
                    ),
                    textAlign = TextAlign.Center,
                    color = reaction.contentColor,
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (selectedWrite != null && dateTimeUpdated) {
                        "$formattedDate $formattedTime"
                    } else if (selectedWrite != null) {
                        selectedReportDateTime
                    } else {
                        "$formattedDate, $formattedTime"
                    },
                    style = TextStyle(
                        fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                        fontSize = MaterialTheme.typography.labelMedium.fontSize,
                        fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                    ),
                    textAlign = TextAlign.Center,
                    color = reaction.contentColor,
                )
            }
        },
        actions = {
            if (dateTimeUpdated) {
                IconButton(onClick = {
                    currentDate = LocalDate.now()
                    currentTime = LocalTime.now()
                    dateTimeUpdated = false
                    onDateTimeUpdated(
                        ZonedDateTime.of(
                            currentDate,
                            currentTime,
                            ZoneId.systemDefault(),
                        ),
                    )
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = reaction.contentColor,
                    )
                }
            } else {
                Icon(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickableWithoutRipple(
                            onClick = { dateDialog.show() },
                        ),
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date Icon",
                    tint = reaction.contentColor,
                )
            }

            if (selectedWrite != null) {
                MoreVertAction(
                    selectedWrite = selectedWrite,
                    onDeleteConfirmed = { onDeleteConfirmed(selectedWrite) },
                )
            }
        },
    )

    CalendarDialog(
        state = dateDialog,
        selection = CalendarSelection.Date { localDate ->
            currentDate = localDate
            timeDialog.show()
        },
        config = CalendarConfig(monthSelection = true, yearSelection = true),
    )

    ClockDialog(
        state = timeDialog,
        selection = ClockSelection.HoursMinutes { hours, minutes ->

            currentTime = LocalTime.of(hours, minutes)
            dateTimeUpdated = true
            onDateTimeUpdated(
                ZonedDateTime.of(
                    currentDate,
                    currentTime,
                    ZoneId.systemDefault(),
                ),
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun WriteTopBarPreview() {
    SoloScapeTheme {
        WriteTopBar(
            selectedWrite = null,
            reaction = Reaction.Neutral,
            onBackPressed = {},
            onDeleteConfirmed = {},
            onDateTimeUpdated = {},

        )
    }
}
