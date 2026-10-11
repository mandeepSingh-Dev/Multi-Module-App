package com.app.core_ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun EmojiPickerComposable(
    onEmojiSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {


   /* AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp), // Set a fixed height or weight for the picker layout
        factory = { context ->
            EmojiPickerView(context).apply {
                // Set the click listener when an emoji is picked
                setOnEmojiPickedListener { emojiViewItem ->
                    onEmojiSelected(emojiViewItem.emoji)
                }
            }
        }
    )*/
}