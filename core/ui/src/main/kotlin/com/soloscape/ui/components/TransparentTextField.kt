package com.soloscape.ui.components

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
import androidx.compose.ui.text.TextStyle

@Composable
fun TransparentTextField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String,
    isHintVisible: Boolean = true,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    onFocusChange: (FocusState) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    testTag: String = "",
    characterLimit: Int? = null,
) {
    Box(
        modifier = modifier,
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it)
                }
                .testTag(testTag),
            value = text,
            onValueChange = { newValue ->
                if (characterLimit == null || newValue.length <= characterLimit) {
                    onValueChange(newValue)
                }
            },
            singleLine = singleLine,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
        )
        if (isHintVisible) {
            Text(
                text = hint,
                style = textStyle,
                color = Color.DarkGray,
            )
        }
    }
}