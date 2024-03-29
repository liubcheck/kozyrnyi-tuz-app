package liubomyr.stepanenko.kozyrnyituzapp.ui.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun Item(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}