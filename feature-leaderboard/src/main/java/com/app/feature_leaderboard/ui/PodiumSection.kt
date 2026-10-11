package com.app.feature_leaderboard.ui


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.feature_leaderboard.remote.model.response.LeaderboardListResponse

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.cos
import kotlin.math.sin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.app.core_ui.R
import com.app.core_ui.components.AsyncImageWithLoader
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.PI
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds


// Color Palette for Gold, Silver, Bronze theme
val DarkBgStart = Color(0xFF140D2B)
val DarkBgEnd = Color(0xFF07040D)

val GoldPrimary = Color(0xFFFFD700)
val GoldGlow = Color(0xFFFFAA00)
val SilverPrimary = Color(0xFFE0E0E0)
val BronzePrimary = Color(0xFFCD7F32)

data class PodiumUser(
    val rank: Int,              // 1, 2 or 3
    val name: String,
    val score: Int,
    val avatar: Painter? = null // pass painterResource(...) or a Coil painter
)


/* ───────────────────────── Palette ───────────────────────── */

private val GoldLight = Color(0xFFFFE082)
private val Gold = Color(0xFFFFB300)
private val GoldDark = Color(0xFFE08A00)
private val SilverLight = Color(0xFFF3F4F6)
private val Silver = Color(0xFF9CA3AF)
private val BronzeLight = Color(0xFFFFC8A2)
private val Bronze = Color(0xFFD2733F)
private val Orange = Color(0xFFFF6A00)
private val Ink = Color(0xFF1F2430)

private fun rankColors(rank: Int): Triple<Color, Color, List<Color>> = when (rank) {
    1 -> Triple(GoldLight, GoldDark, listOf(GoldLight, Gold, Color.White, Gold, GoldLight))
    2 -> Triple(SilverLight, Silver, listOf(SilverLight, Silver, Color.White, Silver, SilverLight))
    else -> Triple(
        BronzeLight,
        Bronze,
        listOf(BronzeLight, Bronze, Color.White, Bronze, BronzeLight)
    )
}

/* ───────────────────────── Screen ───────────────────────── */

@Composable
fun LeaderboardScreen(users: List<PodiumUser>, modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFFF4DC), Color(0xFFFFFBF2), Color.White)
                )
            )
            .statusBarsPadding()
            .padding(top = 24.dp)
    ) {
//        PodiumSection(topThreeUsers = users, Modifier.fillMaxWidth())
    }
}

/* ───────────────────────── Podium ───────────────────────── */

@Composable
fun PodiumSection(
    topThreeUsers: List<LeaderboardListResponse.LeaderboardData.LeaderBoardItem?>?,
    modifier: Modifier = Modifier
) {

    Log.d("kfbnkfnbf", topThreeUsers.toString())

    val first = topThreeUsers?.first { it?.rank == 1 }
    val second = topThreeUsers?.first { it?.rank == 2 }
    val third = topThreeUsers?.first { it?.rank == 3 }

    BoxWithConstraints(modifier) {
        // Everything scales from the available width → works on phones, foldables, tablets
        val w = maxWidth.coerceAtMost(560.dp)
        val sideAvatar = w * 0.26f
        val centerAvatar = w * 0.33f
        val nameSp = (w.value * 0.042f).coerceIn(13f, 22f)
        val scoreSp = (w.value * 0.038f).coerceIn(12f, 20f)

        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            Box(Modifier.width(w)) {
                SunburstBackground(Modifier.matchParentSize())
                Sparkles(Modifier.matchParentSize())

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = w * 0.03f),
                    verticalAlignment = Alignment.Top
                ) {
                    SideColumn(
                        user = second, avatarSize = sideAvatar, nameSp = nameSp, scoreSp = scoreSp,
                        entranceDelay = 350,
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = w * 0.17f)
                    )
                    CenterColumn(
                        user = first,
                        avatarSize = centerAvatar,
                        nameSp = nameSp * 1.15f,
                        scoreSp = scoreSp * 1.1f,
                        entranceDelay = 700,
                        modifier = Modifier.weight(1.25f)
                    )
                    SideColumn(
                        user = third, avatarSize = sideAvatar, nameSp = nameSp, scoreSp = scoreSp,
                        entranceDelay = 150,
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = w * 0.14f)
                    )
                }
            }
        }
    }
}

/* ───────────────────────── Columns ───────────────────────── */

@Composable
private fun CenterColumn(
    user: LeaderboardListResponse.LeaderboardData.LeaderBoardItem?,
    avatarSize: Dp,
    nameSp: Float,
    scoreSp: Float,
    entranceDelay: Int,
    modifier: Modifier = Modifier
) {
    val entrance = rememberEntrance(entranceDelay)
    val floatY by rememberInfiniteTransition(label = "float").animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(2600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "floatY"
    )

    Column(
        modifier
            .entrance(entrance)
            .graphicsLayer { translationY = (floatY - 0.5f) * 6.dp.toPx() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Medal(1, avatarSize * 0.62f, Modifier.zIndex0())
        Spacer(Modifier.height(2.dp))
        Avatar(
            image = user?.profilePic,
            initials = user?.fullName?.take(1) ?: "",
            size = avatarSize,
            ringColors = rankColors(1).third,
            ringWidth = 5.dp,
            spinRing = true,
            glow = true
        )
        // Name card overlaps the avatar like the reference
        Column(
            Modifier
                .offset(y = -(avatarSize * 0.2f))
                .shadow(14.dp, RoundedCornerShape(26.dp), ambientColor = Gold, spotColor = Gold)
                .background(Color.White, RoundedCornerShape(26.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicText(
                user?.fullName ?: "",
                style = TextStyle(
                    color = Orange,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = nameSp.sp,
                    textAlign = TextAlign.Center
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                autoSize = TextAutoSize.StepBased(minFontSize = 5.sp, maxFontSize = nameSp.sp)

            )
            Spacer(Modifier.height(6.dp))
            CoinPill(user?.beans ?: 0, scoreSp, delay = entranceDelay + 300)
        }
    }
}

@Composable
private fun SideColumn(
    user: LeaderboardListResponse.LeaderboardData.LeaderBoardItem?,
    avatarSize: Dp,
    nameSp: Float,
    scoreSp: Float,
    entranceDelay: Int,
    modifier: Modifier = Modifier
) {
    val entrance = rememberEntrance(entranceDelay)
    val (light, dark, ring) = rankColors(user?.rank ?: 0)

    Column(modifier.entrance(entrance), horizontalAlignment = Alignment.CenterHorizontally) {
        Medal(user?.rank ?: 0, avatarSize * 0.7f)
        Box(contentAlignment = Alignment.BottomCenter) {
            Avatar(
                image = user?.profilePic ?: "",
                initials = user?.fullName?.take(1) ?: "",
                size = avatarSize,
                ringColors = ring,
                ringWidth = 4.dp,
                spinRing = false,
                glow = false
            )
            // little rank badge
            Box(
                Modifier
                    .offset(y = avatarSize * 0.14f)
                    .size(avatarSize * 0.26f)
                    .shadow(6.dp, CircleShape)
                    .background(Brush.verticalGradient(listOf(light, dark)), CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "${user?.rank ?: 0}", color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = (avatarSize.value * 0.13f).sp
                )
            }
        }
        Spacer(Modifier.height(avatarSize * 0.2f))
        BasicText(
            user?.fullName ?: "",
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontSize = nameSp.sp,
                textAlign = TextAlign.Center
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            autoSize = TextAutoSize.StepBased(minFontSize = 5.sp, maxFontSize = nameSp.sp)

        )

        Spacer(Modifier.height(8.dp))
        CoinPill(user?.beans ?: 0, scoreSp, delay = entranceDelay + 300)
    }
}

/* ───────────────────────── Avatar ───────────────────────── */

@Composable
private fun Avatar(
    image: Any?,
    initials: String,
    size: Dp,
    ringColors: List<Color>,
    ringWidth: Dp,
    spinRing: Boolean,
    glow: Boolean
) {
    val inf = rememberInfiniteTransition(label = "avatar")
    val angle by inf.animateFloat(
        0f, 360f, infiniteRepeatable(tween(5000, easing = LinearEasing)), label = "angle"
    )
    val pulse by inf.animateFloat(
        0.35f,
        0.9f,
        infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse"
    )

    Box(
        Modifier
            .size(size)
            .drawBehind {
                if (glow) {
                    drawCircle(
                        Brush.radialGradient(
                            listOf(Gold.copy(alpha = pulse * 0.55f), Color.Transparent),
                            radius = this.size.minDimension * 0.85f
                        ),
                        radius = this.size.minDimension * 0.85f
                    )
                }
                rotate(if (spinRing) angle else 0f) {
                    drawCircle(Brush.sweepGradient(ringColors))
                }
            }
            .padding(ringWidth)
            .clip(CircleShape)
            .background(Color.White)
            .padding(2.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (image != null) {
            AsyncImageWithLoader(image = image, Modifier.fillMaxSize())
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Brush.linearGradient(listOf(Color(0xFFFFD6A5), Color(0xFFFF9A62)))),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    initials, color = Color.White, fontWeight = FontWeight.Bold,
                    fontSize = (size.value * 0.4f).sp
                )
            }
        }
    }
}

/* ───────────────────────── Medal ───────────────────────── */

@Composable
private fun Medal(rank: Int, size: Dp, modifier: Modifier = Modifier) {
    val (light, dark, _) = rankColors(rank)
    val scale = remember { Animatable(0f) }
    val sway = rememberInfiniteTransition(label = "sway").animateFloat(
        -4f, 4f, infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "swayAnim"
    )
    LaunchedEffect(Unit) {
        delay(if (rank == 1) 1000 else 600)
        scale.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
    }

    Box(
        modifier
            .size(size)
            .graphicsLayer {
                scaleX = scale.value; scaleY = scale.value
                rotationZ = sway.value * (if (rank == 1) 1f else 0.6f)
            },
        contentAlignment = Alignment.Center
    ) {
        val medalImage = when(rank){
            1 -> R.drawable.rank_1
            2 -> R.drawable.rank_2
            3 -> R.drawable.rank_3
            else -> R.drawable.rank_1
        }
        Image(painter = painterResource(medalImage), contentDescription = null,modifier = Modifier.fillMaxSize())
       /* Canvas(Modifier.fillMaxSize()) {
            val w = this.size.width
            val cx = w / 2f
            val cy = this.size.height * 0.45f
            val r = w * 0.24f

            // ribbon tails
            for (s in listOf(-1f, 1f)) {
                val p = Path().apply {
                    moveTo(cx + s * r * 0.15f, cy + r * 0.6f)
                    lineTo(cx + s * r * 0.95f, cy + r * 0.6f)
                    lineTo(cx + s * r * 1.15f, cy + r * 2.0f)
                    lineTo(cx + s * r * 0.55f, cy + r * 1.65f)
                    lineTo(cx + s * r * 0.1f, cy + r * 2.0f)
                    close()
                }
                drawPath(p, Brush.verticalGradient(listOf(light, dark)))
            }

            // laurel leaves
            for (s in listOf(-1f, 1f)) {
                for (i in 0..5) {
                    val a = 105f + i * 26f                // degrees on the left side
                    val ang = if (s < 0) a else 180f - a
                    val rad = ang * PI.toFloat() / 180f
                    val R = r * 1.6f
                    val lx = cx + R * cos(rad)
                    val ly = cy + R * sin(rad)
                    rotate(ang + 90f + (if (s < 0) 25f else -25f), Offset(lx, ly)) {
                        drawOval(
                            Brush.linearGradient(listOf(light, dark)),
                            topLeft = Offset(lx - r * 0.2f, ly - r * 0.38f),
                            size = Size(r * 0.4f, r * 0.76f)
                        )
                    }
                }
            }

            // medal disc
            drawCircle(Brush.verticalGradient(listOf(light, dark)), r * 1.15f, Offset(cx, cy))
            drawCircle(
                Color.White.copy(alpha = 0.55f), r * 1.15f, Offset(cx, cy),
                style = androidx.compose.ui.graphics.drawscope.Stroke(w * 0.012f)
            )
            drawCircle(Brush.verticalGradient(listOf(dark, light)), r * 0.88f, Offset(cx, cy))
        }
        Text(
            "$rank", color = Color.White, fontWeight = FontWeight.ExtraBold,
            fontSize = (size.value * 0.27f).sp,
            modifier = Modifier.offset(y = -(size * 0.05f))
        )*/
    }
}

/* ───────────────────────── Coin pill ───────────────────────── */

@Composable
private fun CoinPill(score: Int, textSp: Float, delay: Int) {
    val counter = remember { Animatable(0f) }
    LaunchedEffect(score) {
        delay(delay.milliseconds)
        counter.animateTo(score.toFloat(), tween(1500, easing = FastOutSlowInEasing))
    }
    val shimmer by rememberInfiniteTransition(label = "shimmer").animateFloat(
        -0.5f, 1.5f,
        infiniteRepeatable(tween(2400, easing = LinearEasing), RepeatMode.Restart),
        label = "shimmerX"
    )
    val shape = RoundedCornerShape(50)
    val fmt = remember { NumberFormat.getIntegerInstance(Locale("en", "IN")) }

    Row(
        Modifier
            .shadow(6.dp, shape, ambientColor = Gold.copy(0.4f), spotColor = Gold.copy(0.4f))
            .clip(shape)
            .background(Color.White)
            .border(
                BorderStroke(
                    1.5.dp,
                    Brush.horizontalGradient(listOf(GoldLight, Gold, GoldLight))
                ), shape
            )
            .drawWithShimmer(shimmer)
            .padding(horizontal = 7.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Image(
            painter = painterResource(com.app.core_ui.R.drawable.multi_coinsimage46),
            contentDescription = null,
            Modifier.size((textSp * 1.2f).dp)
        )
        BasicText(
            text = "₹ ${fmt.format(counter.value.toInt())}",
            style = TextStyle(
                color = Ink,
                fontWeight = FontWeight.Bold,
                fontSize = textSp.sp
            ),
            autoSize = TextAutoSize.StepBased(minFontSize = 5.sp, maxFontSize = textSp.sp),
            maxLines = 2,
        )
    }
}

private fun Modifier.drawWithShimmer(progress: Float): Modifier = this.drawWithContentCompat {
    drawContent()
    val x = size.width * progress
    drawRect(
        Brush.linearGradient(
            listOf(Color.Transparent, Color.White.copy(alpha = 0.75f), Color.Transparent),
            start = Offset(x, 0f), end = Offset(x + size.width * 0.35f, size.height)
        )
    )
}

@SuppressLint("SuspiciousModifierThen")
private fun Modifier.drawWithContentCompat(
    block: androidx.compose.ui.graphics.drawscope.ContentDrawScope.() -> Unit
): Modifier = this.then(drawWithContent(onDraw = block))

@Composable
private fun CoinStack(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        fun coin(cx: Float, cy: Float, r: Float) {
            drawOval(
                Brush.verticalGradient(listOf(GoldLight, GoldDark), cy - r, cy + r),
                Offset(cx - r, cy - r * 0.55f), Size(r * 2, r * 1.1f)
            )
            drawOval(
                Color.White.copy(0.45f),
                Offset(cx - r * 0.7f, cy - r * 0.35f),
                Size(r * 1.4f, r * 0.7f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(w * 0.03f)
            )
        }
        coin(w * 0.32f, w * 0.72f, w * 0.26f)
        coin(w * 0.62f, w * 0.68f, w * 0.26f)
        coin(w * 0.47f, w * 0.45f, w * 0.28f)
        coin(w * 0.47f, w * 0.30f, w * 0.28f)
    }
}

/* ───────────────────────── Background effects ───────────────────────── */

@Composable
private fun SunburstBackground(modifier: Modifier = Modifier) {
    val rot by rememberInfiniteTransition(label = "rays").animateFloat(
        0f, 360f, infiniteRepeatable(tween(60000, easing = LinearEasing)), label = "rot"
    )
    val appear = remember { Animatable(0f) }
    LaunchedEffect(Unit) { delay(500); appear.animateTo(1f, tween(1400)) }

    Canvas(modifier.graphicsLayer { alpha = appear.value }) {
        val c = Offset(size.width / 2f, size.height * 0.2f)
        val len = size.width * 0.95f
        translate(c.x, c.y) {
            rotate(rot, Offset.Zero) {
                val n = 20
                for (i in 0 until n) {
                    val a1 = (i * 2 * PI / n).toFloat()
                    val a2 = a1 + (PI / n * 0.8f).toFloat()
                    val p = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(len * cos(a1), len * sin(a1))
                        lineTo(len * cos(a2), len * sin(a2))
                        close()
                    }
                    drawPath(
                        p,
                        Brush.radialGradient(
                            listOf(Gold.copy(0.28f), Color.Transparent),
                            center = Offset.Zero, radius = len
                        )
                    )
                }
            }
        }
        // soft halo
        drawCircle(
            Brush.radialGradient(
                listOf(Color(0xFFFFE9A8).copy(0.7f), Color.Transparent),
                center = c, radius = size.width * 0.5f
            ),
            radius = size.width * 0.5f, center = c
        )
    }
}

private data class Spark(
    val x: Float,
    val y: Float,
    val r: Float,
    val speed: Float,
    val phase: Float
)

@Composable
private fun Sparkles(modifier: Modifier = Modifier) {
    val sparks = remember {
        val rnd = Random(11)
        List(26) {
            Spark(
                rnd.nextFloat(),
                rnd.nextFloat(),
                1.5f + rnd.nextFloat() * 3f,
                0.2f + rnd.nextFloat() * 0.6f,
                rnd.nextFloat() * 6.28f
            )
        }
    }
    val t by rememberInfiniteTransition(label = "sparks").animateFloat(
        0f, 1f, infiniteRepeatable(tween(9000, easing = LinearEasing)), label = "t"
    )
    Canvas(modifier) {
        sparks.forEach { s ->
            val y = ((s.y - t * s.speed) % 1f + 1f) % 1f
            val twinkle = (sin(t * 2f * PI.toFloat() * 3f + s.phase) * 0.5f + 0.5f)
            val center = Offset(s.x * size.width, y * size.height)
            val r = s.r.dp.toPx()
            drawCircle(Gold.copy(alpha = 0.15f + 0.6f * twinkle), r * 2.2f, center)
            drawCircle(Color.White.copy(alpha = 0.9f * twinkle), r * 0.8f, center)
        }
    }
}

/* ───────────────────────── Entrance helpers ───────────────────────── */

@Composable
private fun rememberEntrance(delayMillis: Int): Animatable<Float, AnimationVector1D> {
    val a = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(delayMillis.toLong())
        a.animateTo(1f, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow))
    }
    return a
}

private fun Modifier.entrance(a: Animatable<Float, AnimationVector1D>): Modifier =
    graphicsLayer {
        alpha = a.value.coerceIn(0f, 1f)
        val s = 0.7f + 0.3f * a.value
        scaleX = s; scaleY = s
        translationY = (1f - a.value) * 90.dp.toPx()
    }

// tiny no-op so the Medal call-site reads cleanly (keeps z-order as declared)
private fun Modifier.zIndex0(): Modifier = this

/* ───────────────────────── Preview ───────────────────────── */

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun PodiumPreview() {
    LeaderboardScreen(
        listOf(
            PodiumUser(1, "Aisha Sharma", 90430),
            PodiumUser(2, "Anaya Singh", 83300),
            PodiumUser(3, "Aarohi Mehta", 80200)
        )
    )
}



