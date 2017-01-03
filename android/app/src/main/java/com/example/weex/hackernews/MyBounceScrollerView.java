package com.example.weex.hackernews;
import android.content.Context;

import com.taobao.weex.common.WXThread;
import com.taobao.weex.ui.component.WXScroller;
import com.taobao.weex.ui.view.WXScrollView;
import com.taobao.weex.ui.view.refresh.wrapper.BaseBounceView;

/**
 * Created by venkatesh on 7/12/16.
 */

public class MyBounceScrollerView extends BaseBounceView<MyScrollView> {

    @Override
    public boolean postDelayed(Runnable action, long delayMillis) {
        return super.postDelayed(WXThread.secure(action), delayMillis);
    }

    public MyBounceScrollerView(Context context, int orientation, MyScrollerComponent waScroller) {
        super(context,orientation);
        if (getInnerView() != null)
            getInnerView().setWAScroller(waScroller);
    }

    @Override
    public MyScrollView setInnerView(Context context) {
        return new MyScrollView(context);
    }

    @Override
    public void onRefreshingComplete() {
        //TODO update scroller dataset
    }

    @Override
    public void onLoadmoreComplete() {
        //TODO update scroller dataset
    }
}
