package com.nekomimi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.nekomimi.R;
import com.nekomimi.base.AppConfig;
import com.nekomimi.base.NImageCache;
import com.nekomimi.util.Util;

/**
 * Created by hongchi on 2015-8-26.
 */
public class InitActivity extends Activity {

    private AppConfig mAppConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        mAppConfig = AppConfig.getInstance();
        InitTask task = new InitTask();
        task.execute((Void)null);
    }

    private void Resolution()
    {
        if(!mAppConfig.get(mAppConfig.RESOLUTION).equals(mAppConfig.NOT_EXIST))
        {
            return;
        }
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float density = displayMetrics.density;
        float width = displayMetrics.widthPixels;
        float height = displayMetrics.heightPixels;
        String str = Util.toResolution(width,height,density);
        mAppConfig.set(mAppConfig.RESOLUTION,str);
    }

    public class InitTask extends AsyncTask<Void, Void, Boolean>
    {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try{
                Thread.sleep(3000);
            }
            catch (InterruptedException e)
            {
                return false;
            }
            //在这里做初始化工作
            Resolution();
            NImageCache.getInstance();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success )
        {
            String str = mAppConfig.get(mAppConfig.RESOLUTION);
            float width = Util.getResolutionFromStr(str, "width");
            float height = Util.getResolutionFromStr(str , "height");
            float density = Util.getResolutionFromStr(str , "density");
            Log.d("TAG","width:"+width);
            Log.d("TAG","height:"+height);
            Log.d("TAG","density:"+density);
            AppConfig.mScanWidth = (int)width;
            AppConfig.mScanHeight = (int)height;
            Intent intent = new Intent(InitActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
