package tech.pacia.opencaching.features.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tech.pacia.opencaching.data.AuthRepository

sealed interface SignInState {
    data object Idle : SignInState
    data object Loading : SignInState
    data class OpenBrowser(val url: String) : SignInState
    data class Error(val message: String) : SignInState
    data object Success : SignInState
}

class SignInViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<SignInState>(SignInState.Idle)
    val state: StateFlow<SignInState> = _state

    /**
     * Starts the OAuth flow by requesting authorization URL.
     * The UI should open the returned URL in a browser.
     */
    fun startSignIn() {
        viewModelScope.launch {
            _state.value = SignInState.Loading
            try {
                val authUrl = authRepository.startOAuthFlow()
                _state.value = SignInState.OpenBrowser(authUrl)
            } catch (e: Exception) {
                _state.value = SignInState.Error(e.message ?: "Failed to start sign in")
            }
        }
    }

    /**
     * Completes the OAuth flow after receiving the callback with verifier.
     *
     * @param verifier The oauth_verifier from the callback URL
     */
    fun handleOAuthCallback(verifier: String) {
        viewModelScope.launch {
            _state.value = SignInState.Loading
            try {
                authRepository.completeOAuthFlow(verifier)
                _state.value = SignInState.Success
            } catch (e: Exception) {
                _state.value = SignInState.Error(e.message ?: "Failed to complete sign in")
            }
        }
    }

    /**
     * Called after the browser was opened to reset state.
     */
    fun onBrowserOpened() {
        _state.value = SignInState.Idle
    }

    fun dismissError() {
        _state.value = SignInState.Idle
    }
}
