package liubomyr.stepanenko.kozyrnyituzapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import liubomyr.stepanenko.kozyrnyituzapp.model.AddVisitRequest
import liubomyr.stepanenko.kozyrnyituzapp.model.Barber
import liubomyr.stepanenko.kozyrnyituzapp.model.Barbershop
import liubomyr.stepanenko.kozyrnyituzapp.model.NavigationItem
import liubomyr.stepanenko.kozyrnyituzapp.model.Visit
import liubomyr.stepanenko.kozyrnyituzapp.model.VisitInfo
import liubomyr.stepanenko.kozyrnyituzapp.page.BarbersViewModel
import liubomyr.stepanenko.kozyrnyituzapp.page.ConfirmedVisitsViewModel
import liubomyr.stepanenko.kozyrnyituzapp.page.HomeViewModel
import liubomyr.stepanenko.kozyrnyituzapp.page.VisitsViewModel
import liubomyr.stepanenko.kozyrnyituzapp.ui.theme.KozyrnyiTuzAppTheme
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KozyrnyiTuzAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) { paddingValues ->
                    NavHost(navController = navController, startDestination = "home",
                        Modifier.padding(paddingValues)) {
                        composable("home") { HomePage(navController) }
                        composable("barbershopDetail/{barbershopId}") { backStackEntry ->
                            BarbershopDetailPage(barbershopId = backStackEntry.arguments?.getString("barbershopId")
                                ?: "", navController, homeViewModel = viewModel())
                        }
                        composable("barberList") {
                            BarbersListScreen(barbersViewModel = viewModel(), navController)
                        }
                        composable("timeslotSelection/{barberId}") { backStackEntry ->
                            TimeslotSelectionPage(barberId = backStackEntry.arguments?.getString("barberId")?.toLongOrNull() ?: return@composable, navController)
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

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem("Home", Icons.Filled.Home),
        NavigationItem("Search", Icons.Filled.Search),
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

@Composable
fun HomePage(navController: NavController) {
    BarbershopListScreen(navController = navController)
}

@Composable
fun BarbershopListScreen(homeViewModel: HomeViewModel = viewModel(), navController: NavController) {
    val barbershops = homeViewModel.barbershops.observeAsState(initial = emptyList())

    Column {
        Text(
            text = "Our Barbershops",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(16.dp)
        )
        BarbershopList(barbershops = barbershops.value, navController = navController)
    }
}

@Composable
fun BarbershopList(barbershops: List<Barbershop>, navController: NavController) {
    LazyColumn {
        items(barbershops) { barbershop ->
            BarbershopRow(barbershop = barbershop, navController = navController)
        }
    }
}

@Composable
fun BarbershopRow(barbershop: Barbershop, navController: NavController) {
    Column(modifier = Modifier
        .clickable {
            navController.navigate("barbershopDetail/${barbershop.id}")
        }
        .padding(16.dp)) {
        Text(text = barbershop.name, style = MaterialTheme.typography.titleLarge)
        Text(text = barbershop.address, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun BarbershopDetailPage(barbershopId: String, navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
    val barbershopIdLong = barbershopId.toLongOrNull() ?: return
    val barbershop = homeViewModel.selectedBarbershop.observeAsState().value
    LaunchedEffect(barbershopIdLong) {
        homeViewModel.loadBarbershopById(barbershopIdLong)
    }
    if (barbershop != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painterResource(id = R.drawable.barbershop),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = barbershop.name, style = MaterialTheme.typography
                .headlineLarge.copy(fontWeight = FontWeight.Bold))
            Text(text = barbershop.address)
            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = { navController.navigate("barberList") }) {
                Text("Select Barber", style = MaterialTheme.typography.bodyLarge)
            }
        }
    } else {
        Text("Loading barbershop details...")
    }
}

@Composable
fun BarbersListScreen(barbersViewModel: BarbersViewModel = viewModel(), navController: NavController) {
    val barbers = barbersViewModel.barbers.observeAsState(initial = emptyList())

    Column {
        Text(
            text = "Select Your Barber",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(16.dp)
        )
        BarbersList(barbers = barbers.value, navController = navController)
    }
}

@Composable
fun BarbersList(barbers: List<Barber>, navController: NavController) {
    LazyColumn {
        items(barbers) { barber ->
            BarberRow(barber = barber, navController = navController)
        }
    }
}

@Composable
fun BarberRow(barber: Barber, navController: NavController) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        Column() {
            Image(
                painterResource(id = R.drawable.barber),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(modifier = Modifier.padding(start = 16.dp),
                text = "${barber.firstName} ${barber.lastName}",
                style = MaterialTheme.typography.titleLarge)
            Text(modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                text = "An awesome barber", style = MaterialTheme.typography.bodySmall)
        }
        Button(onClick = { navController.navigate("timeslotSelection/${barber.id}") }) {
            Text("Take Visit", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun TimeslotSelectionPage(barberId: Long, navController: NavController) {
    val context = LocalContext.current // Get the local context to use for SharedPreferences
    var showConfirmation by remember { mutableStateOf(false) }

    val initialTimeslot = LocalDateTime.of(2024, Month.MAY, 6, 10, 0)
    val timeslots = List(5) { index ->
        initialTimeslot.plusMinutes(30L * index)
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Select a Timeslot for Barber $barberId",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(45.dp))

        if (showConfirmation) {
            ShowTemporaryMessage(message = "Visit added to cart") {
                navController.navigate("cart")
            }
        } else {
            timeslots.forEach { timeslot ->
                TimeslotOption(timeslot, barberId, context) {
                    showConfirmation = true
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TimeslotOption(timeslot: LocalDateTime, barberId: Long, context: Context, onSelect: () -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    Button(
        onClick = {
            val visitInfo = VisitInfo(barberId, timeslot.format(formatter))
            saveVisit(context, visitInfo)
            onSelect()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(timeslot.format(formatter), style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun SearchPage() {
    Text("Search Page")
}

@Composable
fun VisitsPage(confirmedVisitsViewModel: ConfirmedVisitsViewModel = viewModel()) {
    VisitsListScreen(confirmedVisitsViewModel)
}

@Composable
fun VisitsListScreen(confirmedVisitsViewModel: ConfirmedVisitsViewModel = viewModel()) {
    val confirmedVisits = confirmedVisitsViewModel.confirmedVisits.observeAsState(initial = emptyList())
    VisitsList(confirmedVisits = confirmedVisits.value)
}

@Composable
fun VisitsList(confirmedVisits: List<Visit>) {
    LazyColumn {
        items(confirmedVisits) { confirmedVisit ->
            ConfirmedVisitRow(confirmedVisit = confirmedVisit)
        }
    }
}

@Composable
fun ConfirmedVisitRow(confirmedVisit: Visit) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        Column() {
            Text(modifier = Modifier.padding(start = 16.dp),
                text = "${confirmedVisit.datetime}",
                style = MaterialTheme.typography.titleLarge)
            Text(modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                text = "Barber ID: ${confirmedVisit.barberId}", style = MaterialTheme.typography.bodyMedium)
            Text(modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                text = "User ID: ${confirmedVisit.userId}", style = MaterialTheme.typography.bodyMedium)
            Text(modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                text = "Time: ${confirmedVisit.durationMin} m", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ShoppingCartPage(visitsViewModel: VisitsViewModel = viewModel()) {
    val context = LocalContext.current
    var refreshVisits by remember { mutableStateOf(false) }

    val savedVisits = remember(refreshVisits) { mutableStateOf(getSavedVisits(context)) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Your Selected Visits",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(45.dp))
        LazyColumn {
            items(savedVisits.value) { visit ->
                VisitRow(visit, visitsViewModel, context) {
                    refreshVisits = !refreshVisits
                }
            }
        }
    }
}

@Composable
fun VisitRow(visitInfo: VisitInfo, visitsViewModel: VisitsViewModel,
             context: Context, onVisitConfirmed: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Barber ID: ${visitInfo.barberId}")
            Text("Time: ${visitInfo.datetime}")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            val addVisitRequest = AddVisitRequest(
                barberId = visitInfo.barberId,
                userId = 2L, // Static user ID for demonstration
                datetime = parseStringToLocalDateTime(visitInfo.datetime),
                durationMin = 30
            )
            // Updated to include onSuccess and onError lambdas
            visitsViewModel.confirmVisit(addVisitRequest, context, onSuccess = {
                // onSuccess lambda
                removeVisitFromSharedPreferences(context, visitInfo)
                onVisitConfirmed() // This will trigger UI refresh
                Toast.makeText(context, "Visit confirmed successfully", Toast.LENGTH_SHORT).show()
            }, onError = { errorMessage ->
                // onError lambda
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            })
        }) {
            Text("Confirm Visit")
        }
    }
}

@Composable
fun ProfilePage(onExitClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Профіль",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painterResource(id = R.drawable.profile),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Юрій", style = MaterialTheme.typography.titleLarge)
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Profile options list
        ProfileOption("Мої бронювання")
        ProfileOption("Мої промокоди")
        ProfileOption("Вихід") {
            onExitClick()
        }
    }
}

@Composable
fun ProfileOption(title: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Go to $title",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ShowTemporaryMessage(message: String, onDismiss: () -> Unit) {
    var show by remember { mutableStateOf(true) }

    if (show) {
        Text(text = message, modifier = Modifier.padding(16.dp)) // Customize as needed
        LaunchedEffect(key1 = true) {
            delay(2000) // Show message for 2 seconds
            show = false
            onDismiss()
        }
    }
}

fun saveVisit(context: Context, visitInfo: VisitInfo) {
    val prefs = context.getSharedPreferences("Visits", Context.MODE_PRIVATE)
    val visits = getSavedVisits(context).toMutableList()
    visits.add(visitInfo)
    prefs.edit {
        putString("visits", Gson().toJson(visits))
    }
}

fun getSavedVisits(context: Context): List<VisitInfo> {
    val prefs = context.getSharedPreferences("Visits", Context.MODE_PRIVATE)
    val json = prefs.getString("visits", null) ?: return emptyList()
    val type = object : TypeToken<List<VisitInfo>>() {}.type
    return Gson().fromJson(json, type)
}

private fun removeVisitFromSharedPreferences(context: Context, visitInfo: VisitInfo) {
    val prefs = context.getSharedPreferences("Visits", Context.MODE_PRIVATE)
    val visits = getSavedVisits(context).toMutableList()
    visits.removeIf { it.barberId == visitInfo.barberId } // This should work now
    prefs.edit {
        putString("visits", Gson().toJson(visits))
    }
}

private fun parseStringToLocalDateTime(dateString: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    return LocalDateTime.parse(dateString, formatter)
}
