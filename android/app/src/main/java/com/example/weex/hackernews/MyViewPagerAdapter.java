package com.example.weex.hackernews;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.taobao.weex.ui.component.list.WXListComponent;
import com.taobao.weex.ui.view.WXFrameLayout;
import com.taobao.weex.ui.view.refresh.wrapper.BounceRecyclerView;

import java.util.ArrayList;

/**
 * Created by venkatesh on 15/12/16.
 */

public class MyViewPagerAdapter extends PagerAdapter {

    private ArrayList<View> pagerViewList;
    private ArrayList<String> titleList;
    private Context mContext;

    public MyViewPagerAdapter(Context context) {
        pagerViewList = new ArrayList<>();
        titleList = new ArrayList<>();
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View view = inflater.inflate(R.layout.fragment_example_view, container, false);
        View view = pagerViewList.get(position);
//        view.setLayoutParams(new LinearLayout.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT));
//        ((BounceRecyclerView) ((ViewGroup) view).getChildAt(0)).getInnerView().setClipToPadding(false);
        Log.d("view name", view.toString());
        container.addView(view);
//        pagerViewList.add(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void addPageItem(View view, String title) {
        pagerViewList.add(view);
        titleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return pagerViewList.indexOf(object);
    }


    public void removePageItem(String title, View view) {
        pagerViewList.remove(view);
        titleList.remove(title);
    }

    public void removeAllPageItems() {
        pagerViewList.clear();
        titleList.clear();
    }
}
