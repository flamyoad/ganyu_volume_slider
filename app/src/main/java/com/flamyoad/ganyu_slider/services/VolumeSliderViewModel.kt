package com.flamyoad.ganyu_slider.services

import android.view.View
import android.view.WindowManager
import androidx.databinding.ObservableInt


class VolumeSliderViewModel(
    private val audioVolumeController: AudioVolumeController
) {

    lateinit var onOverlaySizeChanged: (width: Int, height: Int) -> Unit

    val overlayVisibility = ObservableInt(View.GONE)

    fun toggleOverlay() {
        val isOverlayVisible = overlayVisibility.get() == View.VISIBLE
        if (isOverlayVisible) {
            overlayVisibility.set(View.GONE)
            onOverlaySizeChanged(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        } else {
            overlayVisibility.set(View.VISIBLE)
            onOverlaySizeChanged(500, 500)
        }
    }
}