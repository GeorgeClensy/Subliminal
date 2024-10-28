package com.geecee.subliminal

import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.geecee.subliminal.ui.TextboxDialog
import com.geecee.subliminal.ui.theme.SubliminalTheme
import com.geecee.subliminal.ui.views.CardsPage
import com.geecee.subliminal.ui.views.HomePage
import com.geecee.subliminal.ui.views.SetsPage
import com.geecee.subliminal.ui.views.refreshSets
import com.geecee.subliminal.utils.Set
import com.geecee.subliminal.utils.createSet
import com.geecee.subliminal.utils.loadSets
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(SystemBarStyle.auto(TRANSPARENT, TRANSPARENT))

        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.apply {
            hide(WindowInsetsCompat.Type.statusBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        setContent {
            SubliminalTheme {
                MainNavigation()
            }
        }
    }
}

@Composable
fun MainNavigation() {
    //Context
    val context = LocalContext.current

    //Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    //Navigation
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(
        stringResource(R.string.Home),
        stringResource(R.string.Sets),
        stringResource(R.string.Start)
    )
    val selectedIcons =
        listOf(Icons.Filled.Home, Icons.AutoMirrored.Filled.List, Icons.Filled.PlayArrow)
    val unselectedIcons =
        listOf(Icons.Outlined.Home, Icons.AutoMirrored.Outlined.List, Icons.Outlined.PlayArrow)
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    //Sets creation
    val showNewSetDialog = remember { mutableStateOf(false) }
    val sets = remember { mutableStateOf(loadSets(context)) }
    val currentSet = remember {
        mutableStateOf(
            Set(
                "",
                null,
                listOf(

                )
            )
        )
    }

    //Main app scaffold
    Scaffold(
        topBar = {
            //No topBar
        },
        bottomBar = {
            AnimatedVisibility(
                visible = currentRoute != "Cards",
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                                    contentDescription = item
                                )
                            },
                            label = {
                                Text(
                                    item,
                                    style = com.geecee.subliminal.ui.theme.Typography.bodyMedium
                                )
                            },
                            alwaysShowLabel = false,
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index

                                //Navigate to the page corresponding to the clicked index
                                navController.navigate(
                                    when (selectedItem) {
                                        0 -> "Home"
                                        1 -> "Sets"
                                        2 -> "Start"
                                        else -> "Home"
                                    }
                                )
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = currentRoute == "Sets",
                enter = fadeIn(tween(300)) + scaleIn(tween(300)),
                exit = fadeOut(tween(300)) + scaleOut(tween(300))
            ) {
                FloatingActionButton(onClick = {
                    showNewSetDialog.value = true
                    refreshSets(context, sets)
                }) {
                    Icon(Icons.Rounded.Add, contentDescription = "Add")
                }
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->
        NavHost(navController, "Home", Modifier.padding(innerPadding)) {
            composable("Home",
                enterTransition = { fadeIn(tween(500)) },
                exitTransition = { fadeOut(tween(500)) }) {
                HomePage()
            }
            composable("Sets",
                enterTransition = { fadeIn(tween(500)) },
                exitTransition = { fadeOut(tween(500)) }) {
                SetsPage(sets, currentSet, navController)
            }
            composable("Cards",
                enterTransition = { fadeIn(tween(500)) },
                exitTransition = { fadeOut(tween(500)) }) {
                CardsPage(currentSet, navController, sets, snackbarHostState)
            }
            composable("Options",
                enterTransition = { fadeIn(tween(500)) },
                exitTransition = { fadeOut(tween(500)) }) {
            }
            composable("Start",
                enterTransition = { fadeIn(tween(500)) },
                exitTransition = { fadeOut(tween(500)) }) {
            }
        }

        //New set dialog
        if (showNewSetDialog.value) {
            TextboxDialog(
                showNewSetDialog,
                "New Sets",
                "Type here..",
                "Create",
                "Cancel"
            ) { output ->
                var alreadyExists = false

                sets.value.forEach { set ->
                    if (set.title == output) {
                        alreadyExists = true
                    }
                }

                if (!alreadyExists) {
                    createSet(context, output, null)
                } else {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Set with this name already exists",
                            actionLabel = "Ok"
                        )
                    }
                }

                refreshSets(context, sets)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainNavPrev() {
    SubliminalTheme {
        MainNavigation()
    }
}