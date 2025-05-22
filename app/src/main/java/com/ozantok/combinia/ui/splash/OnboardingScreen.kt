package com.ozantok.combinia.ui.splash

import androidx.annotation.ColorRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ozantok.combinia.R
import com.ozantok.combinia.ui.theme.Typography
import com.ozantok.combinia.util.PreferencesManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(navController: NavHostController) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val systemUiController = rememberSystemUiController()
    val scope = rememberCoroutineScope()



    val pages = listOf(
        OnboardingPage(
            imageRes = R.drawable.onboarding1,
            title = "Tarzını Keşfet !",
            description = "Kendine en uygun kombinleri oluşturmak için ilham al. Kendi tarzını kendin belirle."
        ),
        OnboardingPage(
            imageRes = R.drawable.onboarding2,
            title = "Gardırobunu Düzenle",
            description = "Favori kıyafetlerini kategorilere ayır, kombinlerini kaydet, hiçbir şey kaybolmasın."
        ),
        OnboardingPage(
            imageRes = R.drawable.onboarding3,
            title = "Detaylarda Gizli Şıklık !",
            description = "Moda detaylarda gizlidir. Kombinlerini paylaş, yorum al, birlikte geliş."
        )
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pages.size }
    )
    val pageColorList = listOf(
        colorResource(id = R.color.nearly_white),
        colorResource(id = R.color.light_beige),
        colorResource(id = R.color.light_beige)
    )

    val backgroundColor by remember {
        derivedStateOf { pageColorList[pagerState.currentPage] }
    }


    val selectedColor = colorResource(id = R.color.dark_beige)
    val unselectedColor = colorResource(id = R.color.beige)

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = backgroundColor,
            darkIcons = true
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = pages[page].imageRes),
                        contentDescription = "Onboarding Image",
                        modifier = Modifier.fillMaxSize()
                    )

                    Text(
                        text = pages[page].title,
                        style = Typography.headlineLarge,
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = pages[page].description,
                    style = Typography.headlineMedium,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Devam / Başla Butonu
                Button(
                    onClick = {
                        if (pagerState.currentPage == pages.lastIndex) {
                            preferencesManager.setOnboardingShown(true)
                            navController.navigate("login") { // ← Burayı "home" yerine "login" yaptık
                                popUpTo("onboarding") { inclusive = true }
                            }
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                        .height(56.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = if (pagerState.currentPage == pages.lastIndex) "Başla" else "Devam",
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                // Sayfa indikatörleri
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp), // Hepsine eşit yükseklik
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .size(if (isSelected) 12.dp else 8.dp)
                                .background(
                                    color = if (isSelected) selectedColor else unselectedColor,
                                    shape = CircleShape
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
