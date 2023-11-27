package dong.project.chart.utility

import android.app.Application
import android.content.Context

class ContextProvider : Application() {
    companion object {
        lateinit var appContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}

object AppContext{
    val context: Context
        get() = ContextProvider.appContext
}