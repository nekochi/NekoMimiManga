package com.nekomimi.adapter_listener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter 
{
	private final List<Fragment> mFragments = new ArrayList<> ();
	private final List<String> mFragmentTitles = new ArrayList<>();
	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public void changeFrag(Fragment fragment ,int position)
	{
		changeFrag(fragment,null,position);
	}
	public void changeFrag(Fragment fragment,String title ,int position)
	{
		mFragments.set(position,fragment);
		if(title != null) mFragmentTitles.add(position,title);
	}
	public void removeFrag(int position)
	{
		mFragmentTitles.remove(position);
		mFragments.remove(position);
	}
	public void addFrag(Fragment fragment, String title) {
		mFragments.add(fragment);
		mFragmentTitles.add(title);
	}
	
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		return mFragmentTitles.get(position);
	}
}
