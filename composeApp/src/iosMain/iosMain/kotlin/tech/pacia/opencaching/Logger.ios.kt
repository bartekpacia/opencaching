package tech.pacia.opencaching

import platform.Foundation.NSLog

internal actual fun debugLog(tag: String, message: String) {
    NSLog("$tag: $message")
}