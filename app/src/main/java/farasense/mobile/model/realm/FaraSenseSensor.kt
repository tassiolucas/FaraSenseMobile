package farasense.mobile.model.realm

import com.google.gson.annotations.SerializedName
import java.util.Date
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class FaraSenseSensor : RealmObject() {

    @PrimaryKey
    @SerializedName("long_timestamp")
    var timestamp: Long? = null
    @SerializedName("id")
    internal var id: Int? = null
    @SerializedName("amper")
    internal var amper: Double? = null
    @SerializedName("power")
    internal var power: Double? = null
    @SerializedName("timestamp")
    lateinit var date: Date

    fun getId(): Int {
        return id!!
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getAmper(): Double {
        return amper!!
    }

    fun setAmper(amper: Double) {
        this.amper = amper
    }

    fun getPower(): Double {
        return power!!
    }

    fun setPower(power: Double) {
        this.power = power
    }
}
