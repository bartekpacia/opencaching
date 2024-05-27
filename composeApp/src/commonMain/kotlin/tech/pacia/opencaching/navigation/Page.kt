package tech.pacia.opencaching.navigation

interface Page

expect class SignInPage() : Page

expect class MapPage() : Page

expect class GeocachePage(code: String) : Page {
    val code: String
}