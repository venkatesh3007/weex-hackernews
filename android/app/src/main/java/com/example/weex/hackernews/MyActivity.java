package com.example.weex.hackernews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;

/**
 * Created by venkatesh on 13/1/17.
 */

public class MyActivity extends AppCompatActivity implements IWXRenderListener {
    WXSDKInstance mWXSDKInstance;
    ViewGroup container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ab_instant_page);
        container = (LinearLayout) findViewById(R.id.container);

        // To be Removed -- start
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // To be Removed -- end

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String urlToRender = bundle.getString("url_to_render");
            String instantPageTitle = bundle.getString("instant_page_title");

            getSupportActionBar().setTitle(instantPageTitle);
            mWXSDKInstance = new WXSDKInstance(this);
            mWXSDKInstance.registerRenderListener(this);
            mWXSDKInstance.renderByUrl("ab_instant_page", urlToRender, null, null, WXRenderStrategy.APPEND_ASYNC);
        }
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        container.addView(view);
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
