package com.example.weex.hackernews;

import android.content.Context;

import com.taobao.weex.common.WXThread;
import com.taobao.weex.ui.view.refresh.wrapper.BaseBounceView;

/**
 * Created by venkatesh on 15/12/16.
 */

public class MyDrawerBounceScrollView extends BaseBounceView<MyDrawerScrollView> {

    @Override
    public boolean postDelayed(Runnable action, long delayMillis) {
        return super.postDelayed(WXThread.secure(action), delayMillis);
    }

    public MyDrawerBounceScrollView(Context context, int orientation, MyDrawerScrollComponent waScroller) {
        super(context,orientation);
        if (getInnerView() != null)
            getInnerView().setWAScroller(waScroller);
    }

    @Override
    public MyDrawerScrollView setInnerView(Context context) {
        return new MyDrawerScrollView(context);
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
