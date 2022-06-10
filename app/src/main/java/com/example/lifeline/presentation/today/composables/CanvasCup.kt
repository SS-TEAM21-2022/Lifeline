package com.example.lifeline.presentation.today.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lifeline.R
import com.example.lifeline.util.VectorHelper

@Composable
fun CupCanvas() {
    val context = LocalContext.current

    val mask = VectorHelper.createPainter(img = R.drawable.coffeemask)
    val cup = VectorHelper.createPainter(img = R.drawable.coffee_empty)
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    Box(
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .fillMaxHeight(0.5f)
    ) {

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            drawOval(
                color = Color.LightGray,
                size = Size(size.width,size.height*0.3f), topLeft = Offset(x = 0f, y = 0.7f*size.height)
            )
            with(mask) {
                scale(0.7f, Offset(size.width/1.96f,size.height/1.8f), block = {draw(size)})
            }
            with(cup) {
                translate(left = size.width/18f ,block = { draw(size) })
            }


        }
    }
}

@Composable
fun DrawLine() {
    Canvas(modifier = Modifier.size(300.dp)) {
        val height = size.height
        val width = size.width

        drawLine(
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = width, y = height),
            color = Color.Blue,
            strokeWidth = 16.0f
        )
    }
}

@Preview
@Composable
fun CupCanvasPreview() {
    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1f)) {
        CupCanvas()
    }
}