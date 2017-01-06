package com.example.weex.hackernews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Layout;
import android.view.ViewGroup;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXText;
import com.taobao.weex.ui.component.WXVContainer;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by vostro on 2/12/16.
 */

@Component(lazyload = false)
public class MyViewComponent extends WXComponent<MyTextView> {

    /**
     * The default text size
     **/
    public static final int sDEFAULT_SIZE = 32;

    public static class Creator implements ComponentCreator {

        public WXComponent createInstance(WXSDKInstance instance, WXDomObject node, WXVContainer parent) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new MyViewComponent(instance, node, parent);
        }
    }

    @Deprecated
    public MyViewComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent, String instanceId, boolean isLazy) {
        this(instance, dom, parent);
        TextInputLayout textInputLayout = new TextInputLayout(getContext());
    }

    public MyViewComponent(WXSDKInstance instance, WXDomObject node,
                           WXVContainer parent) {
        super(instance, node, parent);
    }

    @Override
    protected MyTextView initComponentHostView(@NonNull Context context) {
        return new MyTextView(context);
    }

    @Override
    public void updateExtra(Object extra) {
        if (extra instanceof Layout &&
                getHostView() != null && !extra.equals(getHostView().getTextLayout())) {
            final Layout layout = (Layout) extra;
            getHostView().setTextLayout(layout);
            getHostView().invalidate();
        }
    }

    @Override
    public void refreshData(WXComponent component) {
        super.refreshData(component);
        if (component instanceof MyViewComponent) {
            updateExtra(component.getDomObject().getExtra());
        }
    }

    @Override
    protected boolean setProperty(String key, Object param) {
        switch (key) {
            case Constants.Name.LINES:
            case Constants.Name.FONT_SIZE:
            case Constants.Name.FONT_WEIGHT:
            case Constants.Name.FONT_STYLE:
            case Constants.Name.COLOR:
            case Constants.Name.TEXT_DECORATION:
            case Constants.Name.FONT_FAMILY:
            case Constants.Name.TEXT_ALIGN:
            case Constants.Name.TEXT_OVERFLOW:
            case Constants.Name.LINE_HEIGHT:
            case Constants.Name.VALUE:
                return true;
            default:
                return super.setProperty(key, param);
        }
    }

    /**
     * Flush view no matter what height and width the {@link WXDomObject} specifies.
     * @param extra must be a {@link Layout} object, otherwise, nothing will happen.
     */
    private void flushView(Object extra) {
        if (extra instanceof Layout &&
                getHostView() != null && !extra.equals(getHostView().getTextLayout())) {
            final Layout layout = (Layout) extra;
            /**The following if block change the height of the width of the textView.
             * other part of the code is the same to updateExtra
             */
            ViewGroup.LayoutParams layoutParams = getHostView().getLayoutParams();
            if (layoutParams != null) {
                layoutParams.height = layout.getHeight();
                layoutParams.width = layout.getWidth();
                getHostView().setLayoutParams(layoutParams);
            }
            getHostView().setTextLayout(layout);
            getHostView().invalidate();
        }
    }
}

