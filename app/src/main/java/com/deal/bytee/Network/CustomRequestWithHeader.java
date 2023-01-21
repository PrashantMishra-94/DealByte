package com.deal.bytee.Network;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CustomRequestWithHeader extends Request<JSONObject> {

    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;
    private Activity activity;

    public CustomRequestWithHeader(String url, Map<String, String> params,
                                   Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener, Activity activity) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
        this.activity = activity;
    }

    public CustomRequestWithHeader(int method, String url, Map<String, String> params,
                                   Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener, Activity activity) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
        this.activity = activity;
    }


    protected Map<String, String> getParams()
            throws AuthFailureError {
        return params;
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }



    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("login", Context.MODE_PRIVATE);

        Map<String, String> headers = new HashMap<>();
        params.put("Accept", "application/json");
        headers.put("Authorization", "Bearer 3AOIlG7OcHQHwZ50SukMKjPtLet1E3NDSYkn7n6n1nUhm1dOo5x3gweKuPTb");
        return headers;
    }


}
