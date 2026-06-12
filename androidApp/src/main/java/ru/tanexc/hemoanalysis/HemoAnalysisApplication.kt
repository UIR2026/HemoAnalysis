package ru.tanexc.hemoanalysis

import android.app.Application
import ru.tanexc.hemoanalysis.di.koinInit
import org.koin.android.ext.koin.androidContext

class HemoAnalysisApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        koinInit {
            androidContext(this@HemoAnalysisApplication)
        }
    }
}