package com.example.weex.hackernews;

import android.view.View;
import android.view.ViewGroup;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXDiv;
import com.taobao.weex.ui.component.WXVContainer;

/**
 * Created by venkatesh on 18/12/16.
 */

public class MyPageItemComponent extends WXDiv {
    public MyPageItemComponent(WXSDKInstance instance, WXDomObject node, WXVContainer parent) {
        super(instance, node, parent);
    }

    @Override
    public ViewGroup.LayoutParams getChildLayoutParams(WXComponent child, View childView, int width, int height, int left, int right, int top, int bottom) {
        return super.getChildLayoutParams(child, childView, width, height, left, right, top, bottom);
    }

    @Override
    public void destroy() {
        String titleToRemove = this.getDomObject().getAttrs().get("title").toString();
        View viewToRemove = this.getHostView();
        ((MyViewPagerComponent) this.getParent()).getMyViewPagerAdapter().removePageItem(titleToRemove, viewToRemove);
        ((MyViewPagerComponent) this.getParent()).getMyViewPagerAdapter().notifyDataSetChanged();
        super.destroy();
    }
}
