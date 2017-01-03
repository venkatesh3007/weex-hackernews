package com.example.weex.hackernews;


import android.app.Application;

import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;
import com.taobao.weex.dom.WXScrollerDomObject;


/**
 * Created by Hanks on 16/12/8.
 */
public class WXApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
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
        } catch (WXException e) {
            e.printStackTrace();
        }
    }
}

