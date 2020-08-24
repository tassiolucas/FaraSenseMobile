package farasense.mobile.di

import com.amazonaws.mobile.auth.core.IdentityManager
import com.amazonaws.mobile.auth.userpools.CognitoUserPoolsSignInProvider
import com.amazonaws.mobile.config.AWSConfiguration
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val awsProviderModule = module {

    single {
        val awsConfiguration = AWSConfiguration(androidApplication())
        val identityManager = IdentityManager(androidContext(), awsConfiguration)

        IdentityManager.setDefaultIdentityManager(identityManager)

        identityManager.addSignInProvider(CognitoUserPoolsSignInProvider::class.java)
    }

    single {
        get<IdentityManager>()
    }
}