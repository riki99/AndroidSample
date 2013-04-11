
package com.satton.overlay;

import com.satton.R;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MobagePagerAdapter extends PagerAdapter {
    int NUM_VIEW = 2;
    Context context;
    LayoutInflater mInflater;

    public MobagePagerAdapter(Context context) {
        this.context = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // 各ページ生成時に呼び出される
    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        ViewPager pager = (ViewPager) collection;
        View view = null;
        if (position == 0) {
            view = mInflater.inflate(R.layout.pager_grip, pager, false);
        } else {
            view = mInflater.inflate(R.layout.pager_main, pager, false);
        }
        pager.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);
    }

    @Override
    public int getCount() {
        return NUM_VIEW;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    @Override
    public void restoreState(Parcelable parcel, ClassLoader classLoader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(ViewGroup collection) {
    }
}
