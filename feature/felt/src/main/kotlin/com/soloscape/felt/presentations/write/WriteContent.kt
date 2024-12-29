package com.soloscape.felt.presentations.write

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.soloscape.felt.presentations.write.components.WriteState
import com.soloscape.ui.Reaction
import com.soloscape.ui.components.TransparentTextField

@Composable
internal fun WriteContent(
    pagerState: PagerState,
    paddingValues: PaddingValues,
    onValueChangeTitle: (String) -> Unit,
    onFocusChangeTitle: (FocusState) -> Unit,
    onValueChangeContent: (String) -> Unit,
    onFocusChangeContent: (FocusState) -> Unit,
    writeState: WriteState,
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = scrollState.maxValue) {
        scrollState.scrollTo(scrollState.maxValue)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding())
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(state = scrollState),
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            HorizontalPager(state = pagerState) { page ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    AsyncImage(
                        modifier = Modifier.size(120.dp),
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(Reaction.entries[page].icon)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Mood Image",
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TransparentTextField(
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.headlineMedium,
                    text = writeState.title,
                    hint = writeState.titleHint,
                    onValueChange = onValueChangeTitle,
                    onFocusChange = onFocusChangeTitle,
                    isHintVisible = writeState.titleHintVisible,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                    ),
                )

                TransparentTextField(
                    modifier = Modifier.fillMaxHeight(),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    text = writeState.content,
                    hint = writeState.contentHint,
                    onValueChange = onValueChangeContent,
                    onFocusChange = onFocusChangeContent,
                    isHintVisible = writeState.contentHintVisible,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),

                )
            }
        }
    }
}
