package com.flamyoad.ganyu_slider

import androidx.lifecycle.ViewModel


class MainActivityViewModel: ViewModel() {

    var startService: (() -> Unit) = {}
    var stopService: (() -> Unit) = {}
}