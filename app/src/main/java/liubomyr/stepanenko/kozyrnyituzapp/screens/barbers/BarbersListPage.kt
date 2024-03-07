package liubomyr.stepanenko.kozyrnyituzapp.screens.barbers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import liubomyr.stepanenko.kozyrnyituzapp.R
import liubomyr.stepanenko.kozyrnyituzapp.model.Barber
import liubomyr.stepanenko.kozyrnyituzapp.screens.home.HomeViewModel

@Composable
fun BarbersListPage(barbersViewModel: BarbersViewModel = viewModel(), navController: NavController) {
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
