import SwiftUI
import shared

@main
struct IOSApp: App {

    private let appContainer = AppContainer(platformContext: nil)

    init() {
        Task {
            try? await appContainer.doInit()
        }
    }

    var body: some Scene {
        WindowGroup {
            ComposeView(appContainer: appContainer)
                .ignoresSafeArea(.all)
        }
    }
}

struct ComposeView: UIViewControllerRepresentable {
    let appContainer: AppContainer

    func makeUIViewController(context: Context) -> UIViewController {
        return MainViewControllerKt.MainViewController(appContainer: appContainer)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}