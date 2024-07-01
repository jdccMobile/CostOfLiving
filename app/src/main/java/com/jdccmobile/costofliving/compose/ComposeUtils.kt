package com.jdccmobile.costofliving.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.debugBorder(color: Color = Color.Magenta) = this.border(1.dp, color)
