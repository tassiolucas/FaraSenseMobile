package farasense.mobile.util

import android.content.Context
import android.content.SharedPreferences
import java.util.Date
import farasense.mobile.App

class Preferences {

    private var sharedPreferences: SharedPreferences? = null

    val maturityDate: Long?
        get() = sharedPreferences!!.getLong(MATURITY_DATE, 0)

    var rateKwh: Float?
        get() = sharedPreferences!!.getFloat(RATE_KWH, 0f)
        set(rateKwh) {
            val editor = sharedPreferences!!.edit()
            editor.putFloat(RATE_KWH, rateKwh!!)
            editor.apply()
        }

    var rateFlag: Float?
        get() = sharedPreferences!!.getFloat(RATE_FLAG, 0f)
        set(rateFlag) {
            val editor = sharedPreferences!!.edit()
            editor.putFloat(RATE_FLAG, rateFlag!!)
            editor.apply()
        }

    fun clear() {
        val editor = sharedPreferences!!.edit()
        editor.clear()
        editor.apply()
    }

    fun setMaturityDate(maturutyDate: Date) {
        val editor = sharedPreferences!!.edit()
        editor.putLong(MATURITY_DATE, maturutyDate.time)
        editor.apply()
    }

    companion object {
        private val SHARED_PREFERENCES = "shared_preferences"
        private val MATURITY_DATE = "maturity_date"
        private val RATE_KWH = "rate_kwh"
        private val RATE_FLAG = "rate_flag"

        private var instance: Preferences? = null

        fun getInstance(): Preferences {
            if (instance == null) {
                instance = Preferences()
                val app = App.getInstance()
                instance!!.sharedPreferences = app.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
            }
            return instance as Preferences
        }
    }
}
