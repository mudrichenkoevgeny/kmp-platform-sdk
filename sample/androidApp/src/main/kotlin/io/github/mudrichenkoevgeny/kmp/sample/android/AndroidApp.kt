package io.github.mudrichenkoevgeny.kmp.sample.android

import android.app.Application
import io.github.mudrichenkoevgeny.kmp.sample.app.di.AppComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AndroidApp : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    val appComponent by lazy { AppComponent(this) }

    override fun onCreate() {
        super.onCreate()

        applicationScope.launch {
            appComponent.init()
        }
    }
}