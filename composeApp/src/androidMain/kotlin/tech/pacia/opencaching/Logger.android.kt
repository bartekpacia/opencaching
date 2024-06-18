package tech.pacia.opencaching

import android.util.Log

internal actual fun debugLog(tag: String, message: String) {
    Log.d(tag, message)
}
