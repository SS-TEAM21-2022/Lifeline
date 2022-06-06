package com.example.lifeline.presentation.task.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.lifeline.R
import com.example.lifeline.presentation.BottomNav
import com.example.lifeline.presentation.TopNav
import com.example.lifeline.presentation.components.PrioritySelector
import com.example.lifeline.presentation.ui.theme.LifelineTheme
import com.example.lifeline.presentation.ui.theme.Shapes
import com.example.lifeline.presentation.ui.theme.myAppTextFieldColors
import com.example.lifeline.util.Screen
import com.example.lifeline.util.clearFocusOnKeyboardDismiss

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddTodoScreen(navController: NavController) {
    var taskName by rememberSaveable { mutableStateOf("") }
    var desc by rememberSaveable { mutableStateOf("") }
    val currentScreen = Screen.AddTodoScreen

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = { TopNav(currentScreen, modifier = Modifier.background(Color.White)) },
        backgroundColor = Color.White
    ) { innerPadding ->
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
                        .height(80.dp)
                ) {
                    TextField(
                        value = taskName,
                        onValueChange = { taskName = it },
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
                Column(modifier = Modifier.padding(vertical = 20.dp)) {

                }
                Divider(thickness = 2.dp)
                PrioritySelector()
                Divider(thickness = 2.dp)
                TextField(
                    value = desc,
                    onValueChange = { desc = it },
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
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, false)
                    .padding(innerPadding)
            ) {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Cancel")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "DONE")
                }
            }

        }

    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun AddTodoScreenPreview() {
    val navController = rememberNavController()
    val currentScreen = Screen.AddTodoScreen
    LifelineTheme {
        Scaffold(
            content = { AddTodoScreen(navController) },
            bottomBar = {
                BottomNav(
                    navController = navController,
                    currentScreen = currentScreen
                )
            }
        )
    }
}