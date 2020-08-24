//package old_farasense.model.DAO
//
//import old_farasense.model.DAO.base.BaseDAO
//import old_farasense.model.realm.FaraSenseSensorDaily
//import io.realm.Realm
//import java.util.*
//
//class FaraSenseSensorDailyDAO : BaseDAO() {
//
//    private val FIELD_ID = "id"
//    private val FIELD_TIMESTAMP = "timestamp"
//    private val FIELD_DATE = "date"
//
//    fun saveFromServer(response: List<FaraSenseSensorDaily>?): List<FaraSenseSensorDaily> {
//
//        val savedFromServer: MutableList<FaraSenseSensorDaily> = mutableListOf()
//
//        if (response != null) {
//            Realm.getDefaultInstance().use { realm ->
//
//                realm.executeTransaction{
//
//                    for (faraSenseSensorDaily in response) {
//                        faraSenseSensorDaily.timestamp = faraSenseSensorDaily.date.time
//                        savedFromServer.add(faraSenseSensorDaily)
//                    }
//
//                    it.copyToRealmOrUpdate(savedFromServer)
//                }
//            }
//        }
//
//        return savedFromServer
//    }
//
//    fun getMeasureByIntervals(startIntervalDay: Date, endIntervalDay: Date): List<FaraSenseSensorDaily> {
//        var dayMeasures: MutableIterable<FaraSenseSensorDaily> = mutableListOf()
//
//        Realm.getDefaultInstance().use { realm ->
//            val results = realm.where(FaraSenseSensorDaily::class.java)
//                    .between(FIELD_DATE, startIntervalDay, endIntervalDay)
//                    .findAll()
//                    .sort(FIELD_DATE)
//
//            if (results.size != 0) {
//                dayMeasures = realm.copyFromRealm(results).toMutableList()
//            }
//
//            return dayMeasures.toList()
//        }
//    }
//}
