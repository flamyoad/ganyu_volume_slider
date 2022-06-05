package com.flamyoad.ganyu_slider.services

import android.media.AudioManager
import android.os.Build
import android.util.Log


interface AudioVolumeController {
    fun getCurrentVolume(): Int
    fun getMinVolume(): Int
    fun getMaxVolume(): Int
    fun setVolume(newVolume: Int)
    fun mute()
    fun unmute()
}

class AudioVolumeControllerImpl(private val audioManager: AudioManager): AudioVolumeController {

    override fun getCurrentVolume(): Int {
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }

    override fun getMinVolume(): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return audioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC)
        }
        return 0
    }

    override fun getMaxVolume(): Int {
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    }

    override fun setVolume(newVolume: Int) {
        if (newVolume < getMinVolume() || newVolume > getMaxVolume()) {
            return
        }
        Log.d("AudioControl", "Audio Current: $newVolume, Min: ${getMinVolume()} Max: ${getMaxVolume()}")
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0)
    }

    override fun mute() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
    }

    override fun unmute() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0)
    }
}