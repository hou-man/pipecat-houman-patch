package ai.pipecat.small_webrtc_client.ui

import ai.pipecat.client.small_webrtc_transport.views.VideoView
import ai.pipecat.client.types.Tracks
import ai.pipecat.small_webrtc_client.ui.theme.Colors
import android.view.ViewGroup.LayoutParams
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.FloatState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun BotIndicator(
    modifier: Modifier,
    isReady: Boolean,
    isTalking: State<Boolean>,
    audioLevel: FloatState,
    tracks: State<Tracks?>
) {
    Box(
        modifier = modifier.padding(15.dp),
        contentAlignment = Alignment.Center
    ) {
        val color by animateColorAsState(if (isTalking.value || !isReady) {
            Color.Black
        } else {
            Colors.botIndicatorBackground
        })

        Box(
            Modifier
                .aspectRatio(1f)
                .fillMaxSize()
                .shadow(20.dp, CircleShape)
                .border(12.dp, Color.White, CircleShape)
                .border(1.dp, Colors.lightGrey, CircleShape)
                .clip(CircleShape)
                .background(color)
                .padding(50.dp),
            contentAlignment = Alignment.Center,
        ) {
            AnimatedContent(
                targetState = isReady
            ) { isReadyVal ->
                if (isReadyVal) {

                    val botVideoTrack by remember { derivedStateOf {
                        tracks.value?.bot?.video
                    }}

                    if (botVideoTrack != null) {
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { context ->
                                VideoView(context).apply {
                                    layoutParams = LayoutParams(
                                        LayoutParams.MATCH_PARENT,
                                        LayoutParams.MATCH_PARENT
                                    )
                                }
                            },
                            update = {
                                it.track = botVideoTrack
                            }
                        )
                    } else {
                        ListeningAnimation(
                            modifier = Modifier.fillMaxSize(),
                            active = isTalking.value,
                            level = audioLevel.floatValue,
                            color = Color.White
                        )
                    }
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(180.dp),
                        color = Color.White,
                        strokeWidth = 12.dp,
                        strokeCap = StrokeCap.Round,
                        trackColor = color
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewBotIndicator() {
    BotIndicator(
        modifier = Modifier,
        isReady = false,
        isTalking = remember { mutableStateOf(true) },
        audioLevel = remember { mutableFloatStateOf(1.0f) },
        tracks = remember { mutableStateOf(null) }
    )
}