package com.soloscape.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soloscape.util.model.Report
import com.soloscape.util.ReportHolder
import java.time.LocalDate


@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HomeContent(
    paddingValues: PaddingValues,
    reportNotes : Map<LocalDate, List<Report>>,
    onClick: (String) -> Unit
){
    if(reportNotes.isNotEmpty()){
        LazyColumn(modifier = Modifier
            .padding(horizontal = 24.dp)
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding())
            )
        {
            reportNotes.forEach { (localDate, reports) ->
                stickyHeader(key = localDate){
                    DateHeader(localDate = localDate)
                }

                items(
                    items = reports,
                    key = { it._id.toString() })
                {
                    ReportHolder(report = it, onClick = onClick)
                }
            }
        }
    }else{
        EmptyPage()
    }

}
@SuppressLint("NewApi")
@Composable
internal fun DateHeader(localDate: LocalDate) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 14.dp)
            ,
        verticalAlignment = Alignment.CenterVertically)
    {
        Column(horizontalAlignment = Alignment.End)
        {
            Text(
                style =
                TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Light
                ),
                text = String.format("%02d", localDate.dayOfMonth)
            )

            Text(
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = FontWeight.Light
                ),
                text = localDate.dayOfWeek.toString().take(3)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(horizontalAlignment = Alignment.Start)
        {
            Text(
                style =
                TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Light
                ),
                text = localDate.month.toString().lowercase().replaceFirstChar { it.titlecase() }
            )

            Text(
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = FontWeight.Light
                ),
                text = "${localDate.year}",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
internal fun EmptyPage(
    title: String = "Welcome to SoloScape",
    subtitle: String = "Write Something"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Medium
            )
        )
        Text(
            text = subtitle,
            style = TextStyle(
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Normal
            )
        )
    }
}

@SuppressLint("NewApi")
@Composable
@Preview(showBackground = true)
internal fun DateHeaderPreview() {
    DateHeader(localDate = LocalDate.now())
}