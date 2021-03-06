package com.nekomimi.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nekomimi.R;
import com.nekomimi.adapter_listener.MyFragmentPagerAdapter;
import com.nekomimi.base.AppConfig;
import com.nekomimi.bean.MangaChapterInfo;
import com.nekomimi.bean.MangaInfo;
import com.nekomimi.fragment.MangaInfoFragment;
import com.nekomimi.fragment.TestFragment;
import com.nekomimi.util.JsonUtil;
import com.nekomimi.util.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by hongchi on 2015-9-10.
 */
public class MangaInfoActivity extends BaseActivity {

    public static final String TAG = "MangaInfoActivity";

    private View mProgressbar;
    private Toolbar mToolbar;
    private MangaInfo mMangaInfo;
    private ImageView mCoverIv;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> mFragments;
    private List<MangaChapterInfo> mMangaChapterList;
    private Handler mUiHandler = new UiHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mangainfo);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        Intent intent = getIntent();
        mMangaInfo = (MangaInfo)intent.getParcelableExtra("mangainfo");
        mProgressbar = findViewById(R.id.mangainfo_progress);
        mCoverIv = (ImageView)findViewById(R.id.iv_mangacover);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            ViewCompat.setTransitionName(mCoverIv, "shareImg");
            Picasso.with(mCoverIv.getContext()).load(mMangaInfo.getCoverImg()).noFade().into(mCoverIv);
        }
        else
        {
            mCoverIv.setImageBitmap(mMangaInfo.getCoverImgBm());
        }
        mTabLayout = (TabLayout)findViewById(R.id.tl_mangainfo);
        mViewPager = (ViewPager)findViewById(R.id.vp_mangainfo);

        mToolbar.setTitle(mMangaInfo.getName());
        setSupportActionBar(mToolbar);





        final MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mFragments = new ArrayList<>();
        mFragments.add(TestFragment.newInstance("#026719"));
        mFragments.add(TestFragment.newInstance("#acf233"));
        adapter.addFrag(mFragments.get(0), "info");
        adapter.addFrag(mFragments.get(1), "comment");
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, AppConfig.mScanHeight - 96));

        connect();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what)
        {
            case 1:
                mMangaChapterList = JsonUtil.praseMangaChapter((JSONObject) msg.obj);
                if(mMangaChapterList.isEmpty())
                    return;
                mFragments.add(0, MangaInfoFragment.create(mMangaChapterList, mMangaInfo.getName()));
                ((MyFragmentPagerAdapter)mViewPager.getAdapter()).changeFrag(mFragments.get(0), 0);
                mViewPager.getAdapter().notifyDataSetChanged();
                Util.showProgress(false, mViewPager, mProgressbar);
                break;
            case 999999:
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void connect()
    {
        Map<String,String> request = new HashMap<>();
        request.put("key", "e00b1e6d896c4f57ae552ab257186680");
        request.put("comicName",mMangaInfo.getName());
       // VolleyConnect.getInstance().connect( NekoJsonRequest.create(Util.makeHtml(AppConfig.MANGAINFO_URL,request,"UTF-8"),mHandler));
    }

    public static void launch(AppCompatActivity activity, View transitionView,Intent intent) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, transitionView, "shareImg");
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }
}
