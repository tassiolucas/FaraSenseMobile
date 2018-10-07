package farasense.mobile.api.base;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GenericRequest<T> extends JsonRequest<T> {

    public static final String AUTHORIZATION_KEY = "Authorization";
    public static final String CONTENT_TYPE_KEY = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/json; charset=utf8";
    public static final int INITIAL_TIMEOUT_MS = 25000;
    public static final String TOKEN_PREFIX = "JWT ";
    public static final String PROTOCOL_CHARSET = "utf-8";
    public static final int REQUEST_MAX_RETRIES = 3;

    private Response.ErrorListener errorListener;
    private Response.Listener<T> successListener;
    private Map<String, String> headers = new HashMap<>();
    private final String requestBody;
    private Class clazz;
    private static final Gson gson = new Gson();

    public GenericRequest(Class clazz, int method, String url, String requestBody,
                          final Response.Listener<T> successListener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, successListener, errorListener);
        this.requestBody = requestBody;
        this.successListener = successListener;
        this.errorListener = errorListener;
        this.clazz = clazz;

        setRetryPolicy(new DefaultRetryPolicy(INITIAL_TIMEOUT_MS,
                REQUEST_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getHeaders() {
        headers.put(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE);
//        if (UserDAO.isLogged())  {
//            headers.put(AUTHORIZATION_KEY, TOKEN_PREFIX + UserDAO.getLoggedToken());
//        }
        return headers;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    protected void deliverResponse(T response) {
        successListener.onResponse(response);
    }

    @Override
    public Response.ErrorListener getErrorListener() {
        return this.errorListener;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, "UTF-8");
            if (clazz.getName().equals(JSONObject.class.getName())) {
                try {
                    JSONObject object = new JSONObject(json);
                    return Response.success(object, HttpHeaderParser.parseCacheHeaders(response));
                } catch (JSONException ex) {
                    return Response.error(new ParseError(ex));
                }
            } else if (clazz.getName().equals(JSONArray.class.getName())) {
                try {
                    JSONArray object = new JSONArray(json);
                    return Response.success(object, HttpHeaderParser.parseCacheHeaders(response));
                } catch (JSONException ex) {
                    return Response.error(new ParseError(ex));
                }
            } else {
                return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
            }
        }catch (UnsupportedEncodingException e) {
            return Response.error(new VolleyError(response));
        }
    }
}
