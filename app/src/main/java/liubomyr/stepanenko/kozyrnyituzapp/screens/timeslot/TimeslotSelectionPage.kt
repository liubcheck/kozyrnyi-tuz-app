package liubomyr.stepanenko.kozyrnyituzapp.screens.timeslot

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.navigation.NavController
import com.google.gson.Gson
import kotlinx.coroutines.delay
import liubomyr.stepanenko.kozyrnyituzapp.model.VisitInfo
import liubomyr.stepanenko.kozyrnyituzapp.screens.cart.getSavedVisits
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.HeaderScreen
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.Item
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter

@Composable
fun TimeslotSelectionPage(barberId: Long, navController: NavController) {
    val context = LocalContext.current // Get the local context to use for SharedPreferences
    var showConfirmation by remember { mutableStateOf(false) }

    val initialTimeslot = LocalDateTime.of(2024, Month.MAY, 6, 10, 0)
    val timeslots = List(5) { index ->
        initialTimeslot.plusMinutes(30L * index)
    }

    HeaderScreen(text = "Timeslot for Barber $barberId", canBack = true) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showConfirmation) {
                Item {
                    ShowTemporaryMessage(message = "Visit added to cart") {
                        navController.navigate("cart")
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
                ) {
                   items(timeslots) { timeslot ->
                       TimeslotOption(timeslot, barberId, context) {
                           showConfirmation = true
                       }
                   }
                }
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
