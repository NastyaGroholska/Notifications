package com.ahrokholska.notifications.presentation.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.ahrokholska.notifications.R
import com.ahrokholska.notifications.presentation.theme.CounterStyle
import com.ahrokholska.notifications.presentation.theme.MainButtonStyle
import com.ahrokholska.notifications.presentation.theme.NotificationsTheme

@Composable
fun Page(
    modifier: Modifier = Modifier,
    number: Int,
    onlyOnePageExists: Boolean,
    onCreateClick: () -> Unit,
    onRemoveClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier.size(166.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp),
            onClick = onCreateClick
        ) {
            Text(
                text = stringResource(R.string.create_new_notification),
                style = MainButtonStyle,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1.6f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 29.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    .padding(vertical = 11.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$number",
                    style = CounterStyle,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (!onlyOnePageExists) {
                    ActionButton(icon = Icons.Default.Remove, onClick = onRemoveClick)
                } else {
                    Spacer(modifier = Modifier.width(actionButtonSize))
                }
                ActionButton(icon = Icons.Default.Add, onClick = onAddClick)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ActionButton(icon: ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(actionButtonSize)
            .shadow(elevation = 3.dp, shape = CircleShape)
            .background(color = Color.White, shape = CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = icon.name,
            tint = MaterialTheme.colorScheme.background
        )
    }
}

private val actionButtonSize = 56.dp

@PreviewLightDark
@Composable
private fun PagePreview() {
    NotificationsTheme {
        Page(
            number = 1,
            onlyOnePageExists = false,
            onCreateClick = {},
            onRemoveClick = {},
            onAddClick = {})
    }
}