package com.nekomimi.net;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.nekomimi.base.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by hongchi on 2015-9-1.
 */
public class NekoJsonRequest extends Request<JSONObject>
{
    public static final String TAG = "NekoJsonRequest";

    private Response.Listener<JSONObject> mListener;
    Map<String,String> mHeader = new HashMap<String,String>();
    Map<String,String> mParams = new HashMap<String,String>();

    public static NekoJsonRequest create(int method, String url,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener,Map<String,String> params)
    {
        return new NekoJsonRequest(method,url,listener,errorListener,params);
    }
    private NekoJsonRequest(int method, String url,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener,Map<String,String> params)
    {
        super(method, url, errorListener);
        mParams = params;
        mListener = listener;
        
    }

    @Override
    public Map<String,String> getHeaders() throws AuthFailureError
    {
        mHeader.put(AppConfig.USERAGENT,AppConfig.getInstance().getUserAgent());
        mHeader.put(AppConfig.COOKIE,AppConfig.getInstance().getCookie());
        mHeader.put("Connection","keep-alive");

        return mHeader;
    }
    @Override
    public Map<String, String> getParams() throws AuthFailureError
    {
        return mParams;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response)
    {
        try {
            String je = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Map<String,String> responseHeader = response.headers;
            String m = responseHeader.get("Set-Cookie");

            if(!TextUtils.isEmpty(m))
            {
                AppConfig.getInstance().setCookie(m);
                Log.d("COOKIE",m);
                Log.d("Header", responseHeader.toString());
            }
            return Response.success(new JSONObject(je), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException var3) {
            return Response.error(new ParseError(var3));
        } catch (JSONException var4) {
            return Response.error(new ParseError(var4));
        }
    }

    @Override
    protected void deliverResponse(JSONObject jsonObject)
    {
        mListener.onResponse(jsonObject);
    }
}
