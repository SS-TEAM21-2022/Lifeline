package com.example.lifeline.presentation.home.composables

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.lifeline.R
import com.example.lifeline.domain.model.HomeScreenElement
import com.example.lifeline.domain.model.Priority
import com.example.lifeline.domain.model.weatherList
import com.example.lifeline.presentation.BottomNav
import com.example.lifeline.presentation.TopNav
import com.example.lifeline.presentation.today.TodayViewModel
import com.example.lifeline.presentation.ui.theme.*
import com.example.lifeline.util.Screen
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*

const val TAG = "HomeScreen"

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: TodayViewModel = hiltViewModel()) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val currentScreen = Screen.HomeScreen

    /**
     * weather[0] denotes the previous, now and next
     */
    val magicBoolean = viewModel.getTodoTask().isEmpty()

    Scaffold(
        topBar = { TopNav(currentScreen = currentScreen) },

        ) { _ ->

        var weatherState by rememberSaveable {
            Log.e(
                TAG,
                "weather is updated"
            ); mutableStateOf(if (magicBoolean) R.drawable.camp else R.drawable.weather_thunder)
        }

        var weatherValue by rememberSaveable { mutableStateOf(0) }

        val calcState: MutableList<Float> = mutableListOf()
        val elementBg: MutableList<HomeScreenElement> = mutableListOf()

        if (magicBoolean) {
            Log.e(TAG, "it is empty")
            weatherState = R.drawable.camp
        } else {
            for (i in 0..2) {
                calcState.add(weatherCalculationState(viewModel, i))
                elementBg.add(
                    when {
                        calcState[i] < 0.3F -> weatherList[0]
                        calcState[i] < 0.6F -> weatherList[1]
                        calcState[i] < 1f -> weatherList[2]
                        else -> weatherList[3]
                    }
                )
            }
            Log.e(TAG, calcState.toString())
            weatherState = elementBg[0].weatherBg
        }


        val painter = rememberVectorPainter(image = ImageVector.vectorResource(id = weatherState))

        /* set the background to camp when there is no task */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
//                horizontal = -0.7f * size.width, vertical = -0.32f * size.height
                /**
                 * They have 2 different modifiers to be able to have it fit to screen
                 */
                if (weatherState == R.drawable.camp) {
                    inset(horizontal = -0.64f * size.width, vertical = -0.47f * size.height) {
                        with(painter) {
                            draw(painter.intrinsicSize)
                        }
                    }
                } else {
                    inset(horizontal = -0.8f * size.width, vertical = -0.47f * size.height) {
                        with(painter) {
                            draw(painter.intrinsicSize)
                        }
                    }
                }
            }


            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
//                Button(onClick = {painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.camp))} ) {
//                    Text("E")
//                }
                if (!magicBoolean) {
                    Log.e(TAG, "print weather card")

                    WeatherCard(
                        weather = elementBg[0],
                        delta = 0,
                        offset = 60.dp,
                        width = 100.dp,
                        height = 180.dp,
                        roundedDp = 110.dp,
                        cardColor = CardColorSelected,
                        textColor = textBoxBg
                    )
                    Spacer(
                        modifier = Modifier
                            .size(10.dp)
                    )
                    WeatherCard(
                        weather = elementBg[1],
                        delta = 1,
                        offset = 100.dp,
                        width = 90.dp,
                        height = 160.dp,
                        roundedDp = 70.dp,
                        cardColor = CardColor,
                        textColor = textColor
                    )

                    Spacer(
                        modifier = Modifier
                            .size(10.dp)
                    )
                    WeatherCard(
                        weather = elementBg[2],
                        delta = 2,
                        offset = 100.dp,
                        width = 90.dp,
                        height = 160.dp,
                        roundedDp = 70.dp,
                        cardColor = CardColor,
                        textColor = textColor
                    )
                } else {
                    /* TODO : Show dialog box you haven't planned for today */
                    Log.e(TAG, "doesn't print weather card")
                    Card(
                        shape = RoundedCornerShape(30.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 20.dp)
                            .offset(y = 40.dp)
                            .fillMaxHeight(0.18f)
                            .alpha(0.7f)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "!",
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .fillMaxWidth()
                                    .offset(y = 8.dp, x = 20.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 60.sp,
                                color = Red
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(4f)
                                    .fillMaxHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceEvenly,
                            )
                            {
                                Text(text = "Oops", textAlign = TextAlign.Center, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "You haven't planned",
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = "anything today",
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }


                }
            }
        }
    }
}

@Composable
fun WeatherCard(
    weather: HomeScreenElement,
    delta: Int,
    offset: Dp,
    width: Dp,
    height: Dp,
    roundedDp: Dp,
    cardColor: Color,
    textColor: Color
) {

    Card(
        elevation = 8.dp,
        shape = RoundedCornerShape(roundedDp),
        modifier = Modifier
            .size(width, height)
            .offset(y = offset)
            .alpha(0.9f),

        backgroundColor = cardColor
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                imageVector = ImageVector.vectorResource(weather.weatherIcon),
                modifier = Modifier
                    .fillMaxWidth(),
                contentDescription = "weatherIcon"
                //colorFilter = ColorFilter.tint(textColor)
            )
            Text(
                color = textColor,
                text = weather.str,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                color = textColor,
                text = getWeatherTime(delta),
                fontSize = 20.sp,
            )
        }
    }
}

/**
 * function to get the time in the weather card
 */
fun getWeatherTime(
    delta: Int
): String {

    val currentTime = Calendar.getInstance()
    currentTime.add(Calendar.HOUR_OF_DAY, delta)

    val dateFormat = SimpleDateFormat("HH:mm", Locale.US)
    return if (delta == 0) "Now"
    else dateFormat.format(currentTime.time)
}

fun weatherCalculationState(viewModel: TodayViewModel, delta: Int): Float {

    val curTime = LocalTime.now()
    val remainingHour = 24 - curTime.hour - 1 - delta
    val remainingMin = 60 - curTime.minute
    var burden = 0f
    viewModel.getTodoTask().forEach {
        if (!it.isChecked) {
            burden += when (it.priority) {
                Priority.ESPRESSO -> 1.5f * it.duration / 60f
                Priority.MILK -> 1f * it.duration / 60f
                else -> 0f
            }
        }
    }
    val cc = remainingHour + remainingMin / 60f
    if (cc == 0.0f) return 0.0f
    return burden / cc
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    val currentScreen = Screen.HomeScreen
    LifelineTheme {
        Scaffold(
            content = { HomeScreen(navController) },
            bottomBar = {
                BottomNav(
                    navController = navController
                )
            }
        )
    }
}
