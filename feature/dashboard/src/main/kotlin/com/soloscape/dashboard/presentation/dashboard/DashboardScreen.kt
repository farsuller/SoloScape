package com.soloscape.dashboard.presentation.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soloscape.ui.theme.SoloScapeTheme

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
            ){
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Hi..\nFlorence",
                    lineHeight = 58.sp,
                    fontSize = 60.sp
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(height = 400.dp)
                            .weight(1F)
                            .clip(RoundedCornerShape(topEnd = 40.dp, bottomStart = 40.dp, bottomEnd = 40.dp))
                            .background(Color(0xFF8D6E63))
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .height(3.dp)
                                    .width(40.dp)
                                    .background(Color.Black)
                            )

                            Spacer(
                                modifier = Modifier
                                    .padding(top = 5.dp)
                                    .height(3.dp)
                                    .width(20.dp)
                                    .background(Color.Black)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier,
                                    text = "Idea",
                                    fontSize = 40.sp
                                )

                                Box(modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray),
                                    contentAlignment = Alignment.Center){
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.Send,
                                        contentDescription = null
                                    )
                                }
                            }
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                text = "Take note your ideas or thoughts",
                                fontSize = 20.sp
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.height(height = 400.dp)
                            .weight(1F)
                            .clip(RoundedCornerShape(topStart = 40.dp,topEnd = 40.dp, bottomStart = 40.dp))
                            .background(Color(0xFF66BB6A))
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .height(3.dp)
                                    .width(40.dp)
                                    .background(Color.Black)
                            )

                            Spacer(
                                modifier = Modifier
                                    .padding(top = 5.dp)
                                    .height(3.dp)
                                    .width(20.dp)
                                    .background(Color.Black)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier,
                                    text = "Felt",
                                    fontSize = 40.sp
                                )

                                Box(modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray),
                                    contentAlignment = Alignment.Center){
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.Send,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }

            }

        }
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {

    SoloScapeTheme {
        Surface {
            DashboardScreen()
        }
    }
}