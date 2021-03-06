package com.nekomimi.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nekomimi.R;
import com.nekomimi.adapter_listener.MyFragmentPagerAdapter;
import com.nekomimi.base.AppConfig;
import com.nekomimi.fragment.ApiPraFragment;
import com.nekomimi.fragment.HomeFragment;
import com.nekomimi.fragment.TestFragment;
import com.nekomimi.service.NetService;

import java.util.ArrayList;


public class HomeActivity extends BaseActivity implements View.OnClickListener
{

	private static final String TAG = "HomeActivity";

	private ViewPager mViewPager = null;
	private ArrayList<Fragment> mFragmentList;
	private ActionBarDrawerToggle mActionBarDrawerToggle;
	private Toolbar toolbar;
	private DrawerLayout mDrawerLayout;
	private TabLayout mTabLayout;
	private Button mTestBt;
	private ImageView mFaceIv;
	private Button mSettingBt;
	private Button mQuitBt;

	private boolean mFlag = false;
	private boolean mIfQuit = false;
	private int mFloatBtMargin ;

	private UiHandler mUiHandler = new UiHandler(this);
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		toolbar = (Toolbar)findViewById(R.id.toolbar);
//		toolbar.setTitleTextColor(Color.WHITE);
		mFloatBtMargin = AppConfig.mScanHeight / 32;
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		initView();
		initViewpager();






//		hideFloatBt();
	}

	@Override
	public void handleMessage(Message msg) {

	}

	private void initView()
	{
		mTabLayout = (TabLayout)findViewById(R.id.toptab);

		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
		mActionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.open,R.string.close)
		{
			@Override
			public void onDrawerClosed(View drawerView)
			{
				super.onDrawerClosed(drawerView);
				mFlag = false;
			}
			@Override
			public void onDrawerOpened(View drawerView)
			{
				super.onDrawerOpened(drawerView);
				mFlag = true;
			}
		};
		mActionBarDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

		NavigationView mNavTab = (NavigationView)findViewById(R.id.nav_view);
		mNavTab.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {
				Toast.makeText(getContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
				switch (menuItem.getItemId()) {

					default:
						break;
				}
				return false;
			}
		});
		mFaceIv = (ImageView)findViewById(R.id.iv_face);
		mSettingBt = (Button)findViewById(R.id.drawer_setting);
		mSettingBt.setOnClickListener(this);
		mQuitBt = (Button)findViewById(R.id.drawer_quit);
		mQuitBt.setOnClickListener(this);


		//mTopTab = (TopTabView)findViewById(R.id.toptab);
		mViewPager = (ViewPager)findViewById(R.id.vp_pager);

		mTestBt = (Button)findViewById(R.id.test_bt);
		if(Build.VERSION.SDK_INT>=21)
		{
			mTestBt.setBackground(getBaseContext().getResources().getDrawable(R.drawable.fab_selector_v21));
		}
		else
		{
			mTestBt.setBackground(getBaseContext().getResources().getDrawable(R.drawable.fab_selector));
		}


//		mTestBt.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				final MyFragmentPagerAdapter adapter = (MyFragmentPagerAdapter)mViewPager.getAdapter();
//				((HomeFragment)adapter.getItem(0)).scrollToTop();
//				Log.e(TAG,mViewPager.getHeight()+"");
//			}
//		});
	}

	private void initViewpager()
	{
		HomeFragment fragment1 = new HomeFragment();
		ApiPraFragment fragment2 = new ApiPraFragment();
//		TestFragment fragment1 = new TestFragment("#FF0000");
//		TestFragment fragment2 = TestFragment.newInstance("#FF0000");
		TestFragment fragment3 = TestFragment.newInstance("#0000FF");
		TestFragment fragment4 = TestFragment.newInstance("#000000");
		mFragmentList = new ArrayList<>();
		mFragmentList.add(fragment1);
		mFragmentList.add(fragment2);
		mFragmentList.add(fragment3);
		mFragmentList.add(fragment4);
		final MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		adapter.addFrag(mFragmentList.get(0),"Home");
		adapter.addFrag(mFragmentList.get(1), "Search");
		adapter.addFrag(mFragmentList.get(2), "Star");
		adapter.addFrag(mFragmentList.get(3), "Me");
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(adapter);
		mTabLayout.setupWithViewPager(mViewPager);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(mActionBarDrawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mActionBarDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mActionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed()
	{
		if(mFlag)
		{
			mDrawerLayout.closeDrawers();
		}

		else if(mIfQuit)
		{
			Intent intent = new Intent(this, NetService.class);
			stopService(intent);
			finish();
		}
		else
		{
			Toast.makeText(this,"press back to quit",Toast.LENGTH_LONG).show();
			mIfQuit = true;
			mUiHandler.postDelayed(runnable,3000);
		}
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			mIfQuit = false;
		}
	};

	public void showFloatBt()
	{
		mTestBt.animate().alpha(1);
		mTestBt.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
	}

	public void hideFloatBt()
	{
		mTestBt.animate().alpha(0);
		mTestBt.animate().translationY(mTestBt.getHeight() + mFloatBtMargin).setInterpolator(new AccelerateInterpolator(2)).start();
	}


	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.drawer_setting:
				Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
				startActivity(intent);
				break;
			case R.id.drawer_quit:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Really???").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				}).setNegativeButton("No!", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
				break;
			default:
				break;
		}

	}
}
