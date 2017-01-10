package com.example.weex.hackernews;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.NestedContainer;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXLoading;
import com.taobao.weex.ui.component.WXRefresh;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.component.list.WXListComponent;
import com.taobao.weex.ui.view.refresh.wrapper.BounceRecyclerView;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;

/**
 * Created by venkatesh on 15/12/16.
 */

public class MyViewPagerComponent extends WXVContainer<CoordinatorLayout> implements ViewPager.OnPageChangeListener{

    MyViewPagerAdapter myViewPagerAdapter;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    public MyViewPagerComponent(WXSDKInstance instance, WXDomObject node, WXVContainer parent) {
        super(instance, node, parent);
    }

    @Override
    protected CoordinatorLayout initComponentHostView(@NonNull Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) layoutInflater.inflate(R.layout.my_view_pager_layout, null);
        viewPager = (ViewPager) coordinatorLayout.findViewById(R.id.viewpager);
        toolbar = (Toolbar) coordinatorLayout.findViewById(R.id.toolbar);
        myViewPagerAdapter = new MyViewPagerAdapter(getContext());
        viewPager.setAdapter(myViewPagerAdapter);

        tabLayout = (TabLayout) coordinatorLayout.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);
        myViewPagerAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (tabLayout.getTabCount() < myViewPagerAdapter.getCount()) {
                    tabLayout.addTab(tabLayout.newTab().setText(myViewPagerAdapter.getPageTitle(myViewPagerAdapter.getCount() - 1)), (myViewPagerAdapter.getCount() - 1));
                }
            }
        });
        return coordinatorLayout;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public MyViewPagerAdapter getMyViewPagerAdapter() {
        return myViewPagerAdapter;
    }

    @Override
    protected boolean setProperty(String key, Object param) {
        Log.d("key", key);
        switch (key) {
            case "primarycolor":
                String result = WXUtils.getString(param, null);
                if (result != null)
                    setPrimaryColor(result);
                return true;
        }
        return super.setProperty(key, param);
    }

    @WXComponentProp(name = "primarycolor")
    public void setPrimaryColor(String value) {
        if (getHostView() == null) {
            return;
        }

        if ((getToolbar() != null) && (getTabLayout() != null)) {
            int primaryColor = Color.parseColor(value);
            getToolbar().setBackgroundColor(primaryColor);
            getTabLayout().setBackgroundColor(primaryColor);
        }
    }

    @Override
    public void remove(WXComponent child, boolean destroy) {
        int index = mChildren.indexOf(child);
        if (destroy) {
            child.detachViewAndClearPreInfo();
        }

        CoordinatorLayout view = getHostView();
        if(view == null){
            return;
        }
        if (child instanceof MyPageItemComponent) {
            getMyViewPagerAdapter().removePageItem(child.getDomObject().getAttrs().get("title").toString(), child.getHostView());
        }
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d("remove viewpager child", "removeChild child at " + index);
        }
        super.remove(child, destroy);
    }

    @Override
    protected void addSubView(View child, int index) {
        if (child == null || myViewPagerAdapter == null) {
            return;
        }
        WXComponent childComponentToAdd = getChild(index);
        if (childComponentToAdd instanceof ToolbarComponent) {
            getToolbar().addView(child);
        } else if (childComponentToAdd instanceof MyPageItemComponent) {
            String title = childComponentToAdd.getDomObject().getAttrs().get("title").toString();
            myViewPagerAdapter.addPageItem(child, title);
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    protected void onHostViewInitialized(CoordinatorLayout host) {
        getInstance().setToolbar(getToolbar());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}