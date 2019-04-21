package farasense.mobile.api.base

import com.android.volley.Response

abstract class SuccessListener<T> : Response.Listener<T> {

    override fun onResponse(response: T) {
        this.onSuccess(response)
    }

    abstract fun onSuccess(response: T)
}
