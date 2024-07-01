package com.jdccmobile.costofliving.common

import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.debugBorder(color: Color = Color.Magenta) = this.border(1.dp, color)

fun Modifier.modifyIf(condition: Boolean, modify: Modifier.() -> Modifier): Modifier =
    if (condition) then(modify()) else this
