import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    private let signInViewModel: SignInViewModel

    init() {
        let tokenStorage = TokenStorage()
        let authRepository = AuthRepository(tokenStorage: tokenStorage)
        signInViewModel = SignInViewModel(authRepository: authRepository)
    }

    var body: some Scene {
        WindowGroup {
            ContentView(signInViewModel: signInViewModel)
                .onOpenURL { url in
                    handleOAuthCallback(url: url)
                }
        }
    }

    private func handleOAuthCallback(url: URL) {
        guard url.scheme == "opencaching",
              url.host == "oauth",
              url.path == "/callback"
        else {
            return
        }

        let components = URLComponents(url: url, resolvingAgainstBaseURL: false)
        if let verifier = components?.queryItems?.first(where: { $0.name == "oauth_verifier" })?.value {
            signInViewModel.handleOAuthCallback(verifier: verifier)
        }
    }
}
