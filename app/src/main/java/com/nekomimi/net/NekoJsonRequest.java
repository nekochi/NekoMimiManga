package com.nekomimi.net;

import android.os.Handler;
import android.os.Message;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;


/**
 * Created by hongchi on 2015-9-1.
 */
public class NekoJsonRequest extends JsonObjectRequest
{

    private static Handler mHandler;
    JSONObject mJsonRequest;

    private static Response.Listener<JSONObject> mListener = new Response.Listener<JSONObject>()
    {
        @Override
        public void onResponse(JSONObject jsonObject) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = jsonObject;
            mHandler.sendMessage(msg);
        }
    };
    private static Response.ErrorListener mErrorListener = new Response.ErrorListener()
    {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };
    public static NekoJsonRequest create(int method, String url,Handler handler,JSONObject jsonRequest)
    {
        return new NekoJsonRequest(method,url,jsonRequest,mListener,mErrorListener,handler);
    }
    private NekoJsonRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener,
                            Response.ErrorListener errorListener,Handler handler)
    {
        super(method, url, jsonRequest, listener, errorListener);
        this.mJsonRequest = jsonRequest;
        this.mHandler = handler;
    }



}