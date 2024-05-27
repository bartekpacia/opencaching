package tech.pacia.opencaching.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
actual class SignInPage : Page, Parcelable

@Parcelize
actual class MapPage : Page, Parcelable

@Parcelize
actual class GeocachePage actual constructor(actual val code: String) : Page, Parcelable