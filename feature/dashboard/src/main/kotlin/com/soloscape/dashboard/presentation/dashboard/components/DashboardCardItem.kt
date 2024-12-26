package com.soloscape.dashboard.presentation.dashboard.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soloscape.ui.R
import com.soloscape.ui.theme.MossGreenColor
import com.soloscape.ui.theme.NyanzaColor
import com.soloscape.ui.theme.SoloScapeTheme
import com.soloscape.ui.theme.robotoMediumItalicFontFamily
import com.soloscape.util.clickableWithoutRipple

@Composable
fun DashboardCardItem(
    modifier: Modifier = Modifier,
    textTitle: String,
    textBody: String,
    textDescription: String,
    bgIconColor: Color,
    @DrawableRes iconImage: Int,
    @DrawableRes imageBottom: Int,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier.clickableWithoutRipple { onClick() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            modifier = Modifier
                .size(110.dp)
                .padding(start = 10.dp, bottom = 10.dp)
                .align(Alignment.BottomStart),
            painter = painterResource(imageBottom),
            contentDescription = null,
        )

        Icon(
            modifier = Modifier
                .size(35.dp)
                .offset(x = (-20).dp, y = (-30).dp)
                .align(Alignment.BottomEnd)
                .rotate(90F),
            painter = painterResource(R.drawable.ic_chevron),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.weight(0.1F),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .height(3.dp)
                        .width(40.dp)
                        .background(MaterialTheme.colorScheme.onSurface)
                )

                Box(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .height(3.dp)
                        .width(20.dp)
                        .background(MaterialTheme.colorScheme.onSurface)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .weight(0.4F),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onSurface,
                    text = textTitle,
                    style = TextStyle(
                        fontFamily = robotoMediumItalicFontFamily,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = MaterialTheme.typography.bodyLarge.fontWeight
                    )
                )

                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(bgIconColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        painter = painterResource(iconImage),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .weight(0.5F),
                text = textBody,
                color = MaterialTheme.colorScheme.onSurface,
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                ),
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .weight(1F),
                text = textDescription,
                color = MaterialTheme.colorScheme.onSurface,
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = MaterialTheme.typography.bodySmall.fontWeight
                ),
            )
        }


    }
}

@Preview
@Composable
internal fun DashboardScreenPreview() {
    SoloScapeTheme {
        Surface {
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
                    .background(NyanzaColor),
                textTitle = "Idea",
                textBody = "Note down thoughts and inspirations.",
                textDescription = "A notepad to capture your ideas, plans, and creative sparks.",
                bgIconColor = MossGreenColor,
                iconImage = R.drawable.ic_idea,
                imageBottom = R.drawable.svg_idea
            )
        }
    }
}