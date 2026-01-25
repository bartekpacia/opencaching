package tech.pacia.opencaching.features.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.pacia.opencaching.ui.theme.OpencachingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    state: SignInState = SignInState.Idle,
    onSignInClicked: () -> Unit = {},
    onSignInSkipped: () -> Unit = {},
    onDismissError: () -> Unit = {},
) {
    val isLoading = state is SignInState.Loading
    val errorMessage = (state as? SignInState.Error)?.message

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Opencaching") }) },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Sign in to access your geocaching profile, log finds, and more.",
                modifier = Modifier.padding(horizontal = 24.dp),
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onSignInClicked,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(fraction = 0.75f).padding(8.dp),
            ) {
                Text("Sign in with Opencaching.pl")
            }

            TextButton(
                onClick = onSignInSkipped,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(fraction = 0.75f).padding(8.dp),
            ) {
                Text("Continue without signing in")
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.3f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { /* block interaction */ },
                ),
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Signing in...")
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator()
            }
        }
    }

    if (errorMessage != null) {
        AlertDialog(
            icon = {
                Icon(
                    Icons.Rounded.ErrorOutline,
                    contentDescription = "Error",
                )
            },
            title = { Text(text = "Sign in failed") },
            text = { Text(text = errorMessage) },
            onDismissRequest = onDismissError,
            confirmButton = {
                TextButton(onClick = onDismissError) {
                    Text("OK")
                }
            },
        )
    }
}

@Preview
@Composable
fun SignInScreenPreview() {
    OpencachingTheme {
        SignInScreen()
    }
}

@Preview
@Composable
fun SignInScreenLoadingPreview() {
    OpencachingTheme {
        SignInScreen(state = SignInState.Loading)
    }
}

@Preview
@Composable
fun SignInScreenWithErrorPreview() {
    OpencachingTheme {
        SignInScreen(state = SignInState.Error("Network error occurred"))
    }
}
