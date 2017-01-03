package com.example.weex.hackernews;

import android.view.View;

import com.taobao.weex.ui.view.IWebView;

/**
 * Created by vostro on 6/12/16.
 */

public interface MyIWebView {
    public View getView();
    public void destroy();
    public void loadUrl(String url);
    public void loadData(String data);
    public void reload();
    public void goBack();
    public void goForward();
    public void setShowLoading(boolean shown);
    public void setOnErrorListener(IWebView.OnErrorListener listener);
    public void setOnPageListener(IWebView.OnPageListener listener);

    public interface OnErrorListener {
        public void onError(String type, Object message);
    }

    public interface OnPageListener {
        public void onReceivedTitle(String title);
        public void onPageStart(String url);
        public void onPageFinish(String url, boolean canGoBack, boolean canGoForward);
    }
}