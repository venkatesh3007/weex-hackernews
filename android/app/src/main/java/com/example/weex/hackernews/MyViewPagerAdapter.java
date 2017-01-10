package com.example.weex.hackernews;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.list.WXListComponent;
import com.taobao.weex.ui.view.WXFrameLayout;
import com.taobao.weex.ui.view.refresh.wrapper.BounceRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by venkatesh on 15/12/16.
 */

public class MyViewPagerAdapter extends PagerAdapter {

    private List<View> pagerViewList;
    private List<WXComponent> pagerItemList;
    private List<String> titleList;
    private Context mContext;

    private int selectedPage;

    public MyViewPagerAdapter(Context context) {
        pagerItemList = new ArrayList<>();
        pagerViewList = new ArrayList<>();
        titleList = new ArrayList<>();
        mContext = context;
        selectedPage = -1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View view = inflater.inflate(R.layout.fragment_example_view, container, false);
        View view = pagerViewList.get(position);
//        view.setLayoutParams(new LinearLayout.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT));
//        ((BounceRecyclerView) ((ViewGroup) view).getChildAt(0)).getInnerView().setClipToPadding(false);
        Log.d("view name", view.toString());
        if (view.getParent() == null) {
            container.addView(view);
        } else {
            ((ViewGroup)view.getParent()).removeView(view);
            container.addView(view);
        }
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

    public int getSelectedPage() {
        return selectedPage;
    }

    public void setSelectedPage(int selectedPage) {
        this.selectedPage = selectedPage;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void addPageItem(WXComponent child, View view, String title) {
        pagerItemList.add(child);
        pagerViewList.add(view);
        titleList.add(title);
        notifyDataSetChanged();
    }

    public WXComponent getPagerItemAtPosition(int position) {
        return pagerItemList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof View && pagerViewList.indexOf(object) != -1) {
            return pagerViewList.indexOf(object);
        } else {
            return POSITION_NONE;
        }
    }


    public void removePageItem(String title, View view, WXComponent child) {
        setSelectedPage(-1);
        pagerItemList.remove(child);
        pagerViewList.remove(view);
        titleList.remove(title);
        notifyDataSetChanged();
    }

    public void removeAllPageItems() {
        pagerItemList.clear();
        pagerViewList.clear();
        titleList.clear();
        notifyDataSetChanged();
    }
}
