//package old_farasense.model.DAO
//
//import old_farasense.model.DAO.base.BaseDAO
//import old_farasense.model.realm.FaraSenseSensor
//import io.realm.Realm
//import java.util.*
//
//class FaraSenseSensorDAO : BaseDAO() {
//
//    private val FIELD_ID = "id"
//    private val FIELD_TIMESTAMP = "timestamp"
//    private val FIELD_DATE = "date"
//
//    fun saveFromServer(response: List<FaraSenseSensor>?): List<FaraSenseSensor> {
//
//        val savedFromServer: MutableList<FaraSenseSensor> = mutableListOf()
//
//        Realm.getDefaultInstance().use { realm ->
//
//            realm.executeTransaction{
//
//                if (response != null) {
//                    for (faraSenseSensor in response) {
//                        faraSenseSensor.timestamp = faraSenseSensor.date.time
//                        savedFromServer.add(faraSenseSensor)
//                    }
//
//                    it.copyToRealmOrUpdate(savedFromServer)
//                }
//            }
//        }
//
//        return savedFromServer.toList()
//    }
//
//    fun getMeasureByIntervals(startIntervalHour: Date, endIntervalHour: Date): List<FaraSenseSensor> {
//
//        var hourMeasures: MutableIterable<FaraSenseSensor> = mutableListOf()
//
//        Realm.getDefaultInstance().use { realm ->
//
//                val results = realm
//                        .where(FaraSenseSensor::class.java)
//                        .between(FIELD_DATE, startIntervalHour, endIntervalHour)
//                        .findAll()
//                        .sort(FIELD_DATE)
//
//                if (results?.size != 0) {
//                    hourMeasures = realm.copyFromRealm(results)
//                }
//        }
//
//        return hourMeasures.toMutableList()
//    }
//}
