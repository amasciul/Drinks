package fr.masciulli.drinks

import android.app.Application
import android.content.Context
import com.squareup.leakcanary.LeakCanary
import fr.masciulli.drinks.core.drinks.DrinksRepository
import fr.masciulli.drinks.core.drinks.DrinksSource
import fr.masciulli.drinks.core.liquors.LiquorsRepository
import fr.masciulli.drinks.core.liquors.LiquorsSource
import fr.masciulli.drinks.core.net.Client
import timber.log.Timber

class DrinksApplication : Application() {
    lateinit var drinksSource: DrinksSource
        private set
    lateinit var liquorsSource: LiquorsSource
        private set

    override fun onCreate() {
        super.onCreate()
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this)
        }

        Timber.plant(Timber.DebugTree())

        //TODO Daggerify
        val client = Client(BuildConfig.SERVER_URL)
        drinksSource = DrinksRepository(client)
        liquorsSource = LiquorsRepository(client)
    }

    companion object {
        @JvmStatic
        fun get(context: Context): DrinksApplication {
            return context.applicationContext as DrinksApplication
        }
    }
}
