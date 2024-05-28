package com.soloscape.note

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soloscape.note.components.CirclePath
import com.soloscape.note.model.locations
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.endOffsetForPage(page: Int) = offsetForPage(page).coerceAtMost(0f)

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.startOffsetForPage(page: Int) = offsetForPage(page).coerceAtLeast(0f)

@Composable
@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
fun CircularCarouselPager() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        val state = rememberPagerState(pageCount = {
            locations.count()
        })

        val (offsetY, setOffsetY) = remember {
            mutableStateOf(0f)
        }
        HorizontalPager(
            state = state,

            modifier = Modifier
                .pointerInteropFilter {
                    setOffsetY(it.y)
                    false
                }
                .padding(16.dp)
                .clip(RoundedCornerShape(14.dp)),
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        val pageOffset = state.offsetForPage(page)
                        translationX = size.width * pageOffset

                        val endOffset = state.endOffsetForPage(page)
                        shadowElevation = 20f
                        shape = CirclePath(
                            progress = 1f - endOffset.absoluteValue,
                            origin = Offset(
                                size.width,
                                offsetY,
                            ),
                        )
                        clip = true

                        val abslouteOffset = state.offsetForPage(page).absoluteValue
                        val scale = 1f + (abslouteOffset.absoluteValue * .3f)

                        scaleX = scale
                        scaleY = scale

                        val startOffset = state.startOffsetForPage(page)
                        alpha = (2f - startOffset) / 2
                    },
            ) {
                val location = locations[page]
                Image(
                    painter = painterResource(id = location.image),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.BottomCenter)
                        .fillMaxHeight(.5f)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = .7f),
                                ),
                            ),
                        ),
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(18.dp),
                ) {
                    Text(
                        text = location.title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = location.subtitle, fontSize = 16.sp, color = Color(0xffcccccc))
                }
            }
        }
    }
}
