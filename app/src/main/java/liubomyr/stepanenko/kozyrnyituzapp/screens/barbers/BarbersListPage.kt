package liubomyr.stepanenko.kozyrnyituzapp.screens.barbers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import liubomyr.stepanenko.kozyrnyituzapp.model.Barber
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.AsyncImage
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.HeaderScreen
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.Item
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.OurLazyColumn

@Composable
fun BarbersListPage(barbershopId: String, barbersViewModel: BarbersViewModel = viewModel(), navController: NavController) {
    val barbers by barbersViewModel.barbers.observeAsState(initial = null)

    LaunchedEffect(barbershopId) {
        barbersViewModel.loadBarberById(barbershopId)
    }

    if (barbers == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
    else {
        HeaderScreen(text = "Select Your Barber", canBack = true) {
            BarbersList(barbers = barbers!!, navController = navController)
        }
    }
}

@Composable
fun BarbersList(barbers: List<Barber>, navController: NavController) {
    OurLazyColumn {
        items(barbers) { barber ->
            BarberRow(barber = barber, navController = navController)
        }
    }
}

@Composable
fun BarberRow(barber: Barber, navController: NavController) {
    Item {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                AsyncImage(
                    url = barber.imageUrl,
                    height = 200
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${barber.firstName} ${barber.lastName}",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Button(
                    onClick = { navController.navigate("timeslotSelection/${barber.id}") }
                ) {
                    Text("Take Visit", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
