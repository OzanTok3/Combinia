package com.ozantok.combinia.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ozantok.combinia.R

val Ancizar = FontFamily(
    Font(R.font.ancizarserif_regular, FontWeight.Normal),
    Font(R.font.ancizarserif_bold, FontWeight.Bold),
    Font(R.font.ancizarserif_medium, FontWeight.Medium)
)

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = Ancizar,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Ancizar,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Ancizar,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Ancizar,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    // Diğer stilleri de buraya ekleyebilirsin
)
