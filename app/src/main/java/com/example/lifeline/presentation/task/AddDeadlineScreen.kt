package com.example.lifeline.presentation.task

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lifeline.R
import com.example.lifeline.domain.model.TaskType
import com.example.lifeline.presentation.TopNav
import com.example.lifeline.presentation.components.DatePicker
import com.example.lifeline.presentation.task.composables.DurationDrawer
import com.example.lifeline.presentation.ui.theme.Shapes
import com.example.lifeline.presentation.ui.theme.myAppTextFieldColors
import com.example.lifeline.util.Screen
import com.example.lifeline.util.clearFocusOnKeyboardDismiss
import com.example.lifeline.util.toDuration
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun AddDeadlineScreen(navController: NavController, viewModel: AddEditTodoViewModel = hiltViewModel()) {



    val task = viewModel.taskEntry
    val currentScreen = Screen.AddDeadlineScreen

    val str = if (task.value.id != null) "Edit Deadline" else "Add Deadline"


    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var showPicker by rememberSaveable { mutableStateOf(false) }
    var date by rememberSaveable { mutableStateOf(false) }

    val selDate = remember { mutableStateOf(Calendar.getInstance().time) }

    val bottomDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val durationValue = remember { mutableStateOf(0) }
    durationValue.value = task.value.duration

//    Log.e("ViewModel", "durationValue" + durationValue.value)
//    Log.e("ViewModel", task.value.toString())
    Scaffold(
        topBar = { TopNav(currentScreen, modifier = Modifier.background(Color.White), viewModel, navController, str) },
        backgroundColor = Color.White
    ) { innerPadding ->
        BottomDrawer(
            drawerState = bottomDrawerState,
            drawerContent = {
                DurationDrawer(durationValue, bottomDrawerState, viewModel, scope)
            }
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()

            )
            {
                Column {
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .height(100.dp)
                    ) {
                        TextField(
                            value = task.value.taskName,
                            onValueChange = { viewModel.onEvent(AddEditTodoEvent.EnteredTitle(it)) },
                            colors = myAppTextFieldColors(),
                            shape = Shapes.large,
                            label = { Text(stringResource(R.string.task_name)) },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),

                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .clearFocusOnKeyboardDismiss()
                                .fillMaxWidth()
                        )


                    }
                    Divider(thickness = 2.dp)
                    Row(
                        modifier = Modifier.padding(vertical = 20.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        /**
                         * Date Picker for AddTodoTask
                         */
                        if (showPicker)
                            DatePicker(onDismissRequest = {
                                showPicker = false
                            }, viewModel = viewModel)

                        /* for calendar format  */
                        val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
                        val strDate = dateFormat.format(viewModel.taskEntry.value.date)
                        TextField(
                            value = strDate,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "DateRange"
                                )
                            },
                            onValueChange = {},
                            colors = myAppTextFieldColors(),
                            modifier = Modifier
                                .requiredWidth(212.dp)
                                .padding(horizontal = 20.dp)
                                .clickable(onClick = { showPicker = true }),

                            enabled = false
                        )

                        /**
                         * Duration Picker for AddTodoTask
                         */
                        TextField(
                            value = durationValue.value.toDuration(),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_alarm),
                                    contentDescription = "DurationIcon",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            onValueChange = {},
                            colors = myAppTextFieldColors(),
                            modifier = Modifier
                                .requiredWidth(168.dp)
                                .padding(horizontal = 20.dp)
                                .clickable(onClick = {
                                    scope.launch { bottomDrawerState.open() }
                                }),
                            enabled = false
                        )
                    }
                    Log.e("todo", "after show picker")

                    Divider(thickness = 2.dp)
                    Box(modifier = Modifier.padding(20.dp)) {

                    }
                    Divider(thickness = 2.dp)
                    Box(modifier = Modifier.padding(20.dp)) {
                        TextField(
                            value = task.value.desc,
                            onValueChange = {
                                viewModel.onEvent(
                                    AddEditTodoEvent.EnteredDescription(
                                        it
                                    )
                                )
                            },
                            colors = myAppTextFieldColors(),
                            shape = Shapes.large,
                            label = { Text(stringResource(R.string.description)) },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),

                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            ),
                            modifier = Modifier
                                .clearFocusOnKeyboardDismiss()
                                .fillMaxWidth()
                                .height(120.dp)
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, false)
                        .padding(innerPadding).fillMaxHeight()
                ) {
                    Button(onClick = { navController.navigate(Screen.HomeScreen.route) }) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = {
                        viewModel.onEvent(AddEditTodoEvent.SaveNote(TaskType.DEADLINE))
                        Toast.makeText(
                            context,
                            "Task Added",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate(Screen.HomeScreen.route)
                    }) {
                        Text(text = "DONE")
                    }
                }

            }


        }

    }
}


//@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
//@Preview
//@Composable
//fun AddDeadlineScreenPreview() {
//    val navController = rememberNavController()
//    val currentScreen = Screen.AddDeadlineScreen
//    LifelineTheme {
//        Scaffold(
//            content = { AddDeadlineScreen(navController) },
//            bottomBar = {
//                BottomNav(
//                    navController = navController)
//            }
//        )
//    }
//}