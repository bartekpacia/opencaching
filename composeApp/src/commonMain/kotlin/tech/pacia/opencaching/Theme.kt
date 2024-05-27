package tech.pacia.opencaching

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorsScheme = if (!darkTheme) lightColorScheme(
        primary = Color(0xFF97D782),
    ) else darkColorScheme(
        primary = Color(0xFF97D782),
        onPrimary = Color(0xFF043900),
        primaryContainer = Color.Red, // XD
        secondary = Color(0xFF5DDBBC),
        onSecondary = Color(0xFF00382D),
        onError = Color(0xFF690005),
        background = Color(0xFF002022),
        onBackground = Color(0xFF70F5FF),
        surface = Color(0xFF002022),
        onSurface = Color(0xFF70F5FF),
    )

//    val colors = if (!darkTheme) MaterialTheme.colors else MaterialTheme.colors.copy(
//        primary = Color(0xFF97D782),
//        onPrimary = Color(0xFF043900),
//        primaryContainer = Color.Red, // XD
//        secondary = Color(0xFF5DDBBC),
//        onSecondary = Color(0xFF00382D),
//        error = Color(0xFFFFB4AB),
//        onError = Color(0xFF690005),
//        background = Color(0xFF002022),
//        onBackground = Color(0xFF70F5FF),
//        surface = Color(0xFF002022),
//        onSurface = Color(0xFF70F5FF),
//    )

    val typography = Typography(
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colorScheme = colorsScheme,
        typography = typography,
        shapes = shapes,
        content = content
    )
}