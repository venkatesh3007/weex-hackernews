package com.example.weex.hackernews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View;

import com.taobao.weex.ui.view.IWXTextView;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;

/**
 * Created by venkatesh on 6/1/17.
 */
public class MyTextView extends View implements WXGestureObservable, IWXTextView {

    private WXGesture wxGesture;
    private Layout textLayout;
    public MyTextView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        Layout layout= getTextLayout();
        if(layout!=null){
            canvas.translate(getPaddingLeft(),getPaddingTop());
            layout.draw(canvas);
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        if (wxGesture != null) {
            result |= wxGesture.onTouch(this, event);
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Spanned spanned = (Spanned) getText();
            URLSpan[] urls = spanned.getSpans(0, spanned.length(), URLSpan.class);
            for (int i = 0; i < urls.length; i++) {
                int start = spanned.getSpanStart(urls[i]);
                int end = spanned.getSpanEnd(urls[i]);

                Path dest = new Path();
                getTextLayout().getSelectionPath(start, end, dest);

                RectF urlBounds = new RectF();
                dest.computeBounds(urlBounds, true);

                urlBounds.offset(getPaddingLeft(), getPaddingTop());

                if (urlBounds.contains(event.getX(), event.getY())) {
                    urls[i].onClick(this);
                    return true;
                }
            }
        }
        return result;
    }

    @Override
    public void registerGestureListener(WXGesture wxGesture) {
        this.wxGesture = wxGesture;
    }

    @Override
    public CharSequence getText() {
        return textLayout.getText();
    }

    public Layout getTextLayout() {
        return textLayout;
    }

    public void setTextLayout(Layout layout) {
        this.textLayout = layout;
    }
}