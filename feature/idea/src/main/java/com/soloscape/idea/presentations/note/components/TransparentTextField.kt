package com.soloscape.idea.presentations.note.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun TransparentTextField(
    modifier: Modifier = Modifier,
    text : String,
    hint : String,
    isHintVisible : Boolean = true,
    onValueChange : (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    onFocusChange : (FocusState) -> Unit,
    testTag: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions(),
){
    Box(
        modifier = modifier
    ){
        BasicTextField(
            modifier = Modifier
                .testTag(testTag)
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it)
                },
            value = TextFieldValue(text, TextRange(text.length)),
            onValueChange = { onValueChange(it.text) },
            singleLine = singleLine,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
        )
        if (isHintVisible){
            Text(
                text = hint,
                style = textStyle,
                color = Color.DarkGray
            )
        }
    }

}