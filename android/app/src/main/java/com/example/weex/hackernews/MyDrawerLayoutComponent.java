package com.example.weex.hackernews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXVContainer;

/**
 * Created by venkatesh on 15/12/16.
 */

public class MyDrawerLayoutComponent extends WXVContainer<DrawerLayout> {

    public MyDrawerLayoutComponent(WXSDKInstance instance, WXDomObject node, WXVContainer parent) {
        super(instance, node, parent);
    }

    @Override
    protected DrawerLayout initComponentHostView(@NonNull Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        DrawerLayout drawerLayout = (DrawerLayout) layoutInflater.inflate(R.layout.my_drawer_view_layout, null);
        return drawerLayout;
    }

    @Override
    protected void onHostViewInitialized(DrawerLayout host) {
        super.onHostViewInitialized(host);
        getInstance().setRootDrawerLayout(host);
    }
}
