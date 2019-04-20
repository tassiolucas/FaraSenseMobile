package farasense.mobile.model.realm

import com.google.gson.annotations.SerializedName
import java.util.Date
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class FaraSenseSensorHours : RealmObject() {

    @PrimaryKey
    @SerializedName("long_timestamp")
    var timestamp: Long? = null
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("timestamp")
    lateinit var date: Date
    @SerializedName("kwh")
    var kwh: Double? = null
    @SerializedName("media_power")
    var mediaPower: Double? = null
    @SerializedName("time")
    var time: Double? = null
}
