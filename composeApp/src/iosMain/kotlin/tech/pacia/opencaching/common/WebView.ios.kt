package tech.pacia.opencaching.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIViewController
import platform.WebKit.WKUIDelegateProtocol
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.darwin.NSObject

@ExperimentalForeignApi
@Composable
actual fun WebView(
    modifier: Modifier,
    html: String,
) {
    val wkWebView = remember {
        val frame = cValue<CGRect> { CGRectZero }
        val configuration = WKWebViewConfiguration()
        
        val webView = WKWebView(frame = frame, configuration = configuration)
//        val uiDelegate = WebViewDelegate(html = html)
//        webView.setUIDelegate(uiDelegate)
        webView.loadHTMLString(html, baseURL = null)
        
        webView
    }

    UIKitView(
        modifier = modifier.fillMaxSize(),
        factory = { wkWebView },
        update = { },
    )
}

class WebViewDelegate(
    private val html: String,
) : UIViewController(), WKUIDelegateProtocol {

    override fun viewDidLoad() {
        super.viewDidLoad()
    }
}