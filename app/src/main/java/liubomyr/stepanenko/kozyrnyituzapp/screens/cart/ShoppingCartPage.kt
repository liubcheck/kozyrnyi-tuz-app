package liubomyr.stepanenko.kozyrnyituzapp.screens.cart

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import liubomyr.stepanenko.kozyrnyituzapp.model.AddVisitRequest
import liubomyr.stepanenko.kozyrnyituzapp.model.VisitInfo
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.HeaderScreen
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.Item
import liubomyr.stepanenko.kozyrnyituzapp.ui.core.OurLazyColumn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
internal fun ShoppingCartPage(visitsViewModel: VisitsViewModel = viewModel()) {
    val context = LocalContext.current
    var refreshVisits by remember { mutableStateOf(false) }

    val savedVisits = remember(refreshVisits) { mutableStateOf(getSavedVisits(context)) }

    HeaderScreen(text = "Your Selected Visits") {
        OurLazyColumn {
            items(savedVisits.value) { visit ->
                VisitRow(visit, visitsViewModel, context) {
                    refreshVisits = !refreshVisits
                }
            }
        }
    }
}

@Composable
private fun VisitRow(
    visitInfo: VisitInfo,
    visitsViewModel: VisitsViewModel,
    context: Context,
    onVisitConfirmed: () -> Unit
) {
    Item {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                    Toast.makeText(context, "Visit confirmed successfully", Toast.LENGTH_SHORT)
                        .show()
                }, onError = { errorMessage ->
                    // onError lambda
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                })
            }) {
                Text("Confirm Visit")
            }
        }
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
