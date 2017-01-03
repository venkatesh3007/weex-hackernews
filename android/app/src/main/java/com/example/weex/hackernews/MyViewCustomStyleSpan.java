package com.example.weex.hackernews;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import com.taobao.weex.dom.WXStyle;
import com.taobao.weex.utils.TypefaceUtil;

/**
 * Created by vostro on 4/12/16.
 */

class MyViewCustomStyleSpan extends MetricAffectingSpan {

    private final int mStyle;
    private final int mWeight;
    private final String mFontFamily;

    public MyViewCustomStyleSpan(int fontStyle, int fontWeight, String fontFamily) {
        mStyle = fontStyle;
        mWeight = fontWeight;
        mFontFamily = fontFamily;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        TypefaceUtil.applyFontStyle(ds, mStyle, mWeight, mFontFamily);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        TypefaceUtil.applyFontStyle(paint, mStyle, mWeight, mFontFamily);
    }

    /**
     * Returns {@link Typeface#NORMAL} or {@link Typeface#ITALIC}.
     */
    public int getStyle() {
        return (mStyle == WXStyle.UNSET ? 0 : mStyle);
    }

    /**
     * Returns {@link Typeface#NORMAL} or {@link Typeface#BOLD}.
     */
    public int getWeight() {
        return (mWeight == WXStyle.UNSET ? 0 : mWeight);
    }

    /**
     * Returns the font family set for this StyleSpan.
     */
    public String getFontFamily() {
        return mFontFamily;
    }
}
