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
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.HeaderScreen
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.Item
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.OurLazyColumn

@Composable
internal fun VisitsPage(confirmedVisitsViewModel: ConfirmedVisitsViewModel = viewModel()) {
    VisitsListScreen(confirmedVisitsViewModel)
}

@Composable
private fun VisitsListScreen(confirmedVisitsViewModel: ConfirmedVisitsViewModel = viewModel()) {
    val confirmedVisits = confirmedVisitsViewModel.confirmedVisits.observeAsState(initial = emptyList())
    HeaderScreen(text = "Visits") {
        VisitsList(confirmedVisits = confirmedVisits.value)
    }
}

@Composable
private fun VisitsList(confirmedVisits: List<Visit>) {
    OurLazyColumn {
        items(confirmedVisits) { confirmedVisit ->
            ConfirmedVisitRow(confirmedVisit = confirmedVisit)
        }
    }
}

@Composable
private fun ConfirmedVisitRow(confirmedVisit: Visit) {
    Item {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Visit to ${confirmedVisit.barbershop.name}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Barber: ${confirmedVisit.barber.firstName} ${confirmedVisit.barber.lastName}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "User: ${confirmedVisit.user.name}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Duration: ${confirmedVisit.durationMin} min",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}