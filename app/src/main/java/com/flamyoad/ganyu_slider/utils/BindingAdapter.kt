package com.flamyoad.ganyu_slider.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


object BindingAdapter {

    @JvmStatic
    @BindingAdapter("setImage")
    fun setImage(imageView: ImageView, @DrawableRes src: Int) {
        Glide.with(imageView.context)
            .load(src)
            .into(imageView)
    }
}