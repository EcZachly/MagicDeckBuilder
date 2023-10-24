package com.zach.wilson.magic.app.adapters;

import java.util.Vector;

import com.zach.wilson.magic.app.helpers.JazzyViewPager;
import com.zach.wilson.magic.app.helpers.JazzyViewPager.TransitionEffect;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class CustomPagerAdapter extends PagerAdapter {


	private Vector<View> pages;
	private JazzyViewPager pager;

	
	
	public CustomPagerAdapter(JazzyViewPager pager, Context context,
			Vector<View> pages) {
		this.pages = pages;
		this.pager = pager;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View page = pages.get(position);
		container.addView(page);
		pager.setObjectForPosition(page, position);
		pager.setTransitionEffect(TransitionEffect.FlipHorizontal);
		return page;
	}

	@Override
	public int getCount() {
		return pages.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}