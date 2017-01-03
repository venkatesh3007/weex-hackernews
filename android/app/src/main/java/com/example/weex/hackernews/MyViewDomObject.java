package com.example.weex.hackernews;

import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import com.taobao.weex.WXEnvironment;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.dom.WXLineHeightSpan;
import com.taobao.weex.dom.WXStyle;
import com.taobao.weex.dom.WXTextDomObject;
import com.taobao.weex.dom.flex.CSSConstants;
import com.taobao.weex.dom.flex.CSSNode;
import com.taobao.weex.dom.flex.FloatUtil;
import com.taobao.weex.dom.flex.MeasureOutput;
import com.taobao.weex.dom.flex.Spacing;
import com.taobao.weex.ui.component.WXText;
import com.taobao.weex.ui.component.WXTextDecoration;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXResourceUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.taobao.weex.dom.WXStyle.UNSET;

/**
 * Created by vostro on 4/12/16.
 */

public class MyViewDomObject extends WXDomObject {
    /**
     * Command object for setSpan
     */
    private static class SetSpanOperation {

        protected int start, end;
        protected Object what;
        protected String type;

        SetSpanOperation(int start, int end, Object what, String type) {
            this.start = start;
            this.end = end;
            this.what = what;
            this.type = type;
        }

        SetSpanOperation(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void execute(Spannable sb) {
            sb.setSpan(what, start, sb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        public void execute(SpannableStringBuilder span, String value) {
            Pattern pattern = Pattern.compile("\\{b\\}(.*?)\\{b\\}");
            Matcher match = pattern.matcher(value);
            int indexTracker = 0;
            while (match.find()) {
                span.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                        match.start() - indexTracker, match.end() - indexTracker, 0);
                span.delete(match.start() - indexTracker, (match.start() + 3 - indexTracker));
                indexTracker += 3;
                span.delete((match.end() - 3) - indexTracker, match.end() - indexTracker);
                indexTracker += 3;
            }
        }
    }

    /**
     * Object for calculating text's width and height. This class is an anonymous class of
     * implementing {@link com.taobao.weex.dom.flex.CSSNode.MeasureFunction}
     */
    private static final CSSNode.MeasureFunction TEXT_MEASURE_FUNCTION = new CSSNode.MeasureFunction() {
        @Override
        public void measure(CSSNode node, float width, MeasureOutput measureOutput) {
            MyViewDomObject textDomObject = (MyViewDomObject) node;
            if (CSSConstants.isUndefined(width)) {
                width = node.cssstyle.maxWidth;
            }
            if(textDomObject.getTextWidth(textDomObject.mTextPaint,width,false)>0) {
                textDomObject.layout = textDomObject.createLayout(width, false, null);
                textDomObject.hasBeenMeasured = true;
                textDomObject.previousWidth = textDomObject.layout.getWidth();
                measureOutput.height = textDomObject.layout.getHeight();
                measureOutput.width = textDomObject.previousWidth;
            }else{
                measureOutput.height = 0;
                measureOutput.width = 0;
            }
        }
    };


    private static final Canvas DUMMY_CANVAS = new Canvas();
    private static final String ELLIPSIS = "\u2026";
    private boolean mIsColorSet = false;
    private boolean hasBeenMeasured = false;
    private int mColor;
    /**
     * mFontStyle can be {@link Typeface#NORMAL} or {@link Typeface#ITALIC}.
     */
    private int mFontStyle = UNSET;
    /**
     * mFontWeight can be {@link Typeface#NORMAL} or {@link Typeface#BOLD}.
     */
    private int mFontWeight = UNSET;
    private int mNumberOfLines = UNSET;
    private int mFontSize = UNSET;
    private int mLineHeight = UNSET;
    private float previousWidth = Float.NaN;
    private String mFontFamily = null;
    private String mText = null;
    private TextUtils.TruncateAt textOverflow;
    private Layout.Alignment mAlignment;
    private WXTextDecoration mTextDecoration = WXTextDecoration.NONE;
    private TextPaint mTextPaint = new TextPaint();
    private @Nullable
    Spanned spanned;
    private @Nullable Layout layout;
    private AtomicReference<Layout> atomicReference = new AtomicReference<>();

    /**
     * Create an instance of current class, and set {@link #TEXT_MEASURE_FUNCTION} as the
     * measureFunction
     * @see CSSNode#setMeasureFunction(CSSNode.MeasureFunction)
     */
    public MyViewDomObject() {
        super();
        mTextPaint.setFlags(TextPaint.ANTI_ALIAS_FLAG);
        setMeasureFunction(TEXT_MEASURE_FUNCTION);
    }

    /**
     * Prepare the text {@link Spanned} for calculating text's size. This is done by setting
     * various text span to the text.
     * @see android.text.style.CharacterStyle
     */
    @Override
    public void layoutBefore() {
        hasBeenMeasured = false;
        updateStyleAndText();
        spanned = createSpanned(mText);
        super.dirty();
        super.layoutBefore();
    }

    @Override
    public void layoutAfter() {
        if (hasBeenMeasured) {
            if (layout != null && !FloatUtil.floatsEqual(getTextContentWidth(), previousWidth)) {
                recalculateLayout();
            }
        } else {
            updateStyleAndText();
            recalculateLayout();
        }
        hasBeenMeasured = false;
        if (layout != null && !layout.equals(atomicReference.get()) &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //TODO Warm up, a profile should be used to see the improvement.
            warmUpTextLayoutCache(layout);
        }
        swap();
        super.layoutAfter();
    }

    @Override
    public Layout getExtra() {
        return atomicReference.get();
    }

    @Override
    public void updateAttr(Map<String, Object> attrs) {
        swap();
        super.updateAttr(attrs);
        if (attrs.containsKey(Constants.Name.VALUE)) {
            mText = WXAttr.getValue(attrs);
        }
    }

    @Override
    public void updateStyle(Map<String, Object> styles) {
        swap();
        super.updateStyle(styles);
        updateStyleImp(styles);
    }

    @Override
    public MyViewDomObject clone() {
        MyViewDomObject dom = null;
        try {
            dom = new MyViewDomObject();
            copyFields(dom);
            dom.hasBeenMeasured = hasBeenMeasured;
            dom.atomicReference = atomicReference;
        } catch (Exception e) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e("WXTextDomObject clone error: ", e);
            }
        }
        if (dom != null) {
            dom.spanned = spanned;
        }
        return dom;
    }

    /**
     * Get the content width of the dom.
     * @return the width of the dom that excludes left-padding and right-padding.
     */
    private float getTextContentWidth() {
        float rawWidth = getLayoutWidth();
        float leftPadding, rightPadding, leftBorder, rightBorder;
        Spacing padding = getPadding();
        Spacing border = getBorder();

        if (!CSSConstants.isUndefined((leftPadding = padding.get(Spacing.LEFT)))) {
            rawWidth -= leftPadding;
        }
        if (!CSSConstants.isUndefined((rightPadding = padding.get(Spacing.RIGHT)))) {
            rawWidth -= rightPadding;
        }

        if (!CSSConstants.isUndefined(leftBorder = border.get(Spacing.LEFT))) {
            rawWidth -= leftBorder;
        }
        if (!CSSConstants.isUndefined(rightBorder = border.get(Spacing.RIGHT))) {
            rawWidth -= rightBorder;
        }
        return rawWidth;
    }

    /**
     * RecalculateLayout.
     */
    private void recalculateLayout() {
        float contentWidth = getTextContentWidth();
        if (contentWidth > 0) {
            spanned = createSpanned(mText);
            layout = createLayout(contentWidth, true, layout);
            previousWidth = layout.getWidth();
        }
    }

    /**
     * Update style and text.
     */
    private void updateStyleAndText() {
        updateStyleImp(getStyles());
        mText = WXAttr.getValue(getAttrs());
    }

    /**
     * Record the property according to the given style
     * @param style the give style.
     */
    private void updateStyleImp(Map<String, Object> style) {
        if (style != null) {
            if (style.containsKey(Constants.Name.LINES)) {
                int lines = WXStyle.getLines(style);
                if (lines > 0) {
                    mNumberOfLines = lines;
                }
            }
            if (style.containsKey(Constants.Name.FONT_SIZE)) {
                mFontSize = WXStyle.getFontSize(style);
            }
            if (style.containsKey(Constants.Name.FONT_WEIGHT)) {
                mFontWeight = WXStyle.getFontWeight(style);
            }
            if (style.containsKey(Constants.Name.FONT_STYLE)) {
                mFontStyle = WXStyle.getFontStyle(style);
            }
            if (style.containsKey(Constants.Name.COLOR)) {
                mColor = WXResourceUtils.getColor(WXStyle.getTextColor(style));
                mIsColorSet = mColor != Integer.MIN_VALUE;
            }
            if (style.containsKey(Constants.Name.TEXT_DECORATION)) {
                mTextDecoration = WXStyle.getTextDecoration(style);
            }
            if (style.containsKey(Constants.Name.FONT_FAMILY)) {
                mFontFamily = WXStyle.getFontFamily(style);
            }
            mAlignment = WXStyle.getTextAlignment(style);
            textOverflow = WXStyle.getTextOverflow(style);
            int lineHeight = WXStyle.getLineHeight(style);
            if (lineHeight != UNSET) {
                mLineHeight = lineHeight;
            }
        }
    }

    /**
     * Update layout according to {@link #mText} and span
     * @param width the specified width.
     * @param forceWidth If true, force the text width to the specified width, otherwise, text width
     *                   may equals to or be smaller than the specified width.
     * @param previousLayout the result of previous layout, could be null.
     */
    private
    @NonNull
    Layout createLayout(float width, boolean forceWidth, @Nullable Layout previousLayout) {
        float textWidth;
        textWidth = getTextWidth(mTextPaint, width, forceWidth);
        Layout layout;
        if (!FloatUtil.floatsEqual(previousWidth, textWidth) || previousLayout == null) {
            layout = new StaticLayout(spanned, mTextPaint, (int) Math.ceil(textWidth),
                    Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
        } else {
            layout = previousLayout;
        }
        if (mNumberOfLines != UNSET && mNumberOfLines > 0 && mNumberOfLines < layout.getLineCount()) {
            int lastLineStart, lastLineEnd;
            lastLineStart = layout.getLineStart(mNumberOfLines - 1);
            lastLineEnd = layout.getLineEnd(mNumberOfLines - 1);
            if (lastLineStart < lastLineEnd) {
                String text = mText.subSequence(0, lastLineStart).toString() +
                        truncate(mText.substring(lastLineStart, lastLineEnd),
                                mTextPaint, layout.getWidth(), textOverflow);
                spanned = createSpanned(text);
                return new StaticLayout(spanned, mTextPaint, (int) Math.ceil(textWidth),
                        Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
            }
        }
        return layout;
    }

    public @NonNull String truncate(@Nullable String source, @NonNull TextPaint paint,
                                    int desired, @Nullable TextUtils.TruncateAt truncateAt){
        if(!TextUtils.isEmpty(source)){
            StringBuilder builder;
            Spanned spanned;
            StaticLayout layout;
            for(int i=source.length();i>0;i--){
                builder=new StringBuilder(i+1);
                builder.append(source, 0, i);
                if(truncateAt!=null){
                    builder.append(ELLIPSIS);
                }
                spanned = createSpanned(builder.toString());
                layout = new StaticLayout(spanned, paint, desired, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
                if(layout.getLineCount()<=1){
                    return spanned.toString();
                }
            }
        }
        return "";
    }

    /**
     * Get text width according to constrain of outerWidth with and forceToDesired
     * @param textPaint paint used to measure text
     * @param outerWidth the width that css-layout desired.
     * @param forceToDesired if set true, the return value will be outerWidth, no matter what the width
     *                   of text is.
     * @return if forceToDesired is false, it will be the minimum value of the width of text and
     * outerWidth in case of outerWidth is defined, in other case, it will be outer width.
     */
    private float getTextWidth(TextPaint textPaint,float outerWidth, boolean forceToDesired) {
        float textWidth;
        if (forceToDesired) {
            textWidth = outerWidth;
        } else {
            float desiredWidth = Layout.getDesiredWidth(spanned, textPaint);
            if (CSSConstants.isUndefined(outerWidth) || desiredWidth < outerWidth) {
                textWidth = desiredWidth;
            } else {
                textWidth = outerWidth;
            }
        }
        return textWidth;
    }

    /**
     * Update {@link #spanned} according to the give charSequence and styles
     * @param text the give raw text.
     * @return an Spanned contains text and spans
     */
    private
    @NonNull
    Spanned createSpanned(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (text.contains("{b}")) {
                SpannableStringBuilder spannable = new SpannableStringBuilder(text);
                List<MyViewDomObject.SetSpanOperation> ops = createSetSpanOperation(spannable.length());
                if (mFontSize == UNSET) {
                    ops.add(new MyViewDomObject.SetSpanOperation(0, spannable.length(),
                            new AbsoluteSizeSpan(WXText.sDEFAULT_SIZE), "normal"));
                }

                ops.add(new MyViewDomObject.SetSpanOperation("boldSpanOperation"));

                Collections.reverse(ops);
                for (MyViewDomObject.SetSpanOperation op : ops) {
                    if (op.getType().equals("normal")) {
                        op.execute(spannable);
                    } else {
                        op.execute(spannable, text);
                    }
                }
                return spannable;

            } else {
                SpannableString spannable = new SpannableString(text);
                List<MyViewDomObject.SetSpanOperation> ops = createSetSpanOperation(spannable.length());
                if (mFontSize == UNSET) {
                    ops.add(new MyViewDomObject.SetSpanOperation(0, spannable.length(),
                            new AbsoluteSizeSpan(WXText.sDEFAULT_SIZE), "normal"));
                }

                Collections.reverse(ops);
                for (MyViewDomObject.SetSpanOperation op : ops) {
                    op.execute(spannable);
                }
                return spannable;
            }
        }
        return new SpannableString("");
    }

    /**
     * Create a task list which contains {@link WXTextDomObject.SetSpanOperation}. The task list will be executed
     * in other method.
     * @param end the end character of the text.
     * @return a task list which contains {@link WXTextDomObject.SetSpanOperation}.
     */
    private List<MyViewDomObject.SetSpanOperation> createSetSpanOperation(int end) {
        List<MyViewDomObject.SetSpanOperation> ops = new LinkedList<>();
        int start = 0;
        if (end >= start) {
            if (mTextDecoration == WXTextDecoration.UNDERLINE) {
                ops.add(new MyViewDomObject.SetSpanOperation(start, end,
                        new UnderlineSpan(), "normal"));
            }
            if (mTextDecoration == WXTextDecoration.LINETHROUGH) {
                ops.add(new MyViewDomObject.SetSpanOperation(start, end,
                        new StrikethroughSpan(), "normal"));
            }
            if (mIsColorSet) {
                ops.add(new MyViewDomObject.SetSpanOperation(start, end,
                        new ForegroundColorSpan(mColor), "normal"));
            }
            if (mFontSize != UNSET) {
                ops.add(new MyViewDomObject.SetSpanOperation(start, end, new AbsoluteSizeSpan(mFontSize), "normal"));
            }
            if (mFontStyle != UNSET
                    || mFontWeight != UNSET
                    || mFontFamily != null) {
                ops.add(new MyViewDomObject.SetSpanOperation(start, end,
                        new MyViewCustomStyleSpan(mFontStyle, mFontWeight, mFontFamily), "normal"));
            }
            ops.add(new MyViewDomObject.SetSpanOperation(start, end, new AlignmentSpan.Standard(mAlignment), "normal"));
            if (mLineHeight != UNSET) {
                ops.add(new MyViewDomObject.SetSpanOperation(start, end, new WXLineHeightSpan(mLineHeight), "normal"));
            }
        }
        return ops;
    }

    /**
     * Move the reference of current layout to the {@link AtomicReference} for further use,
     * then clear current layout.
     */
    private void swap() {
        if (layout != null) {
            atomicReference.set(layout);
            layout = null;
            mTextPaint = new TextPaint(mTextPaint);
        }
    }

    /**
     * As warming up TextLayoutCache done in the DOM thread may manipulate UI operation,
     there may be some exception, in which case the exception is ignored. After all,
     this is just a warm up operation.
     * @return false for warm up failure, otherwise returns true.
     */
    private boolean warmUpTextLayoutCache(Layout layout) {
        boolean result;
        try {
            layout.draw(DUMMY_CANVAS);
            result = true;
        } catch (Exception e) {
            WXLogUtils.eTag(TAG, e);
            result = false;
        }
        return result;
    }
}
