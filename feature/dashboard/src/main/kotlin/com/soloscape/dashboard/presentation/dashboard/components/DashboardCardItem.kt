package com.soloscape.dashboard.presentation.dashboard.components

import android.os.Build.VERSION.SDK_INT
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.rememberAsyncImagePainter
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
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
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(AnimatedImageDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Box(
        modifier = modifier.clickableWithoutRipple { onClick() },
        contentAlignment = Alignment.BottomCenter,
    ) {
        Image(
            modifier = Modifier
                .size(120.dp)
                .padding(top = 10.dp, end = 10.dp)
                .align(Alignment.TopEnd),
            painter = painterResource(imageBottom),
            contentDescription = null,
        )
        Icon(
            modifier = Modifier
                .size(25.dp)
                .rotate(90F)
                .offset(x = (-10).dp, y = 15.dp)
                .align(Alignment.BottomEnd),
            painter = painterResource(R.drawable.ic_chevron),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onSurface,
                    text = textTitle,
                    style = TextStyle(
                        fontFamily = robotoMediumItalicFontFamily,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                    ),
                )

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(bgIconColor),
                    contentAlignment = Alignment.Center,
                ) {
                    val painter = rememberAsyncImagePainter(
                        model = iconImage,
                        imageLoader = imageLoader,
                    )

                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.size(35.dp),
                    )
                }
            }
            Text(
                modifier = Modifier
                    .width(300.dp)
                    .padding(start = 10.dp),
                text = textBody,
                color = MaterialTheme.colorScheme.onSurface,
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                ),
            )

            Text(
                modifier = Modifier
                    .width(300.dp)
                    .padding(start = 10.dp, top = 5.dp),
                text = textDescription,
                color = MaterialTheme.colorScheme.onSurface,
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
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
                    .height(height = 160.dp)
                    .clip(
                        RoundedCornerShape(12.dp),
                    )
                    .background(NyanzaColor),
                textTitle = "Idea",
                textBody = "Note down thoughts and inspirations.",
                textDescription = "A notepad to capture your ideas, plans, and creative sparks.",
                bgIconColor = MossGreenColor,
                iconImage = R.drawable.ic_idea,
                imageBottom = R.drawable.svg_idea,
            )
        }
    }
}
