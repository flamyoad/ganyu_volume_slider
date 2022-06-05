package com.flamyoad.ganyu_slider.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.media.AudioManager
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.SeekBar
import com.flamyoad.ganyu_slider.databinding.StickyVolumeOverlayViewBinding


class StickyVolumeOverlayService : Service() {

    private lateinit var windowManager: WindowManager

    private var _binding: StickyVolumeOverlayViewBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding is null. Was it destroyed?" }

    private val volumeController: AudioVolumeController by lazy {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        AudioVolumeControllerImpl(audioManager)
    }

    private val viewModel: VolumeSliderViewModel by lazy {
        VolumeSliderViewModel(volumeController)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        _binding = StickyVolumeOverlayViewBinding.inflate(layoutInflater, null, false)
        binding.vm = viewModel

        viewModel.onOverlaySizeChanged = { newWidth, newHeight ->
            val layoutParams = binding.root.layoutParams as WindowManager.LayoutParams
            layoutParams.width = newWidth
            layoutParams.height = newHeight
            windowManager.updateViewLayout(binding.root, layoutParams)
        }

        val overlayPermission = if (Build.VERSION.SDK_INT < 26) {
            WindowManager.LayoutParams.TYPE_PHONE
        } else {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }

        binding.seekbar.max = volumeController.getMaxVolume()
        binding.seekbar.progress = volumeController.getCurrentVolume()
        binding.seekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, newVolume: Int, fromUser: Boolean) {
                if (fromUser) {
                    volumeController.setVolume(newVolume)
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            overlayPermission,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT,
        )

        params.gravity = Gravity.TOP or Gravity.RIGHT
        params.y = 1000

        windowManager.addView(binding.root, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding?.let {
            windowManager.removeView(it.root)
        }
        _binding = null
    }

}