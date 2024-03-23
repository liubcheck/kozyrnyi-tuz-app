package liubomyr.stepanenko.kozyrnyituzapp.ui.core

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import liubomyr.stepanenko.kozyrnyituzapp.localNavController

@Composable
internal fun HeaderScreen(
    text: String,
    canBack: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    val navController = localNavController.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary)
                .padding(8.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            if (canBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(28.dp)
                        .clickable(
                            onClick = navController::popBackStack,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false),
                        )
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )
        }
        content()
    }
}