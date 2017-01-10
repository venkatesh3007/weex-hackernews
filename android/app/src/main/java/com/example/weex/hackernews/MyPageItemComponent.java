package com.example.weex.hackernews;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXDiv;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.WXFrameLayout;

/**
 * Created by venkatesh on 18/12/16.
 */

public class MyPageItemComponent extends WXDiv {
    String title = null;

    public MyPageItemComponent(WXSDKInstance instance, WXDomObject node, WXVContainer parent) {
        super(instance, node, parent);
    }

    @Override
    public ViewGroup.LayoutParams getChildLayoutParams(WXComponent child, View childView, int width, int height, int left, int right, int top, int bottom) {
        return super.getChildLayoutParams(child, childView, width, height, left, right, top, bottom);
    }

    @Override
    protected void onHostViewInitialized(WXFrameLayout host) {
        super.onHostViewInitialized(host);
        this.title = this.getDomObject().getAttrs().get("title").toString();
        Log.d("set title: ", this.title);
    }

    @Override
    public void destroy() {
//        String titleToRemove = this.getDomObject().getAttrs().get("title").toString();
//        View viewToRemove = this.getHostView();
//        ((MyViewPagerComponent) this.getParent()).getMyViewPagerAdapter().removePageItem(titleToRemove, viewToRemove);
//        ((MyViewPagerComponent) this.getParent()).getMyViewPagerAdapter().notifyDataSetChanged();
        Log.d("page item destroyed: ", this.title);
        super.destroy();
    }
}
