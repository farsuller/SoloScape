package com.soloscape.home.presentations.write

import android.widget.Toast
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
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
import com.soloscape.database.domain.model.Write
import com.soloscape.home.presentations.write.components.TransparentTextField
import com.soloscape.home.presentations.write.components.WriteChanges
import com.soloscape.ui.Mood
import kotlinx.coroutines.launch

@Composable
internal fun NoteContent(
    writeState: WriteState,
    pagerState: PagerState,
    paddingValues: PaddingValues,
    onValueChangeTitle : (String) -> Unit,
    onFocusChangeTitle : (FocusState) -> Unit,
    onValueChangeContent : (String) -> Unit,
    onFocusChangeContent : (FocusState) -> Unit,
    noteTitle : String,
    noteContent : String
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = scrollState.maxValue) {
        scrollState.scrollTo(scrollState.maxValue)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding())
            .padding(bottom = 24.dp)
            .padding(horizontal = 24.dp),
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
                            .data(Mood.entries[page].icon)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Mood Image",
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TransparentTextField(
                    text = writeState.title,
                    hint = writeState.hint,
                    onValueChange = onValueChangeTitle,
                    onFocusChange = onFocusChangeTitle,
                    isHintVisible = writeState.isHintVisible,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.headlineMedium
                )

                TransparentTextField(
                    modifier = Modifier.fillMaxHeight(),
                    text = writeState.content,
                    hint = writeState.hint,
                    onValueChange = onValueChangeContent,
                    onFocusChange = onFocusChangeContent,
                    isHintVisible = writeState.isHintVisible,
                    textStyle = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
