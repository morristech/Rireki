package tr.xip.rireki

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val config: RealmConfiguration = RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded() // TODO: Remove when in prod
                .build()
        Realm.setDefaultConfiguration(config)
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}