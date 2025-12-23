package com.oyenscilik.app.data.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Unified audio player manager that handles both pre-recorded audio files
 * and TTS fallback for a more natural learning experience.
 * 
 * Priority:
 * 1. Pre-recorded audio file (most natural)
 * 2. TTS fallback (when audio file not available)
 */
@Singleton
class AudioPlayerManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null
    private var tts: TextToSpeech? = null
    private var isTtsReady = false
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()
    
    private val _currentAudioId = MutableStateFlow<String?>(null)
    val currentAudioId = _currentAudioId.asStateFlow()
    
    companion object {
        private const val TAG = "AudioPlayerManager"
        // Base URL for audio files - should match backend uploads
        const val AUDIO_BASE_URL = "https://oyens-cilik-api.vercel.app/uploads/audio/"
    }
    
    init {
        initializeTts()
    }
    
    private fun initializeTts() {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Set Indonesian language for TTS
                val result = tts?.setLanguage(Locale("id", "ID"))
                isTtsReady = result != TextToSpeech.LANG_MISSING_DATA && 
                             result != TextToSpeech.LANG_NOT_SUPPORTED
                
                // Configure TTS for more natural speech
                tts?.setSpeechRate(0.8f) // Slightly slower for children
                tts?.setPitch(1.1f) // Slightly higher pitch for friendlier sound
                
                Log.d(TAG, "TTS initialized successfully, isReady: $isTtsReady")
            } else {
                Log.e(TAG, "TTS initialization failed with status: $status")
                isTtsReady = false
            }
        }
    }
    
    /**
     * Play audio from URL or fallback to TTS
     * 
     * @param audioUrl URL of the pre-recorded audio file (can be null)
     * @param fallbackText Text to speak using TTS if audio URL is not available
     * @param audioId Unique identifier for this audio (for tracking)
     */
    fun playAudio(
        audioUrl: String?,
        fallbackText: String,
        audioId: String = fallbackText
    ) {
        // Stop any currently playing audio
        stop()
        
        _currentAudioId.value = audioId
        _isPlaying.value = true
        
        if (!audioUrl.isNullOrEmpty()) {
            playFromUrl(audioUrl, fallbackText, audioId)
        } else {
            playTts(fallbackText)
        }
    }
    
    /**
     * Play letter audio with proper pronunciation
     */
    fun playLetter(
        letter: String,
        exampleWord: String,
        audioUrl: String? = null
    ) {
        val audioId = "letter_$letter"
        val fallbackText = "Huruf $letter. $letter untuk $exampleWord"
        playAudio(audioUrl, fallbackText, audioId)
    }
    
    /**
     * Play number audio with proper pronunciation
     */
    fun playNumber(
        number: Int,
        word: String,
        audioUrl: String? = null
    ) {
        val audioId = "number_$number"
        val fallbackText = "Angka $number. $word"
        playAudio(audioUrl, fallbackText, audioId)
    }
    
    /**
     * Play animal audio with proper pronunciation
     */
    fun playAnimal(
        name: String,
        nameEn: String,
        audioUrl: String? = null
    ) {
        val audioId = "animal_$name"
        val fallbackText = "$name. Dalam bahasa Inggris disebut $nameEn"
        playAudio(audioUrl, fallbackText, audioId)
    }
    
    private fun playFromUrl(
        audioUrl: String,
        fallbackText: String,
        audioId: String
    ) {
        try {
            releaseMediaPlayer()
            
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                
                // Handle different URL formats
                val fullUrl = if (audioUrl.startsWith("http")) {
                    audioUrl
                } else {
                    AUDIO_BASE_URL + audioUrl
                }
                
                Log.d(TAG, "Playing audio from URL: $fullUrl")
                setDataSource(context, Uri.parse(fullUrl))
                
                setOnPreparedListener { mp ->
                    mp.start()
                    Log.d(TAG, "Audio started playing: $audioId")
                }
                
                setOnCompletionListener {
                    _isPlaying.value = false
                    _currentAudioId.value = null
                    Log.d(TAG, "Audio completed: $audioId")
                }
                
                setOnErrorListener { _, what, extra ->
                    Log.e(TAG, "MediaPlayer error: what=$what, extra=$extra, falling back to TTS")
                    // Fallback to TTS on error
                    releaseMediaPlayer()
                    playTts(fallbackText)
                    true
                }
                
                prepareAsync()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error playing audio from URL: ${e.message}, falling back to TTS")
            releaseMediaPlayer()
            playTts(fallbackText)
        }
    }
    
    private fun playTts(text: String) {
        if (isTtsReady && tts != null) {
            Log.d(TAG, "Playing TTS: $text")
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, text.hashCode().toString())
            
            // TTS doesn't have a reliable completion callback, so estimate duration
            // Average speaking rate is about 150 words per minute
            val wordCount = text.split(" ").size
            val estimatedDuration = (wordCount * 400L).coerceIn(1000L, 5000L)
            
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                _isPlaying.value = false
                _currentAudioId.value = null
            }, estimatedDuration)
        } else {
            Log.e(TAG, "TTS not ready, cannot play: $text")
            _isPlaying.value = false
            _currentAudioId.value = null
        }
    }
    
    /**
     * Stop any currently playing audio
     */
    fun stop() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
            }
            tts?.stop()
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping audio: ${e.message}")
        }
        
        _isPlaying.value = false
        _currentAudioId.value = null
    }
    
    private fun releaseMediaPlayer() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.reset()
                it.release()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing MediaPlayer: ${e.message}")
        }
        mediaPlayer = null
    }
    
    /**
     * Release all resources - call when no longer needed
     */
    fun release() {
        releaseMediaPlayer()
        tts?.stop()
        tts?.shutdown()
        tts = null
        isTtsReady = false
    }
}
