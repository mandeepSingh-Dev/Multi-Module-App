package com.app.core_ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.core_ui.R
import com.app.core_ui.ui.theme.theme_color_17
import com.app.core_ui.ui.theme.theme_color_3
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class StoryProgressItem(
    val progress : Float = 0f,
    val index : Int
)



const val PROGRESS_FACTOR =  0.025f
const val PROGRESS_DELAY = 300

@Composable
@Preview
fun storyProgressBars(){

    val count = 5

    val mutableList = remember { mutableStateListOf<StoryProgressItem>() }

    val pagerState = rememberPagerState() { count }


    LaunchedEffect(Unit) {
        repeat(count){
        val item = StoryProgressItem(
            progress = 0f,
            index = it
        )
            mutableList.add(item)
    }
    }


    var currentIndex by remember{ mutableIntStateOf(0) }

    val coroutineScope = rememberCoroutineScope()

    var job : Job? = remember { null }

    LaunchedEffect(pagerState.currentPage) {
//        repeat(count){
            job?.cancel()
            job = coroutineScope.launch {
                var progress = 0f
                val currentItem = mutableList[currentIndex]

                while(progress <= 1f){
                    delay(PROGRESS_DELAY.milliseconds)
                    progress += PROGRESS_FACTOR
                    val uItem = currentItem.copy(progress = progress)
                    mutableList.set(index = currentIndex, uItem)
                }
            }
            job.join()
//        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .systemBarsPadding()) {

        Row(modifier = Modifier.fillMaxWidth().padding(top = 50.dp)) {
            mutableList.forEachIndexed { index, item ->
                val value by animateFloatAsState(item.progress)
                LinearProgressIndicator(
                    gapSize = 0.dp,
                    drawStopIndicator = {},
                    progress = {
                        value
                    },
                    color = theme_color_17,
                    modifier = Modifier.weight(1f)
                )
            }
        }


        HorizontalPager(state = pagerState) {
            Image(painter = painterResource(R.drawable.girl_large), contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
                    .clickable{
                        currentIndex = it+1
                    }
                    .padding(top = 20.dp, bottom = 10.dp), contentDescription = null,
            )
        }
    }
}