package com.app.feature_withdraw.ui.voice_recording

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File

/** Thin wrapper around MediaRecorder. Records AAC/M4A, hard-capped at [MAX_DURATION_MS]. */
class VoiceRecorder(private val context: Context) {

    companion object {
        const val MAX_DURATION_MS = 30_000
    }

    private var recorder: MediaRecorder? = null
    private var file: File? = null

    /** @param onMaxReached called by the system when the 30s cap is hit. */
    fun start(onMaxReached: () -> Unit) {
        val outFile = File(context.cacheDir, "voice_${System.currentTimeMillis()}.m4a")
        val r = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }

        try {
            r.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128_000)
                setAudioSamplingRate(44_100)
                setMaxDuration(MAX_DURATION_MS)
                setOutputFile(outFile.absolutePath)
                setOnInfoListener { _, what, _ ->
                    if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) onMaxReached()
                }
                prepare()
                start()
            }
        } catch (e: Exception) {
            r.release()
            outFile.delete()
            throw e
        }

        recorder = r
        file = outFile
    }

    /** Stops and keeps the file. Returns null if nothing usable was recorded. */
    fun stop(): File? {
        val f = file
        val ok = try {
            recorder?.stop()
            true
        } catch (e: RuntimeException) {
            false // stop() throws if stopped immediately after start
        } finally {
            release()
        }
        return if (ok && f != null && f.exists()) f else { f?.delete(); null }
    }

    /** Stops and deletes the file. */
    fun cancel() {
        val f = file
        runCatching { recorder?.stop() }
        release()
        f?.delete()
    }

    private fun release() {
        recorder?.release()
        recorder = null
        file = null
    }
}
