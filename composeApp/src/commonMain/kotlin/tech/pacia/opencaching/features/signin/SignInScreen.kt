package tech.pacia.opencaching.features.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import tech.pacia.opencaching.ui.theme.OpencachingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    inProgress: Boolean = false,
    hasError: Boolean = false,
    onDismissError: () -> Unit = {},
    onSignInSubmitted: (email: String, password: String) -> Unit = { _, _ -> },
    onSignInSkipped: () -> Unit = {},
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }

    val localFocusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier.pointerInput(Unit) { detectTapGestures(onTap = { localFocusManager.clearFocus() }) },
        topBar = { TopAppBar(title = { Text("Opencaching") }) },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                modifier = Modifier.padding(8.dp),
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = "Username",
                    )
                },
            )

            OutlinedTextField(
                modifier = Modifier.padding(8.dp),
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = "Password",
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        onClick = { showPassword = !showPassword },
                    ) {
                        Icon(
                            imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (showPassword) "Hide password" else "Show password",
                        )
                    }
                },
            )

            Button(
                onClick = { onSignInSubmitted(username, password) },
                modifier = Modifier.fillMaxWidth(fraction = 0.75f).padding(8.dp),
            ) {
                Text("Sign in")
            }

            TextButton(
                onClick = onSignInSkipped,
                modifier = Modifier.fillMaxWidth(fraction = 0.75f).padding(8.dp),
            ) {
                Text("Continue without signing in")
            }
        }
    }

    if (inProgress) {
        Box(
            modifier = Modifier.fillMaxSize().background(color = Color.Transparent).clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }, // This is mandatory
                    onClick = { /* block interaction with the UI behind the dialog */ },
                ),
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Signing in...",
                )

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator()
            }
        }
    }

    if (hasError) {
        AlertDialog(
            icon = {
                Icon(
                    Icons.Rounded.ErrorOutline,
                    contentDescription = "Example Icon",
                )
            },
            title = { Text(text = "dialogTitle") },
            text = { Text(text = "dialogText") },
            onDismissRequest = onDismissError,
            confirmButton = {
                TextButton(
                    onClick = onDismissError,
                ) {
                    Text("Try again")
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
fun SignInScreenWithErrorPreview() {
    OpencachingTheme {
        SignInScreen(hasError = true)
    }
}
