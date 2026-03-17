package dev.csse.nperlich.clappybird

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.abs

class SoundDetector(
    private val onClapDetected: () -> Unit
) {
    private var audioRecord: AudioRecord? = null
    private var isListening = false
    private var listeningJob: Job? = null

    private val sampleRate = 44100
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

    // Threshold for clap detection
    private val clapThreshold = 15000

    fun startListening() {
        if (isListening) return

        try {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfig,
                audioFormat,
                bufferSize
            )

            audioRecord?.startRecording()
            isListening = true

            listeningJob = CoroutineScope(Dispatchers.IO).launch {
                val buffer = ShortArray(bufferSize)

                while (isListening) {
                    val readSize = audioRecord?.read(buffer, 0, bufferSize) ?: 0

                    if (readSize > 0) {
                        // Calculate amplitude (loudness)
                        val amplitude = buffer.maxOfOrNull { abs(it.toInt()) } ?: 0

                        // If amplitude exceeds threshold, it's a clap!
                        if (amplitude > clapThreshold) {
                            onClapDetected()
                            // Small delay to prevent multiple triggers from one clap
                            kotlinx.coroutines.delay(200)
                        }
                    }
                }
            }
        } catch (e: SecurityException) {
            // Permission not granted
            e.printStackTrace()
        }
    }

    fun stopListening() {
        isListening = false
        listeningJob?.cancel()
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }
}