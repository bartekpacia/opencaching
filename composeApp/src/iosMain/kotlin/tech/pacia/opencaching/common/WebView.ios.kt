package tech.pacia.opencaching.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIViewController
import platform.WebKit.WKWebView

@ExperimentalForeignApi
@Composable
actual fun WebView(
    modifier: Modifier,
    html: String,
) {
    val webView = remember { WebViewController(html) }

    UIKitViewController(
        factory = { webView },
        modifier = modifier.fillMaxSize(),
        update = {},
        properties = UIKitInteropProperties(
            isInteractive = true,
            isNativeAccessibilityEnabled = true,
        ),
    )
}

class WebViewController(private val html: String) : UIViewController(
    nibName = null,
    bundle = null,
) {
    private var webView: WKWebView? = null

    override fun loadView() {
        webView = WKWebView()
        // webView!!.navigationDelegate = this
        view = webView!!
    }

    override fun viewDidLoad() {
        super.viewDidLoad()

        webView!!.loadHTMLString(html, null)
    }
}
