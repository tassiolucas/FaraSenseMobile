package farasense.mobile

import android.app.Application
import farasense.mobile.aws.AWSProvider
import io.realm.Realm
import io.realm.RealmConfiguration

class App : Application() {

    private var downloadingServices: Boolean = false

    override fun onCreate() {

        super.onCreate()

        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
                .name("faraSenseMobileDataBase")
                .deleteRealmIfMigrationNeeded()
                .build()

        Realm.setDefaultConfiguration(realmConfig)

        AWSProvider.initialize(applicationContext)

        downloadingServices = false
    }
}
