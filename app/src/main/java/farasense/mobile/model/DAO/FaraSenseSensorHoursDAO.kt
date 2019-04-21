package farasense.mobile.model.DAO

import farasense.mobile.model.DAO.base.BaseDAO
import farasense.mobile.model.realm.FaraSenseSensorHours
import farasense.mobile.util.DateUtil
import io.realm.Realm

class FaraSenseSensorHoursDAO : BaseDAO() {

    private val FIELD_ID = "id"
    private val FIELD_TIMESTAMP = "timestamp"
    private val FIELD_DATE = "date"

    fun saveFromServer(response: List<FaraSenseSensorHours>?): List<FaraSenseSensorHours> {
        val savedFromServer: MutableList<FaraSenseSensorHours> = mutableListOf()

        Realm.getDefaultInstance().use { realm ->

            realm.executeTransaction {
                if (response != null) {
                    for (faraSenseSensorHours in response) {
                        faraSenseSensorHours.timestamp = faraSenseSensorHours.date.time
                        savedFromServer.plus(faraSenseSensorHours)
                    }

                    it.copyToRealmOrUpdate(savedFromServer)
                }
            }
        }

        return savedFromServer.toList()
    }

    val dailyComsumption: List<FaraSenseSensorHours>
        get() {

            var dailyComsumption: MutableIterable<FaraSenseSensorHours>

            dailyComsumption = mutableListOf()

            Realm.getDefaultInstance().use { realm ->
                val now = DateUtil.now
                val firtsMomentInTheDay = DateUtil.firstMomentOfTheDay

                val results = realm.where(FaraSenseSensorHours::class.java)
                        .between(FIELD_DATE, firtsMomentInTheDay, now)
                        .findAll()

                if (results.size != 0) {
                    dailyComsumption = realm.copyFromRealm(results)
                }

                return dailyComsumption.toList()
            }
        }
}
