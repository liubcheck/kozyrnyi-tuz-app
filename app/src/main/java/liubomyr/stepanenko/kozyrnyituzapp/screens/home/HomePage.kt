package liubomyr.stepanenko.kozyrnyituzapp.screens.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import liubomyr.stepanenko.kozyrnyituzapp.model.Barbershop
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.AsyncImage
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.HeaderScreen
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.Item
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.OurLazyColumn

@Composable
internal fun HomePage(navController: NavController) {
    BarbershopListScreen(navController = navController)
}

@Composable
private fun BarbershopListScreen(homeViewModel: HomeViewModel = viewModel(), navController: NavController) {
    val barbershops = homeViewModel.barbershops.observeAsState(initial = emptyList())

    HeaderScreen(text = "Our Barbershops") {
        BarbershopList(barbershops = barbershops.value, navController = navController)
    }
}

@Composable
private fun BarbershopList(barbershops: List<Barbershop>, navController: NavController) {
    OurLazyColumn {
        items(barbershops) { barbershop ->
            BarbershopRow(barbershop = barbershop, navController = navController)
        }
    }
}

@Composable
private fun BarbershopRow(barbershop: Barbershop, navController: NavController) {
    Item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("barbershopDetail/${barbershop.id}") }
                .padding(16.dp)
        ) {
            Text(text = barbershop.name, style = MaterialTheme.typography.titleLarge)
            Text(text = barbershop.address, style = MaterialTheme.typography.bodySmall)
        }
    }
}


@Composable
internal fun BarbershopDetailPage(barbershopId: String, navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
    val content = LocalContext.current

    val barbershopIdLong = barbershopId.toLongOrNull() ?: return
    val barbershop = homeViewModel.selectedBarbershop.observeAsState().value
    LaunchedEffect(barbershopIdLong) {
        homeViewModel.loadBarbershopById(barbershopIdLong)
    }

    if (barbershop != null) {
        HeaderScreen(text = barbershop.name, canBack = true) {
            Item(
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        url = barbershop.imageUrl,
                        height = 300
                    )
                    Text(text = barbershop.address)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Button(onClick = { navController.navigate("barberList/$barbershopId") }) {
                            Text("Select Barber", style = MaterialTheme.typography.bodyLarge)
                        }
                        Button(onClick = {
                            val geoUri = "http://maps.google.com/maps?q=loc:${barbershop.latitude},${barbershop.longitude}"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
                            content.startActivity(intent)
                        }) {
                            Text(text = "Go To Location", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
