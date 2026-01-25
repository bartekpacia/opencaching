import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    let signInViewModel: SignInViewModel

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(signInViewModel: signInViewModel)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    let signInViewModel: SignInViewModel

    var body: some View {
        ComposeView(signInViewModel: signInViewModel)
                .ignoresSafeArea(edges: .all)
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}
