package liubomyr.stepanenko.kozyrnyituzapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import liubomyr.stepanenko.kozyrnyituzapp.model.NavigationItem
import liubomyr.stepanenko.kozyrnyituzapp.screens.auth.AuthPage
import liubomyr.stepanenko.kozyrnyituzapp.screens.barbers.BarbersListPage
import liubomyr.stepanenko.kozyrnyituzapp.screens.cart.ShoppingCartPage
import liubomyr.stepanenko.kozyrnyituzapp.screens.home.BarbershopDetailPage
import liubomyr.stepanenko.kozyrnyituzapp.screens.home.HomePage
import liubomyr.stepanenko.kozyrnyituzapp.screens.profile.ProfilePage
import liubomyr.stepanenko.kozyrnyituzapp.screens.search.SearchPage
import liubomyr.stepanenko.kozyrnyituzapp.screens.timeslot.TimeslotSelectionPage
import liubomyr.stepanenko.kozyrnyituzapp.screens.visit.VisitsPage
import liubomyr.stepanenko.kozyrnyituzapp.ui.theme.KozyrnyiTuzAppTheme

val localNavController = compositionLocalOf<NavController> { error("") }

class MainActivity : ComponentActivity() {
    private val bottomBarsScreen = listOf("home", "search", "visits", "cart", "profile")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KozyrnyiTuzAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                CompositionLocalProvider(
                    localNavController provides navController
                ) {
                    Scaffold(
                        bottomBar = {
                            val route = navBackStackEntry?.destination?.route ?: return@Scaffold
                            if (bottomBarsScreen.contains(route)) {
                                BottomNavigationBar(navController)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    ) { paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = "auth",
                            modifier = Modifier.padding(paddingValues),
                            enterTransition = { EnterTransition.None },
                            exitTransition = { ExitTransition.None },
                            popEnterTransition = { EnterTransition.None },
                            popExitTransition = { ExitTransition.None },
                        ) {
                            composable("auth") { AuthPage(navController) }
                            composable("home") { HomePage(navController) }
                            composable("barbershopDetail/{barbershopId}") { backStackEntry ->
                                BarbershopDetailPage(
                                    barbershopId = backStackEntry.arguments?.getString("barbershopId")
                                    ?: "",
                                    navController = navController,
                                    homeViewModel = viewModel()
                                )
                            }
                            composable("barberList") {
                                BarbersListPage(barbersViewModel = viewModel(), navController)
                            }
                            composable("timeslotSelection/{barberId}") { backStackEntry ->
                                TimeslotSelectionPage(
                                    barberId = backStackEntry.arguments?.getString("barberId")?.toLongOrNull() ?: return@composable,
                                    navController = navController
                                )
                            }
                            composable("search") { SearchPage() }
                            composable("visits") {
                                VisitsPage(confirmedVisitsViewModel = viewModel())
                            }
                            composable("cart") { ShoppingCartPage(visitsViewModel = viewModel()) }
                            composable("profile") {
                                ProfilePage(onExitClick = { this@MainActivity.finish() })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem("Home", Icons.Filled.Home),
        // NavigationItem("Search", Icons.Filled.Search),
        NavigationItem("Visits", Icons.Filled.List),
        NavigationItem("Cart", Icons.Filled.ShoppingCart),
        NavigationItem("Profile", Icons.Filled.Person)
    )
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.title) },
                selected = navController.currentDestination?.route == item.title.lowercase(),
                onClick = {
                    navController.navigate(item.title.lowercase()) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

