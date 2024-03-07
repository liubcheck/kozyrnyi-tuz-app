package liubomyr.stepanenko.kozyrnyituzapp.screens.visit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import liubomyr.stepanenko.kozyrnyituzapp.model.Visit

@Composable
internal fun VisitsPage(confirmedVisitsViewModel: ConfirmedVisitsViewModel = viewModel()) {
    VisitsListScreen(confirmedVisitsViewModel)
}

@Composable
private fun VisitsListScreen(confirmedVisitsViewModel: ConfirmedVisitsViewModel = viewModel()) {
    val confirmedVisits = confirmedVisitsViewModel.confirmedVisits.observeAsState(initial = emptyList())
    VisitsList(confirmedVisits = confirmedVisits.value)
}

@Composable
private fun VisitsList(confirmedVisits: List<Visit>) {
    LazyColumn {
        items(confirmedVisits) { confirmedVisit ->
            ConfirmedVisitRow(confirmedVisit = confirmedVisit)
        }
    }
}



@Composable
private fun ConfirmedVisitRow(confirmedVisit: Visit) {
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