//package old_farasense.model.DAO.base
//
//import android.content.Context
//import android.os.Environment
//import android.widget.Toast
//import io.realm.Realm
//import io.realm.RealmObject
//import io.realm.internal.IOException
//import java.io.File
//
//open class BaseDAO {
//
//    fun save(realmObject: RealmObject): RealmObject {
//        var realm: Realm? = null
//
//        try {
//            realm = Realm.getDefaultInstance()
//            realm!!.executeTransaction { r -> r.copyToRealmOrUpdate(realmObject) }
//        } finally {
//            realm?.close()
//        }
//        return realmObject
//    }
//
//    fun <T : RealmObject> deleteAll(clazz: Class<T>): Boolean {
//        var realm: Realm? = null
//        val deleted = BooleanArray(1)
//        try {
//            realm = Realm.getDefaultInstance()
//            realm!!.executeTransaction { r ->
//                r.delete(clazz)
//                deleted[0] = true
//            }
//        } catch (e: Exception) {
//            deleted[0] = false
//        } finally {
//            realm?.close()
//        }
//        return deleted[0]
//    }
//
//    fun saveDataBase(context: Context) {
//
//        try {
//
//            val file = File(Environment.getExternalStorageDirectory().path + "/faraSenseDB.realm")
//
//            if (file.exists()) {
//                file.delete()
//            }
//
//            Realm.getDefaultInstance().writeCopyTo(file) // TODO: Não salvando em meu celular Moto G3
//
//            Toast.makeText(context, "Backup feito com sucesso!", Toast.LENGTH_SHORT).show()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//}
