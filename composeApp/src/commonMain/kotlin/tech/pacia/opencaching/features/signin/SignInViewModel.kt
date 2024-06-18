package tech.pacia.opencaching.features.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class SignInViewModel : ViewModel() {
    private val _inProgress = MutableStateFlow(false)
    val inProgress: StateFlow<Boolean> = _inProgress

    private val _hasError = MutableStateFlow(false)
    val hasError: StateFlow<Boolean> = _hasError

    fun signIn(
        username: String,
        password: String,
        onSignInComplete: () -> Unit,
    ) {
        viewModelScope.launch {
            _inProgress.value = true
            _hasError.value = false

            delay(1.seconds) // userRepository.signIn(username, password)

            if (username.isBlank() || password.isBlank()) {
                _inProgress.value = false
                _hasError.value = true
                return@launch
            }

            if (username != "bartek" || password != "123") {
                _inProgress.value = false
                _hasError.value = true
                return@launch
            }

            _inProgress.value = false
            onSignInComplete()
        }
    }

    fun dismissError() {
        _hasError.value = false
    }
}
