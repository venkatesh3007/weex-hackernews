package com.example.weex.hackernews;

import android.content.Intent;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

/**
 * Created by venkatesh on 13/1/17.
 */

public class InstantPageModule extends WXModule {
    private static final String WEEX_CATEGORY="com.taobao.android.intent.category.WEEX";

    @JSMethod
    public void openNewPage(String instantPageTitle, String url) {
        Intent intent = new Intent(mWXSDKInstance.getContext(), MyActivity.class);
        intent.putExtra("url_to_render", url);
        intent.putExtra("instant_page_title", instantPageTitle);
        mWXSDKInstance.getContext().startActivity(intent);
    }
}
