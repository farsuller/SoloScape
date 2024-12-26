package com.soloscape.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font

import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.soloscape.ui.R

val robotoThinFontFamily = FontFamily(
    Font(R.font.roboto_thin)
)

val robotoThinItalicFontFamily = FontFamily(
    Font(R.font.roboto_thin_italic)
)

val robotoRegularFontFamily = FontFamily(
    Font(R.font.roboto_regular)
)

val robotoMediumFontFamily = FontFamily(
    Font(R.font.roboto_medium)
)

val robotoMediumItalicFontFamily = FontFamily(
    Font(R.font.roboto_medium_italic)
)

val robotoLightItalicFontFamily = FontFamily(
    Font(R.font.roboto_light_italic)
)

val robotoLightFontFamily = FontFamily(
    Font(R.font.roboto_light)
)

val robotoItalicFontFamily = FontFamily(
    Font(R.font.roboto_italic)
)

val robotoBoldFontFamily = FontFamily(
    Font(R.font.roboto_bold)
)

val robotoBlackFontFamily = FontFamily(
    Font(R.font.roboto_black, FontWeight.Normal)
)

val robotoBlackItalicFontFamily = FontFamily(
    Font(R.font.roboto_black_italic, FontWeight.Normal)
)

val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = robotoRegularFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = robotoRegularFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = robotoRegularFontFamily),

    headlineLarge = baseline.headlineLarge.copy(fontFamily = robotoBoldFontFamily, fontSize = 40.sp),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = robotoLightFontFamily, fontSize = 40.sp),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = robotoRegularFontFamily),

    titleLarge = baseline.titleLarge.copy(fontFamily = robotoBoldFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = robotoMediumFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = robotoRegularFontFamily),

    bodyLarge = baseline.bodyLarge.copy(fontFamily = robotoMediumFontFamily, fontSize = 30.sp),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = robotoLightFontFamily, fontSize = 20.sp),
    bodySmall = baseline.bodySmall.copy(fontFamily = robotoThinFontFamily, fontSize = 16.sp),

    labelLarge = baseline.labelLarge.copy(fontFamily = robotoRegularFontFamily, fontSize = 16.sp),
    labelMedium = baseline.labelMedium.copy(fontFamily = robotoRegularFontFamily, fontSize = 14.sp),
    labelSmall = baseline.labelSmall.copy(fontFamily = robotoRegularFontFamily, fontSize = 12.sp),
)
