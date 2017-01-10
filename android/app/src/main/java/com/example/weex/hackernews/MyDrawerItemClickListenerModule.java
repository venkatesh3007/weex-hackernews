package com.example.weex.hackernews;

import android.support.v4.widget.DrawerLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by venkatesh on 8/1/17.
 */

public class MyDrawerItemClickListenerModule extends WXModule {
    private static final String WEEX_CATEGORY="com.taobao.android.intent.category.WEEX";

    @JSMethod
    public void closeDrawer(String data) {
        DrawerLayout drawer = WXSDKManager.getInstance().getSDKInstance("1").getRootDrawerLayout();
        if (drawer != null) {
            drawer.closeDrawers();
        }
    }
}
