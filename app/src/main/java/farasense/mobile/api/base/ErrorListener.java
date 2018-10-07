package farasense.mobile.api.base;

import android.content.Context;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import farasense.mobile.App;
import farasense.mobile.R;

public abstract class ErrorListener implements Response.ErrorListener {

    private Context context;

    public ErrorListener() {
        this.context = App.getInstance();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        RestError restError;

        try {
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                restError = new RestError(
                        context.getString(R.string.error_network_timeout_title),
                        context.getString(R.string.error_network_timeout_message));
            } else if (error instanceof ServerError) {
                String data = new String(error.networkResponse.data);
                restError = new Gson().fromJson(data, RestError.class);
            } else if (error instanceof NetworkError) {
                restError = new RestError(
                        context.getString(R.string.error_network_title),
                        context.getString(R.string.error_network_message));
            } else {
                throw error;
            }
        } catch (Exception e) {
            restError = new RestError(
                    context.getString(R.string.error_unknown_error_title),
                    context.getString(R.string.error_unknown_error_message));
        }

        this.onError(restError);
    }

    public abstract void onError(RestError restError);
}
