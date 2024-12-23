package com.soloscape.home.presentations.write.components

data class WriteTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true,
)
