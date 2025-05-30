package com.pmgdev.pulse.ui.base.baseicons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember

@Composable
fun fitnessIcon(): ImageVector{
    return remember {
        ImageVector.Builder(
            name = "Fitness_center",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(536f, 876f)
                lineToRelative(-56f, -56f)
                lineToRelative(142f, -142f)
                lineToRelative(-340f, -340f)
                lineToRelative(-142f, 142f)
                lineToRelative(-56f, -56f)
                lineToRelative(56f, -58f)
                lineToRelative(-56f, -56f)
                lineToRelative(84f, -84f)
                lineToRelative(-56f, -58f)
                lineToRelative(56f, -56f)
                lineToRelative(58f, 56f)
                lineToRelative(84f, -84f)
                lineToRelative(56f, 56f)
                lineToRelative(58f, -56f)
                lineToRelative(56f, 56f)
                lineToRelative(-142f, 142f)
                lineToRelative(340f, 340f)
                lineToRelative(142f, -142f)
                lineToRelative(56f, 56f)
                lineToRelative(-56f, 58f)
                lineToRelative(56f, 56f)
                lineToRelative(-84f, 84f)
                lineToRelative(56f, 58f)
                lineToRelative(-56f, 56f)
                lineToRelative(-58f, -56f)
                lineToRelative(-84f, 84f)
                lineToRelative(-56f, -56f)
                close()
            }
        }.build()
    }
}