package com.soloscape.dashboard.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soloscape.dashboard.presentation.dashboard.components.DashboardCardItem
import com.soloscape.ui.R
import com.soloscape.ui.theme.MistyRoseColor
import com.soloscape.ui.theme.MossGreenColor
import com.soloscape.ui.theme.NyanzaColor
import com.soloscape.ui.theme.PhilippineBronzeColor

@Composable
fun DashboardScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
        ) {
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .weight(0.4F)
                    .padding(10.dp),
                contentAlignment = Alignment.CenterStart
            ) {

                Column(Modifier.padding(10.dp)) {
                    Text(
                        text = "Hi..",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.headlineLarge.fontFamily,
                            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                            fontWeight = MaterialTheme.typography.headlineLarge.fontWeight
                        ),
                    )

                    Text(
                        color = MaterialTheme.colorScheme.onSurface,
                        text = "Your Name Your Name Your Name Your Name",
                        style = TextStyle(
                            fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
                            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                            fontWeight = MaterialTheme.typography.headlineMedium.fontWeight
                        ),
                        lineHeight = 40.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DashboardCardItem(
                        modifier = Modifier
                            .height(height = 400.dp)
                            .clip(
                                RoundedCornerShape(
                                    topEnd = 40.dp,
                                    bottomStart = 40.dp,
                                    bottomEnd = 40.dp
                                )
                            )
                            .background(NyanzaColor)
                            .weight(1F),
                        textTitle = "Idea",
                        textBody = "Note down thoughts and inspirations.",
                        textDescription = "A notepad to capture your ideas, plans, and creative sparks.",
                        bgIconColor = MossGreenColor,
                        iconImage = R.drawable.ic_idea,
                        imageBottom = R.drawable.svg_idea
                    )

                    DashboardCardItem(
                        modifier = Modifier
                            .height(height = 400.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 40.dp,
                                    topEnd = 40.dp,
                                    bottomStart = 40.dp
                                )
                            )
                            .background(MistyRoseColor)
                            .weight(1F),
                        textTitle = "Felt",
                        textBody = "Express and reflect on your emotions.",
                        textDescription = "A journal designed to help you articulate and process your feelings.",
                        bgIconColor = PhilippineBronzeColor,
                        iconImage = R.drawable.ic_feeling,
                        imageBottom = R.drawable.svg_felt
                    )
                }

            }
        }
    }
}
