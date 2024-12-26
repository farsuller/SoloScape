package com.soloscape.felt.presentations.felt.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.soloscape.util.clickableWithoutRipple
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
internal fun FeltTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    dateIsSelected: Boolean,
    onDateSelected: (ZonedDateTime) -> Unit,
    onDateReset: () -> Unit,
    onBackPressed: () -> Unit
) {
    val dateDialog = rememberUseCaseState()
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    TopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .padding(10.dp)
                    .clickableWithoutRipple { onBackPressed() },
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = "Close Icon",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        title = {},
        actions = {
            Icon(
                modifier = Modifier
                    .padding(10.dp)
                    .clickableWithoutRipple { if (dateIsSelected) onDateReset() else dateDialog.show() },
                imageVector = if (dateIsSelected) Icons.Default.Close else Icons.Default.DateRange,
                contentDescription = "Close Icon",
                tint = MaterialTheme.colorScheme.onSurface,
            )

            Icon(
                modifier = Modifier
                    .padding(10.dp)
                    .clickableWithoutRipple {  },
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Close Icon",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
    )

    CalendarDialog(
        state = dateDialog,
        selection = CalendarSelection.Date { localDate ->
            pickedDate = localDate
            onDateSelected(
                ZonedDateTime.of(
                    pickedDate,
                    LocalTime.now(),
                    ZoneId.systemDefault(),
                ),
            )
        },
        config = CalendarConfig(monthSelection = true, yearSelection = true),
    )
}
