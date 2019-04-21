package farasense.mobile.api.base

import com.android.volley.VolleyError
import com.google.gson.annotations.Expose

class RestError : VolleyError {

    @Expose
    var statusCode: Int = 0
    @Expose
    var messageString: String? = null
    @Expose
    var title: String? = null

    constructor(title: String, message: String) {
        this.title = title
        this.messageString = message
    }

    constructor(statusCode: Int) {
        this.statusCode = statusCode
    }

    companion object {
        val NO_CONNECTION_CODE = 502
    }
}
