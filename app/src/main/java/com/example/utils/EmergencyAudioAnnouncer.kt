package com.example.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.example.domain.model.AppLanguage
import java.util.Locale

class EmergencyAudioAnnouncer(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private val mainHandler = Handler(Looper.getMainLooper())
    private var currentLocale: Locale = Locale.ENGLISH

    private var onDoneCallback: (() -> Unit)? = null
    private var onStartCallback: (() -> Unit)? = null

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (_: Exception) {
            tts = null
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.let { engine ->
                applyLocale(currentLocale)
                engine.setSpeechRate(0.95f)
                engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        mainHandler.post { onStartCallback?.invoke() }
                    }

                    override fun onDone(utteranceId: String?) {
                        mainHandler.post { onDoneCallback?.invoke() }
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        mainHandler.post { onDoneCallback?.invoke() }
                    }

                    override fun onError(utteranceId: String?, errorCode: Int) {
                        mainHandler.post { onDoneCallback?.invoke() }
                    }
                })
                isInitialized = true
            }
        }
    }

    fun setLanguage(language: AppLanguage) {
        currentLocale = language.locale
        if (isInitialized) {
            applyLocale(currentLocale)
        }
    }

    private fun applyLocale(locale: Locale) {
        tts?.let { engine ->
            val result = engine.isLanguageAvailable(locale)
            if (result >= TextToSpeech.LANG_AVAILABLE) {
                engine.language = locale
            } else {
                engine.language = Locale.ENGLISH
            }
        }
    }

    fun speak(text: String, language: AppLanguage? = null, onStart: () -> Unit = {}, onDone: () -> Unit = {}) {
        if (tts == null || !isInitialized) {
            onDone()
            return
        }

        language?.let { applyLocale(it.locale) }

        onStartCallback = onStart
        onDoneCallback = onDone

        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ALERT_TTS_${System.currentTimeMillis()}")
    }

    fun stop() {
        try {
            tts?.stop()
            onDoneCallback?.invoke()
        } catch (_: Exception) {
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {
        }
    }
}

