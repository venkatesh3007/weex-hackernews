package com.example.weex.hackernews;


import android.app.Application;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;
import com.taobao.weex.dom.WXScrollerDomObject;


/**
 * Created by Hanks on 16/12/8.
 */
public class WXApplication extends Application {

    public static int linkColor = Color.BLUE;

    @Override
    public void onCreate() {
        super.onCreate();
        linkColor = ContextCompat.getColor(getBaseContext(), R.color.ab_blue);
        InitConfig config=new InitConfig.Builder().setImgAdapter(new ImageAdapter()).build();
        WXSDKEngine.initialize(this,config);
        try {
            WXSDKEngine.registerComponent("myview", MyViewComponent.class);
            WXSDKEngine.registerComponent("myweb", MyWebComponent.class);
            WXSDKEngine.registerComponent("myscroller", MyScrollerComponent.class);
            WXSDKEngine.registerComponent("mydrawerlayout", MyDrawerLayoutComponent.class);
            WXSDKEngine.registerComponent("mydrawerview", MyDrawerScrollComponent.class);
            WXSDKEngine.registerComponent("mypageview", MyViewPagerComponent.class);
            WXSDKEngine.registerComponent("mypageitem", MyPageItemComponent.class);
            WXSDKEngine.registerComponent("toolbar", ToolbarComponent.class);
            WXSDKEngine.registerComponent("mylist", MyListComponent.class);
            WXSDKEngine.registerDomObject("myview", MyViewDomObject.class);
//      WXSDKEngine.registerDomObject("toolbar", WXTextDomObject.class);
            WXSDKEngine.registerDomObject("myscroller", WXScrollerDomObject.class);
            WXSDKEngine.registerModule("draweritemsmanager", MyDrawerItemClickListenerModule.class);
        } catch (WXException e) {
            e.printStackTrace();
        }
    }

    public static int getLinkColor() {
        return linkColor;
    }
}

