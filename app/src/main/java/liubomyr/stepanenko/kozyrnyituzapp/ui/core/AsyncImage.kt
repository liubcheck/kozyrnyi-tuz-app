package liubomyr.stepanenko.kozyrnyituzapp.ui.core

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun AsyncImage(
    url: String,
    height: Int,
) {
    var isLoading by remember { mutableStateOf(true) }
    val modifier = if (isLoading) {
        Modifier
    } else {
        Modifier.border(
            5.dp,
            MaterialTheme.colorScheme.primary,
            shape = MaterialTheme.shapes.medium
        )
    }

    SubcomposeAsyncImage(
        model = url,
        modifier = Modifier
            .heightIn(height.dp)
            .clip(MaterialTheme.shapes.medium)
            .then(modifier),
        contentScale = ContentScale.FillHeight,
        contentDescription = null,
        loading = { CircularProgressIndicator() },
        onSuccess = {
            isLoading = false
        }
    )
}