package com.example.weex.hackernews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.IWebView;
import com.taobao.weex.utils.WXUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vostro on 6/12/16.
 */

public class MyWebComponent extends WXComponent {

    protected MyIWebView mWebView;
    protected String mData;

    public MyWebComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent, boolean isLazy) {
        super(instance, dom, parent, isLazy);
        createWebView();
    }

    protected void  createWebView(){
        mWebView = new MyWebView(getContext());
    }

    @Override
    protected View initComponentHostView(@NonNull Context context) {
        mWebView.setOnErrorListener(new IWebView.OnErrorListener() {
            @Override
            public void onError(String type, Object message) {
                fireEvent(type, message);
            }
        });
        mWebView.setOnPageListener(new IWebView.OnPageListener() {
            @Override
            public void onReceivedTitle(String title) {
                if (getDomObject().getEvents().contains(Constants.Event.RECEIVEDTITLE)) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("title", title);
                    getInstance().fireEvent(getRef(), Constants.Event.RECEIVEDTITLE, params);
                }
            }

            @Override
            public void onPageStart(String url) {
                if ( getDomObject().getEvents().contains(Constants.Event.PAGESTART)) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("url", url);
                    getInstance().fireEvent(getRef(), Constants.Event.PAGESTART, params);
                }
            }

            @Override
            public void onPageFinish(String url, boolean canGoBack, boolean canGoForward) {
                if ( getDomObject().getEvents().contains(Constants.Event.PAGEFINISH)) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("url", url);
                    params.put("canGoBack", canGoBack);
                    params.put("canGoForward", canGoForward);
                    getInstance().fireEvent(getRef(), Constants.Event.PAGEFINISH, params);
                }
            }
        });
        return mWebView.getView();
    }

    @Override
    public void destroy() {
        super.destroy();
        getWebView().destroy();
    }

    @Override
    protected boolean setProperty(String key, Object param) {
        switch (key) {
            case Constants.Name.SHOW_LOADING:
                Boolean result = WXUtils.getBoolean(param,null);
                if (result != null)
                    setShowLoading(result);
                return true;
            case Constants.Name.SRC:
                String src = WXUtils.getString(param,null);
                if (src != null)
                    setUrl(src);
                return true;
        }
        return super.setProperty(key,param);
    }

    @WXComponentProp(name = Constants.Name.SHOW_LOADING)
    public void setShowLoading(boolean showLoading) {
        getWebView().setShowLoading(showLoading);
    }

    @WXComponentProp(name = Constants.Name.SRC)
    public void setUrl(String url) {
        if (TextUtils.isEmpty(url) || getHostView() == null) {
            return;
        }
        mData = url;
        if (!TextUtils.isEmpty(url)) {
            loadData(url);
        }
    }

    public void setAction(String action) {
        if (!TextUtils.isEmpty(action)) {
            if (action.equals("goBack")) {
                goBack();
            } else if (action.equals("goForward")) {
                goForward();
            } else if (action.equals("reload")) {
                reload();
            }
        }
    }

    private void fireEvent(String type, Object message) {
        if (getDomObject().getEvents().contains(Constants.Event.ERROR)) {
            Map<String, Object> params = new HashMap<>();
            params.put("type", type);
            params.put("errorMsg", message);
            getInstance().fireEvent(getRef(), Constants.Event.ERROR, params);
        }
    }

    private void loadData(String data) {
        getWebView().loadData(data);
    }

    private void reload() {
        getWebView().reload();
    }

    private void goForward() {
        getWebView().goForward();
    }

    private void goBack() {
        getWebView().goBack();
    }

    private MyIWebView getWebView() {
        return mWebView;
    }

}
