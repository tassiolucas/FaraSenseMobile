package old_farasense.api.base

import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonRequest
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.util.HashMap

class GenericRequest<T>(private val clazz: Class<*>,
                        method: Int,
                        url: String,
                        requestBody: String,
                        private val successListener: Response.Listener<T>,
                        private val errorListener: Response.ErrorListener) : JsonRequest<T>(method, url, requestBody, successListener, errorListener) {
    private val headers = HashMap<String, String>()

    init {

        retryPolicy = DefaultRetryPolicy(INITIAL_TIMEOUT_MS,
                REQUEST_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
    }

    override fun getHeaders(): Map<String, String> {
        headers[CONTENT_TYPE_KEY] = CONTENT_TYPE_VALUE
        //        if (UserDAO.isLogged())  {
        //            headers.put(AUTHORIZATION_KEY, TOKEN_PREFIX + UserDAO.getLoggedToken());
        //        }
        return headers
    }

    fun addHeader(key: String, value: String) {
        headers[key] = value
    }

    override fun getBodyContentType(): String {
        return "application/json"
    }

    override fun deliverResponse(response: T) {
        successListener.onResponse(response)
    }

    override fun getErrorListener(): Response.ErrorListener? {
        return this.errorListener
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<T>? {
        try {
            val json = String(response.data)
            return if (clazz.name == JSONObject::class.java.name) {
                try {
                    val `object` = JSONObject(json)
                    Response.success(`object`, HttpHeaderParser.parseCacheHeaders(response))
                } catch (ex: JSONException) {
                    Response.error<Any>(ParseError(ex))
                } as Response<T>?

            } else if (clazz.name == JSONArray::class.java.name) {
                try {
                    val `object` = JSONArray(json)
                    Response.success(`object`, HttpHeaderParser.parseCacheHeaders(response))
                } catch (ex: JSONException) {
                    Response.error<Any>(ParseError(ex))
                } as Response<T>?

            } else {
                Response.success(gson.fromJson<Any>(json, clazz), HttpHeaderParser.parseCacheHeaders(response)) as Response<T>
            }
        } catch (e: UnsupportedEncodingException) {
            return Response.error<Any>(VolleyError(response)) as Response<T>
        }
    }

    companion object {
        const val AUTHORIZATION_KEY = "Authorization"
        const val CONTENT_TYPE_KEY = "Content-Type"
        const val CONTENT_TYPE_VALUE = "application/json; charset=utf8"
        const val INITIAL_TIMEOUT_MS = 25000
        const val TOKEN_PREFIX = "JWT "
        const val PROTOCOL_CHARSET = "utf-8"
        const val REQUEST_MAX_RETRIES = 3
        private val gson = Gson()
    }
}
