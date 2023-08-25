package com.media.gallery.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.media.gallery.R

val promptFamily = FontFamily(
    Font(R.font.prompt_black, FontWeight.Black),
    Font(R.font.prompt_black_italic, FontWeight.Black, FontStyle.Italic),
    Font(R.font.prompt_bold, FontWeight.Bold),
    Font(R.font.prompt_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.prompt_extra_bold, FontWeight.ExtraBold),
    Font(R.font.prompt_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.prompt_extra_light, FontWeight.ExtraLight),
    Font(R.font.prompt_extra_light_italic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.prompt_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.prompt_light, FontWeight.Light),
    Font(R.font.prompt_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.prompt_medium, FontWeight.Medium),
    Font(R.font.prompt_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.prompt_regular, FontWeight.Normal),
    Font(R.font.prompt_semi_bold, FontWeight.SemiBold),
    Font(R.font.prompt_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.prompt_thin, FontWeight.Thin),
    Font(R.font.prompt_thin_italic, FontWeight.Thin, FontStyle.Italic)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)