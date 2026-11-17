package com.example.lsposed

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import io.github.libxposed.service.XposedService
import io.github.libxposed.service.XposedServiceHelper

class App : Application(), XposedServiceHelper.OnServiceListener {

    companion object {
        private val serviceState = mutableStateOf<XposedService?>(null)

        val service: XposedService?
            get() = serviceState.value
    }

    override fun onCreate() {
        super.onCreate()
        XposedServiceHelper.registerListener(this)
    }

    override fun onServiceBind(service: XposedService) {
        serviceState.value = service
    }

    override fun onServiceDied(service: XposedService) {
        serviceState.value = null
    }
}
