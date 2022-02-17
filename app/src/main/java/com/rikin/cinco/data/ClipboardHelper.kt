package com.rikin.cinco.data

import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString


class ClipboardHelper(private val manager: ClipboardManager) {
    fun copy(value: String) {
        manager.setText(AnnotatedString(value))
    }
}