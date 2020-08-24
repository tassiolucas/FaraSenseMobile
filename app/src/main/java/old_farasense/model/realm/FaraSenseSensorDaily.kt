//package old_farasense.model.realm
//
//import com.google.gson.annotations.SerializedName
//import java.util.Date
//import io.realm.RealmObject
//import io.realm.annotations.PrimaryKey
//
//open class FaraSenseSensorDaily : RealmObject() {
//
//    @PrimaryKey
//    @SerializedName("long_timestamp")
//    var timestamp: Long? = null
//    @SerializedName("id")
//    var id: Int? = null
//    @SerializedName("timestamp")
//    lateinit var date: Date
//    @SerializedName("total_kwh")
//    var totalKwh: Double? = null
//    @SerializedName("total_power")
//    var totalPower: Double? = null
//}
