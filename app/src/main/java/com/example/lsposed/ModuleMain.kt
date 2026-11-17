package com.example.lsposed

import android.app.Activity
import android.os.Bundle
import android.util.Log
import io.github.libxposed.api.XposedInterface.PRIORITY_DEFAULT
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface.ModuleLoadedParam
import io.github.libxposed.api.XposedModuleInterface.PackageReadyParam

class ModuleMain : XposedModule() {

    companion object {
        private const val TAG = "LsposedTemplate"
    }

    override fun onModuleLoaded(param: ModuleLoadedParam) {
        log(
            Log.INFO,
            TAG,
            "loaded into ${param.processName} ($frameworkName $frameworkVersionCode, API $apiVersion)",
        )
    }

    override fun onPackageReady(param: PackageReadyParam) {
        if (!param.isFirstPackage) return

        val onCreate = Activity::class.java.getDeclaredMethod("onCreate", Bundle::class.java)
        hook(onCreate)
            .setPriority(PRIORITY_DEFAULT)
            .intercept { chain ->
                log(Log.INFO, TAG, "Activity.onCreate in ${param.packageName}")
                chain.proceed()
            }
    }
}
