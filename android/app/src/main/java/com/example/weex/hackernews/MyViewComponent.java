package com.example.weex.hackernews;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXText;
import com.taobao.weex.ui.component.WXVContainer;

/**
 * Created by vostro on 2/12/16.
 */

@Component(lazyload = false)
public class MyViewComponent extends WXText {


    @Deprecated
    public MyViewComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent, String instanceId, boolean isLazy) {
        this(instance, dom, parent, isLazy);
    }

    public MyViewComponent(WXSDKInstance instance, WXDomObject node, WXVContainer parent, boolean lazy) {
        super(instance, node, parent);
    }
}

