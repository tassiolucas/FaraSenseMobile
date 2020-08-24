package old_farasense.api

import android.content.Context
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import old_farasense.api.base.GenericRequest
import old_farasense.api.base.RestError
import old_farasense.util.ConnectionUtil

object RestClient {

    private val SENSOR_ID = 1

    private var queue: RequestQueue? = null

    fun <T> request(context: Context,
                    clazz: Class<*>,
                    method: Int,
                    resource: String,
                    requestBody: Any?,
                    successListener: Response.Listener<T>,
                    errorListener: Response.ErrorListener) {

        val url = "https://p4b2zvd5pi.execute-api.us-east-1.amazonaws.com/dev/current_sensor/"

        if (ConnectionUtil.isDataConnectionAvailable(context)) {
            request(context, url, clazz, method, resource, requestBody, successListener, errorListener)
        } else {
            errorListener.onErrorResponse(RestError(RestError.NO_CONNECTION_CODE))
        }
    }

    fun <T> request(context: Context,
                    url: String,
                    clazz: Class<*>,
                    method: Int,
                    resource: String?,
                    requestBody: Any?,
                    successListener: Response.Listener<T>,
                    errorListener: Response.ErrorListener) {

        if (queue == null) {
            queue = Volley.newRequestQueue(context)
            Log.d("NEW QUEUE", "Configurando uma nova fila de solicitação...")
        }

        val finalUrl = if (resource != null && !resource.isEmpty()) url + resource else url

        val requestBodyString = GsonBuilder()
                .disableHtmlEscaping()
                .create()
                .toJson(requestBody)

        val request = GenericRequest(clazz, method, finalUrl,
                requestBodyString, successListener, errorListener)

        queue?.add(request)
    }

//    fun getFaraSenseSensor(context: Context, successListener: SuccessListener<List<FaraSenseSensor>>,
//                           errorListener: ErrorListener, start: Date, end: Date) {
//        val resource = SENSOR_ID.toString() + "/" + start.time + "/" + end.time
//        request(context, Array<FaraSenseSensor>::class.java, Request.Method.GET, resource, null, Response.Listener<Array<FaraSenseSensor>> { response -> successListener.onSuccess(Arrays.asList(*response)) }, errorListener)
//    }
//
//    fun getFaraSenseSensorHours(context: Context, successListener: SuccessListener<List<FaraSenseSensorHours>>,
//                                errorListener: ErrorListener, start: Date, end: Date) {
//        val resource = "get_hour/" + SENSOR_ID + "/" + start.time + "/" + end.time
//
//        request(context, Array<FaraSenseSensorHours>::class.java, Request.Method.GET, resource, null, Response.Listener<Array<FaraSenseSensorHours>> { response -> successListener.onSuccess(Arrays.asList(*response)) }, errorListener)
//    }
//
//    fun getFaraSenseSensorDaily(context: Context, successListener: SuccessListener<List<FaraSenseSensorDaily>>,
//                                errorListener: ErrorListener, start: Date, end: Date) {
//        val resource = "get_day/" + SENSOR_ID + "/" + start.time + "/" + end.time
//
//        request(context, Array<FaraSenseSensorDaily>::class.java, Request.Method.GET, resource, null, Response.Listener<Array<FaraSenseSensorDaily>> { response -> successListener.onSuccess(Arrays.asList(*response)) }, errorListener)
//    }
}
