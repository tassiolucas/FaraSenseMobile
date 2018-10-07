package farasense.mobile.api.base;

import com.android.volley.VolleyError;
import com.google.gson.annotations.Expose;

public class RestError extends VolleyError {

    public static final int NO_CONNECTION_CODE = 502;

    @Expose
    private int statusCode;
    @Expose
    private String message;
    @Expose
    private String title;

    public RestError(final String title, final String message) {
        this.title = title;
        this.message = message;
    }

    public RestError(final int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}
