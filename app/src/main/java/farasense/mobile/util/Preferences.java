package farasense.mobile.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import farasense.mobile.App;

public class Preferences {

    private static final String SHARED_PREFERENCES = "shared_preferences";
    private static final String MATURITY_DATE = "maturity_date";
    private static final String RATE_KWH = "rate_kwh";
    private static final String RATE_FLAG = "rate_flag";

    private static Preferences instance;
    private SharedPreferences sharedPreferences;

    public static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
            App app = App.getInstance();
            instance.sharedPreferences = app.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        }
        return instance;
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void setMaturityDate(Date maturutyDate) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(MATURITY_DATE, maturutyDate.getTime());
        editor.apply();
    }

    public Long getMaturityDate() {
        return sharedPreferences.getLong(MATURITY_DATE, 0);
    }

    public void setRateKwh(Float rateKwh) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(RATE_KWH, rateKwh);
        editor.apply();
    }

    public Float getRateKwh() {
        return sharedPreferences.getFloat(RATE_KWH, 0F);
    }

    public void setRateFlag(Float rateFlag) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(RATE_FLAG, rateFlag);
        editor.apply();
    }

    public Float getRateFlag() {
        return sharedPreferences.getFloat(RATE_FLAG, 0F);
    }
}
