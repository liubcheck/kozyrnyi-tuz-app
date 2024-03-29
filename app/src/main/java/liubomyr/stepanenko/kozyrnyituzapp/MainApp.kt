package liubomyr.stepanenko.kozyrnyituzapp

import android.app.Application
import liubomyr.stepanenko.kozyrnyituzapp.utils.SharedPreferencesManager
import liubomyr.stepanenko.kozyrnyituzapp.utils.ToastManager

class MainApp: Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
        ToastManager.init(this)
    }
}