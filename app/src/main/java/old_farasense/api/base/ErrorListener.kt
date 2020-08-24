//package old_farasense.api.base
//
//import com.android.volley.NetworkError
//import com.android.volley.NoConnectionError
//import com.android.volley.Response
//import com.android.volley.ServerError
//import com.android.volley.TimeoutError
//import com.android.volley.VolleyError
//import com.google.gson.Gson
//import farasense.mobile.R
//
//abstract class ErrorListener : Response.ErrorListener {
//
//    override fun onErrorResponse(error: VolleyError) {
//        var restError: RestError
//
//        try {
//            if (error is TimeoutError || error is NoConnectionError) {
//                restError = RestError(
//                        context.getString(R.string.error_network_timeout_title),
//                        context.getString(R.string.error_network_timeout_message))
//            } else if (error is ServerError) {
//                val data = String(error.networkResponse.data)
//                restError = Gson().fromJson(data, RestError::class.java)
//            } else if (error is NetworkError) {
//                restError = RestError(
//                        context.getString(R.string.error_network_title),
//                        context.getString(R.string.error_network_message))
//            } else {
//                throw error
//            }
//        } catch (e: Exception) {
//            restError = RestError(
//                    context.getString(R.string.error_unknown_error_title),
//                    context.getString(R.string.error_unknown_error_message))
//        }
//
//        this.onError(restError)
//    }
//
//    abstract fun onError(restError: RestError)
//}
