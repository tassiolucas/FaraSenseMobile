package old_farasense.aws

import android.content.Context
import com.amazonaws.mobile.auth.core.IdentityManager
import com.amazonaws.mobile.auth.userpools.CognitoUserPoolsSignInProvider
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager

class AWSProvider private constructor(val context: Context) {

    companion object {

        private lateinit var context: Context
        private lateinit var configuration: AWSConfiguration
        private lateinit var pinpointManager: PinpointManager
        lateinit var instance: AWSProvider
            private set

        fun initialize(context: Context) {
            this.context = context
            this.instance = AWSProvider(context)
            this.configuration = AWSConfiguration(context)

            val identityManager = IdentityManager(context, configuration)

            IdentityManager.setDefaultIdentityManager(identityManager)
            identityManager.addSignInProvider(CognitoUserPoolsSignInProvider::class.java)
        }

        val identityManager: IdentityManager
            get() = IdentityManager.getDefaultIdentityManager()

        fun getPinpointManager(): PinpointManager {
            val cp = identityManager.credentialsProvider
            val config = PinpointConfiguration(
                    context, cp, configuration)

            pinpointManager = PinpointManager(config)

            return pinpointManager as PinpointManager
        }
    }
}