package com.soloscape.dashboard.presentation.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soloscape.dashboard.presentation.dashboard.components.DashboardCardItem
import com.soloscape.dashboard.presentation.dashboard.components.DashboardStaggeredHorizontalGrid
import com.soloscape.ui.R
import com.soloscape.ui.components.TransparentTextField
import com.soloscape.ui.theme.MistyRoseColor
import com.soloscape.ui.theme.MossGreenColor
import com.soloscape.ui.theme.NyanzaColor
import com.soloscape.ui.theme.PhilippineBronzeColor
import com.soloscape.ui.theme.robotoThinFontFamily
import com.soloscape.util.Constants.TestTags.HI
import com.soloscape.util.Constants.TestTags.JOURNAL_CARD
import com.soloscape.util.Constants.TestTags.NOTE_CARD
import com.soloscape.util.Constants.TestTags.YOUR_NAME_TEXT_FIELD

@Composable
fun DashboardScreen(
    navigationToFelt: () -> Unit = {},
    navigationToIdea: () -> Unit = {},
    onValueChange: (String) -> Unit = {},
    onFocusChange: (FocusState) -> Unit = {},
    onNoteClick: () -> Unit = {},
    onJournalClick: () -> Unit = {},
    yourNameState: YourNameState,
    journalNoteState: JournalNoteState,
) {
    val focusManager = LocalFocusManager.current
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 500)),
            exit = fadeOut(animationSpec = tween(durationMillis = 500)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = padding.calculateTopPadding(),
                        bottom = padding.calculateBottomPadding(),
                    )
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    modifier = Modifier
                        .width(330.dp)
                        .padding(start = 10.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Column {
                        Text(
                            modifier = Modifier.testTag(HI),
                            text = stringResource(R.string.hi),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = TextStyle(
                                fontFamily = robotoThinFontFamily,
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                fontWeight = MaterialTheme.typography.headlineLarge.fontWeight,
                            ),
                        )

                        TransparentTextField(
                            text = yourNameState.text,
                            hint = yourNameState.hint,
                            onValueChange = onValueChange,
                            onFocusChange = onFocusChange,
                            isHintVisible = yourNameState.isHintVisible,
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyLarge,
                            testTag = YOUR_NAME_TEXT_FIELD,
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            characterLimit = 10,
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    DashboardCardItem(
                        modifier = Modifier
                            .height(height = 160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NyanzaColor)
                            .testTag(NOTE_CARD),
                        textTitle = stringResource(R.string.idea_title),
                        textBody = stringResource(R.string.idea_content_1),
                        textDescription = stringResource(R.string.idea_content_2),
                        bgIconColor = MossGreenColor,
                        iconImage = R.drawable.lightbulb,
                        imageRight = R.drawable.svg_idea,
                        onClick = navigationToIdea,
                    )

                    DashboardCardItem(
                        modifier = Modifier
                            .height(height = 160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MistyRoseColor)
                            .testTag(JOURNAL_CARD),
                        textTitle = stringResource(R.string.felt_title),
                        textBody = stringResource(R.string.felt_content_1),
                        textDescription = stringResource(R.string.felt_content_2),
                        bgIconColor = PhilippineBronzeColor,
                        iconImage = R.drawable.heart,
                        imageRight = R.drawable.svg_felt,
                        onClick = navigationToFelt,
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                DashboardStaggeredHorizontalGrid(
                    onNoteClick = onNoteClick,
                    onJournalClick = onJournalClick,
                    combinedList = journalNoteState.combinedList ?: emptyList(),
                )
            }
        }
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(yourNameState = YourNameState(), journalNoteState = JournalNoteState())
}
