package com.example.appdepelis.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

object MovieAnimations {

    val fadeInSpec = fadeIn(
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearOutSlowInEasing
        )
    )

    val fadeOutSpec = fadeOut(
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutLinearInEasing
        )
    )

    val slideInFromBottomSpec = slideInVertically(
        initialOffsetY = { it },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    val slideOutToBottomSpec = slideOutVertically(
        targetOffsetY = { it },
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutLinearInEasing
        )
    )

    val scaleInSpec = scaleIn(
        initialScale = 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val scaleOutSpec = scaleOut(
        targetScale = 0.8f,
        animationSpec = tween(
            durationMillis = 200,
            easing = FastOutSlowInEasing
        )
    )
}

@Composable
fun Modifier.shimmerEffect(): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val alpha = transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )
    return this.graphicsLayer { this.alpha = alpha.value }
}