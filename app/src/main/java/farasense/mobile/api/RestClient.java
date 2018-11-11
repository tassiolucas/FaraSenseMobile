package farasense.mobile.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.GsonBuilder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import farasense.mobile.App;
import farasense.mobile.api.base.ErrorListener;
import farasense.mobile.api.base.GenericRequest;
import farasense.mobile.api.base.RestError;
import farasense.mobile.api.base.SuccessListener;
import farasense.mobile.model.realm.FaraSenseSensor;
import farasense.mobile.model.realm.FaraSenseSensorDaily;
import farasense.mobile.model.realm.FaraSenseSensorHours;
import farasense.mobile.util.ConnectionUtil;

import static farasense.mobile.api.base.RestError.NO_CONNECTION_CODE;

public class RestClient {

    private static final int SENSOR_ID = 1;

    private static RestClient instance;
    private Context context;
    private RequestQueue queue;
    private String url;

    private RestClient() {
        this.context = App.getInstance();
    }

    public static final RestClient getInstance() {
        if (instance == null) {
            instance = new RestClient();
            Log.d("NEW RESTCLIENT", "Criando nova instancia do cliente Voley de APIs...");
        }
        return instance;
    }

    public <T> void  request(final Class clazz,
                             final int method,
                             final String resource,
                             final Object requestBody,
                             final Response.Listener<T> successListener,
                             final Response.ErrorListener errorListener) {
        try {
            url = "https://p4b2zvd5pi.execute-api.us-east-1.amazonaws.com/dev/current_sensor/";
        } catch (Exception e) {}

        if (ConnectionUtil.isDataConnectionAvailable(context)) {
            request(url, clazz, method, resource, requestBody, successListener, errorListener);
        } else {
            errorListener.onErrorResponse(new RestError(NO_CONNECTION_CODE));
        }
    }

    public <T> void request(final String url,
                            final Class clazz,
                            final int method,
                            final String resource,
                            final Object requestBody,
                            final Response.Listener<T> successListener,
                            final Response.ErrorListener errorListener) {

        if (queue == null) {
            queue = Volley.newRequestQueue(context);
            Log.d("NEW QUEUE", "Configurando uma nova fila de solicitação...");
        }

        final String finalUrl = resource != null && !resource.isEmpty() ? url + resource : url;

        String requestBodyString = new GsonBuilder()
                .disableHtmlEscaping()
                .create()
                .toJson(requestBody);

        GenericRequest request = new GenericRequest<>(clazz, method, finalUrl,
                requestBodyString, successListener, errorListener);

        queue.add(request);
    }

    public void getFaraSenseSensor(final SuccessListener<List<FaraSenseSensor>> successListener,
                                   final ErrorListener errorListener, Date start, Date end) {
        String resource = SENSOR_ID + "/" + start.getTime() + "/" + end.getTime();
        request(FaraSenseSensor[].class, Request.Method.GET, resource, null, new Response.Listener<FaraSenseSensor[]>() {
            @Override
            public void onResponse(FaraSenseSensor[] response) {
                successListener.onSuccess(Arrays.asList(response));
            }
        }, errorListener);
    }

    public void getFaraSenseSensorHours(final SuccessListener<List<FaraSenseSensorHours>> successListener,
                                        final ErrorListener errorListener, Date start, Date end) {
        String resource = "get_hour/" + SENSOR_ID + "/" + start.getTime() + "/" + end.getTime();

        request(FaraSenseSensorHours[].class, Request.Method.GET, resource, null, new Response.Listener<FaraSenseSensorHours[]>() {
            @Override
            public void onResponse(FaraSenseSensorHours[] response) {
                successListener.onSuccess(Arrays.asList(response));
            }
        }, errorListener);
    }

    public void getFaraSenseSensorDaily(final SuccessListener<List<FaraSenseSensorDaily>> successListener,
                                        final ErrorListener errorListener, Date start, Date end) {
        String resource = "get_day/" + SENSOR_ID + "/" + start.getTime() + "/" + end.getTime();

        request(FaraSenseSensorDaily[].class, Request.Method.GET, resource, null, new Response.Listener<FaraSenseSensorDaily[]>() {
            @Override
            public void onResponse(FaraSenseSensorDaily[] response) {
                successListener.onSuccess(Arrays.asList(response));
            }
        }, errorListener);
    }
}
