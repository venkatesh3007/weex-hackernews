package com.example.weex.hackernews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.OnWXScrollListener;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.component.AppearanceHelper;
import com.taobao.weex.ui.component.Scrollable;
import com.taobao.weex.ui.component.WXBaseRefresh;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXLoading;
import com.taobao.weex.ui.component.WXRefresh;
import com.taobao.weex.ui.component.WXScroller;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.component.helper.WXStickyHelper;
import com.taobao.weex.ui.view.IWXScroller;
import com.taobao.weex.ui.view.WXBaseRefreshLayout;
import com.taobao.weex.ui.view.WXHorizontalScrollView;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;
import com.taobao.weex.ui.view.refresh.wrapper.BaseBounceView;
import com.taobao.weex.ui.view.refresh.wrapper.BounceScrollerView;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXReflectionUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by venkatesh on 15/12/16.
 */

@Component(lazyload = false)

public class MyDrawerScrollComponent extends WXVContainer<ViewGroup> implements MyDrawerScrollView.WXScrollViewListener, Scrollable {

    public static final String DIRECTION = "direction";
    protected int mOrientation = Constants.Orientation.VERTICAL;
    private List<WXComponent> mRefreshs = new ArrayList<>();

    public static class Creator implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance instance, WXDomObject node, WXVContainer parent) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new WXScroller(instance, node, parent);
        }
    }

    /**
     * Map for storing appear information
     **/
    private Map<String, AppearanceHelper> mAppearanceComponents = new HashMap<>();

    /**
     * Map for storing component that is sticky.
     **/
    private Map<String, HashMap<String, WXComponent>> mStickyMap = new HashMap<>();
    private FrameLayout mRealView;

    private int mContentHeight = 0;

    private WXStickyHelper stickyHelper;
    private Handler handler = new Handler();


    public MyDrawerScrollComponent(WXSDKInstance instance, WXDomObject node,
                                   WXVContainer parent, boolean lazy) {
        super(instance, node, parent, lazy);
        stickyHelper = new WXStickyHelper(this);
    }

    /**
     * @return FrameLayout inner ScrollView
     */
    @Override
    public ViewGroup getRealView() {
        return mRealView;
    }


    @Override
    public void createViewImpl() {
        super.createViewImpl();
        for (int i = 0; i < mRefreshs.size(); i++) {
            WXComponent component = mRefreshs.get(i);
            component.createView();
            checkRefreshOrLoading(component);
        }
    }


    /**
     * @return ScrollView
     */
    public ViewGroup getInnerView() {
        if (getHostView() == null)
            return null;
        if (getHostView() instanceof BounceScrollerView) {
            return ((BounceScrollerView) getHostView()).getInnerView();
        } else {
            return getHostView();
        }
    }

    /**
     * Intercept refresh view and loading view
     */
    @Override
    protected void addSubView(View child, int index) {
        if (child == null || getRealView() == null) {
            return;
        }

        if (child instanceof WXBaseRefreshLayout) {
            return;
        }

        int count = getRealView().getChildCount();
        index = index >= count ? -1 : index;
        if (index == -1) {
            getRealView().addView(child);
        } else {
            getRealView().addView(child, index);
        }
    }

    /**
     * Intercept refresh view and loading view
     */
    @Override
    public void addChild(WXComponent child, int index) {
        if (child == null || index < -1) {
            return;
        }

        if (child instanceof WXBaseRefresh) {
            if (!checkRefreshOrLoading(child)) {
                mRefreshs.add(child);
            }
            return;
        }

        if (mChildren == null) {
            mChildren = new ArrayList<>();
        }
        int count = mChildren.size();
        index = index >= count ? -1 : index;
        if (index == -1) {
            mChildren.add(child);
        } else {
            mChildren.add(index, child);
        }
    }

    /**
     * Setting refresh view and loading view
     *
     * @param child the refresh_view or loading_view
     */

    private boolean checkRefreshOrLoading(final WXComponent child) {
        boolean result = false;
        if (child instanceof WXRefresh && getHostView() != null) {
            ((BaseBounceView) getHostView()).setOnRefreshListener((WXRefresh) child);
            Runnable runnable = WXThread.secure(new Runnable() {
                @Override
                public void run() {
                    ((BaseBounceView) getHostView()).setHeaderView(child);
                }
            });
            handler.postDelayed(runnable, 100);
        }

        if (child instanceof WXLoading && getHostView() != null) {
            ((BaseBounceView) getHostView()).setOnLoadingListener((WXLoading) child);
            Runnable runnable = WXThread.secure(new Runnable() {
                @Override
                public void run() {
                    ((BaseBounceView) getHostView()).setFooterView(child);
                }
            });
            handler.postDelayed(runnable, 100);
            result = true;
        }
        return result;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (mAppearanceComponents != null) {
            mAppearanceComponents.clear();
        }
        if (mStickyMap != null) {
            mStickyMap.clear();
        }
        if (getInnerView() != null && getInnerView() instanceof IWXScroller) {
            ((IWXScroller) getInnerView()).destroy();
        }
    }

    @Override
    protected MeasureOutput measure(int width, int height) {
        MeasureOutput measureOutput = new MeasureOutput();
        if (this.mOrientation == Constants.Orientation.HORIZONTAL) {
            int screenW = WXViewUtils.getScreenWidth(WXEnvironment.sApplication);
            int weexW = WXViewUtils.getWeexWidth(getInstanceId());
            measureOutput.width = width > (weexW >= screenW ? screenW : weexW) ? FrameLayout.LayoutParams.MATCH_PARENT
                    : width;
            measureOutput.height = height;
        } else {
            int screenH = WXViewUtils.getScreenHeight(WXEnvironment.sApplication);
            int weexH = WXViewUtils.getWeexHeight(getInstanceId());
            measureOutput.height = height > (weexH >= screenH ? screenH : weexH) ? FrameLayout.LayoutParams.MATCH_PARENT
                    : height;
            measureOutput.width = width;
        }
        return measureOutput;
    }

    @Override
    protected ViewGroup initComponentHostView(@NonNull Context context) {
        String scroll;
        if (getDomObject() == null || getDomObject().getAttrs().isEmpty()) {
            scroll = "vertical";
        } else {
            scroll = getDomObject().getAttrs().getScrollDirection();
        }

        ViewGroup host;
        if (("horizontal").equals(scroll)) {
            mOrientation = Constants.Orientation.HORIZONTAL;
            WXHorizontalScrollView scrollView = new WXHorizontalScrollView(context);
            mRealView = new FrameLayout(context);
            scrollView.setScrollViewListener(new WXHorizontalScrollView.ScrollViewListener() {
                @Override
                public void onScrollChanged(WXHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
                    procAppear(x, y, oldx, oldy);
                }
            });
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            scrollView.addView(mRealView, layoutParams);
            scrollView.setHorizontalScrollBarEnabled(false);

            host = scrollView;
        } else {
            mOrientation = Constants.Orientation.VERTICAL;
            MyDrawerBounceScrollView scrollerView = new MyDrawerBounceScrollView(context, mOrientation, this);
            mRealView = new FrameLayout(context);
            MyDrawerScrollView innerView = scrollerView.getInnerView();
            innerView.addScrollViewListener(this);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            innerView.addView(mRealView, layoutParams);
            innerView.setVerticalScrollBarEnabled(true);
            innerView.addScrollViewListener(new MyDrawerScrollView.WXScrollViewListener() {
                @Override
                public void onScrollChanged(MyDrawerScrollView scrollView, int x, int y, int oldx, int oldy) {

                }

                @Override
                public void onScrollToBottom(MyDrawerScrollView scrollView, int x, int y) {

                }

                @Override
                public void onScrollStopped(MyDrawerScrollView scrollView, int x, int y) {
                    List<OnWXScrollListener> listeners = getInstance().getWXScrollListeners();
                    if (listeners != null && listeners.size() > 0) {
                        for (OnWXScrollListener listener : listeners) {
                            if (listener != null) {
                                listener.onScrollStateChanged(scrollView, x, y, OnWXScrollListener.IDLE);
                            }
                        }
                    }
                }

                @Override
                public void onScroll(MyDrawerScrollView scrollView, int x, int y) {
                    List<OnWXScrollListener> listeners = getInstance().getWXScrollListeners();
                    if (listeners != null && listeners.size() > 0) {
                        for (OnWXScrollListener listener : listeners) {
                            if (listener != null) {
                                listener.onScrolled(scrollView, x, y);
                            }
                        }
                    }
                }
            });
            host = scrollerView;
        }
        return host;
    }

    @Override
    public int getScrollY() {
        return getInnerView() == null ? 0 : getInnerView().getScrollY();
    }

    @Override
    public int getScrollX() {
        return getInnerView() == null ? 0 : getInnerView().getScrollX();
    }

    public Map<String, HashMap<String, WXComponent>> getStickMap() {
        return mStickyMap;
    }

    @Override
    protected boolean setProperty(String key, Object param) {
        switch (key) {
            case Constants.Name.SHOW_SCROLLBAR:
                Boolean result = WXUtils.getBoolean(param, null);
                if (result != null)
                    setShowScrollbar(result);
                return true;
            case "alignment":
                setAlignment("start");
                return true;
        }
        return super.setProperty(key, param);
    }

    @WXComponentProp(name = Constants.Name.SHOW_SCROLLBAR)
    public void setShowScrollbar(boolean show) {
        if (getInnerView() == null) {
            return;
        }
        if (mOrientation == Constants.Orientation.VERTICAL) {
            getInnerView().setVerticalScrollBarEnabled(show);
        } else {
            getInnerView().setHorizontalScrollBarEnabled(show);
        }
    }

    @WXComponentProp(name = "alignment")
    public void setAlignment(String value) {
        if (getInnerView() == null) {
            return;
        }

        DrawerLayout.LayoutParams layoutParams = (DrawerLayout.LayoutParams) getInnerView().getLayoutParams();
        layoutParams.gravity = Gravity.START;
        getInnerView().setLayoutParams(layoutParams);
    }

    // TODO Need constrain, each container can only have one sticky child
    @Override
    public void bindStickStyle(WXComponent component) {
        stickyHelper.bindStickStyle(component, mStickyMap);
    }

    @Override
    public void unbindStickStyle(WXComponent component) {
        stickyHelper.unbindStickStyle(component, mStickyMap);
    }

    /**
     * Bind appear event
     */
    @Override
    public void bindAppearEvent(WXComponent component) {
        setWatch(AppearanceHelper.APPEAR, component, true);
    }

    private void setWatch(int event, WXComponent component, boolean isWatch) {
        AppearanceHelper item = mAppearanceComponents.get(component.getRef());
        if (item == null) {
            item = new AppearanceHelper(component);
            mAppearanceComponents.put(component.getRef(), item);
        }

        item.setWatchEvent(event, isWatch);

        procAppear(1, 1, 0, 0);//check current components appearance status.
    }

    /**
     * Bind disappear event
     */
    @Override
    public void bindDisappearEvent(WXComponent component) {
        setWatch(AppearanceHelper.DISAPPEAR, component, true);
    }

    /**
     * Remove appear event
     */
    @Override
    public void unbindAppearEvent(WXComponent component) {
        setWatch(AppearanceHelper.APPEAR, component, false);
    }

    /**
     * Remove disappear event
     */
    @Override
    public void unbindDisappearEvent(WXComponent component) {
        setWatch(AppearanceHelper.DISAPPEAR, component, false);
    }

    @Override
    public void scrollTo(WXComponent component, int offset) {
        int offsetIntF = (int) WXViewUtils.getRealPxByWidth(offset);

        int viewYInScroller = component.getAbsoluteY() - getAbsoluteY();
        int viewXInScroller = component.getAbsoluteX() - getAbsoluteX();

        scrollBy(viewXInScroller - getScrollX() + offsetIntF, viewYInScroller - getScrollY() + offsetIntF);
    }

    /**
     * Scroll by specified distance. Horizontal scroll is not supported now.
     *
     * @param x horizontal distance, not support
     * @param y vertical distance. Negative for scroll to top
     */
    public void scrollBy(final int x, final int y) {
        if (getInnerView() == null) {
            return;
        }

        getInnerView().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getInnerView() == null) {
                    return;
                }
                if (mOrientation == Constants.Orientation.VERTICAL) {
                    ((MyDrawerScrollView) getInnerView()).smoothScrollBy(0, y);
                } else {
                    ((WXHorizontalScrollView) getInnerView()).smoothScrollBy(x, 0);
                }
                getInnerView().invalidate();
            }
        }, 16);
    }

    @Override
    public void onScrollChanged(MyDrawerScrollView scrollView, int x, int y,
                                int oldx, int oldy) {
        procAppear(x, y, oldx, oldy);
    }

    /**
     * Process event like appear and disappear
     */
    private void procAppear(int x, int y, int oldx,
                            int oldy) {
        String direction = y - oldy > 0 ? "up" : "down";
        if (mOrientation == Constants.Orientation.HORIZONTAL) {
            direction = x - oldx > 0 ? "right" : "left";
        }

        for (Map.Entry<String, AppearanceHelper> item : mAppearanceComponents.entrySet()) {
            AppearanceHelper helper = item.getValue();

            if (!helper.isWatch()) {
                continue;
            }
            boolean visible = helper.isViewVisible();

            int result = helper.setAppearStatus(visible);
            if (result != AppearanceHelper.RESULT_NO_CHANGE) {
                helper.getAwareChild().notifyAppearStateChange(result == AppearanceHelper.RESULT_APPEAR ? Constants.Event.APPEAR : Constants.Event.DISAPPEAR, direction);
            }
        }
    }

    @Override
    public void onScrollToBottom(MyDrawerScrollView scrollView, int x, int y) {

    }

    @Override
    public void onScrollStopped(MyDrawerScrollView scrollView, int x, int y) {
    }

    @Override
    public void onScroll(MyDrawerScrollView scrollView, int x, int y) {

        this.onLoadMore(scrollView, x, y);
    }

    /**
     * Handle loadMore Event.when Scroller has bind loadMore Event and set the attr of loadMoreOffset
     * it will tell the JS to handle the event of onLoadMore;
     *
     * @param scrollView the MyDrawerScrollView
     * @param x          the X direction
     * @param y          the Y direction
     */
    protected void onLoadMore(MyDrawerScrollView scrollView, int x, int y) {
        try {
            String offset = getDomObject().getAttrs().getLoadMoreOffset();

            if (TextUtils.isEmpty(offset)) {
                return;
            }

            int contentH = scrollView.getChildAt(0).getHeight();
            int scrollerH = scrollView.getHeight();
            int offScreenY = contentH - y - scrollerH;
            if (offScreenY < Integer.parseInt(offset)) {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("[WXScroller-onScroll] offScreenY :" + offScreenY);
                }

                if (mContentHeight != contentH) {
                    getInstance().fireEvent(getDomObject().getRef(), Constants.Event.LOADMORE);
                    mContentHeight = contentH;
                }
            }
        } catch (Exception e) {
            WXLogUtils.d("[WXScroller-onScroll] ", e);
        }

    }
}
