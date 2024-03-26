package liubomyr.stepanenko.kozyrnyituzapp.screens.barbers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import liubomyr.stepanenko.kozyrnyituzapp.R
import liubomyr.stepanenko.kozyrnyituzapp.model.Barber
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.HeaderScreen
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.Item
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.OurLazyColumn

@Composable
fun BarbersListPage(barbersViewModel: BarbersViewModel = viewModel(), navController: NavController) {
    val barbers = barbersViewModel.barbers.observeAsState(initial = emptyList())

    HeaderScreen(text = "Select Your Barber", canBack = true) {
        BarbersList(barbers = barbers.value, navController = navController)

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
                SubcomposeAsyncImage(
                    // model = "https://fastly.picsum.photos/id/1014/200/300.jpg?hmac=nxBnyyuXuAKEA6yVxBtNN4YjpjaciQXA3KwTRICTlWU",
                    model = barber.imageUrl,
                    modifier = Modifier.heightIn(200.dp),
                    contentScale = ContentScale.FillHeight,
                    contentDescription = null,
                    loading = { CircularProgressIndicator() },
                )
                Text(
                    text = "${barber.firstName} ${barber.lastName}",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "An awesome barber",
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
