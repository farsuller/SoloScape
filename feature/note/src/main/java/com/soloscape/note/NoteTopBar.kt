package com.soloscape.note

import android.annotation.SuppressLint
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.soloscape.ui.components.DisplayAlertDialog
import com.soloscape.ui.theme.MultiModularArchJCTheme
import com.soloscape.util.clickableWithoutRipple
import com.soloscape.util.model.Mood
import com.soloscape.util.model.Report
import com.soloscape.util.toInstant
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NoteTopBar(
    moodName: () -> String,
    selectedReport: Report?,
    onBackPressed: () -> Unit,
    onDeleteConfirmed: () -> Unit,
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

    val selectedReportDateTime = remember(selectedReport) {
        if (selectedReport != null) {
            SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                .format(Date.from(selectedReport.date.toInstant())).uppercase()
        } else {
            "Unknown"
        }
    }

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Arrow Icon",
                )
            }
        },
        title = {
            Column(
                modifier = Modifier.padding(start = 10.dp),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = moodName(),
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                    ),
                    textAlign = TextAlign.Center,
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (selectedReport != null && dateTimeUpdated) {
                        "$formattedDate $formattedTime"
                    } else if (selectedReport != null) {
                        selectedReportDateTime
                    } else {
                        "$formattedDate, $formattedTime"
                    },
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    ),
                    textAlign = TextAlign.Center,
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
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            } else {
                Icon(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickableWithoutRipple(
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { dateDialog.show() },
                        ),
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date Icon",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            if (selectedReport != null) {
                DeleteReportAction(
                    selectedReport = selectedReport,
                    onDeleteConfirmed = onDeleteConfirmed,
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

@Composable
fun DeleteReportAction(
    selectedReport: Report?,
    onDeleteConfirmed: () -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var openDialog by remember {
        mutableStateOf(false)
    }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(text = {
            Text(text = "Delete")
        }, onClick = {
            openDialog = true
            expanded = false
        })
    }

    DisplayAlertDialog(
        title = "Delete",
        message = "Are you sure you want to delete this note? '${selectedReport?.title}'?",
        dialogOpened = openDialog,
        onCloseDialog = { openDialog = false },
        onYesClicked = onDeleteConfirmed,
    )

    Icon(
        modifier = Modifier
            .padding(end = 5.dp)
            .clickableWithoutRipple(
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    expanded = !expanded
                },
            ),
        imageVector = Icons.Default.MoreVert,
        contentDescription = "Overflow Menu Icon",
        tint = MaterialTheme.colorScheme.onSurface,
    )
}

@Preview(showBackground = true)
@Composable
fun NoteTopBarPreview() {
    MultiModularArchJCTheme {
        NoteTopBar(
            moodName = { Mood.Neutral.name },
            selectedReport = null,
            onBackPressed = {},
            onDeleteConfirmed = {},
            onDateTimeUpdated = {},

        )
    }
}
