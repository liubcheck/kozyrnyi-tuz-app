package liubomyr.stepanenko.kozyrnyituzapp.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import liubomyr.stepanenko.kozyrnyituzapp.R
import liubomyr.stepanenko.kozyrnyituzapp.model.Barbershop

@Composable
internal fun HomePage(navController: NavController) {
    BarbershopListScreen(navController = navController)
}

@Composable
private fun BarbershopListScreen(homeViewModel: HomeViewModel = viewModel(), navController: NavController) {
    val barbershops = homeViewModel.barbershops.observeAsState(initial = emptyList())

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            text = "Our Barbershops",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        )
        BarbershopList(barbershops = barbershops.value, navController = navController)
    }
}

@Composable
private fun BarbershopList(barbershops: List<Barbershop>, navController: NavController) {
    LazyColumn {
        items(barbershops) { barbershop ->
            BarbershopRow(barbershop = barbershop, navController = navController)
        }
    }
}

@Composable
private fun BarbershopRow(barbershop: Barbershop, navController: NavController) {
    Column(modifier = Modifier
        .clickable { navController.navigate("barbershopDetail/${barbershop.id}") }
        .padding(16.dp)) {
        Text(text = barbershop.name, style = MaterialTheme.typography.titleLarge)
        Text(text = barbershop.address, style = MaterialTheme.typography.bodySmall)
    }
}


@Composable
internal fun BarbershopDetailPage(barbershopId: String, navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
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
