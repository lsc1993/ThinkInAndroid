package com.ls.customview.mentionedittext;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

import com.ls.customview.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 统一处理各种Tag的显示与交互
 * 输入文字时自动识别 @ # 标志的文字,并且高亮显示
 * @ 表示识别用户  # 表示识别话题
 * 支持一次删除高亮文字
 */
public class MentionEditText extends AppCompatEditText {

    public static final String AT_USER_MENTION_TAG = "@";
    public static final String AT_USER_MENTION_PATTERN = "@[\\S\\-]+";  //@的正则表达式
    public static final String HASH_TAG_MENTION_TAG = "#";
    public static final String HASH_TAG_MENTION_PATTERN = "#[\\S\\-]+"; //#的正则表达式

    private Map<String, Pattern> mPatternMap = new HashMap<>();

    private HashMap<String, Integer> mentionTagColor = new HashMap<>(); //每个Tag对应Span的颜色
    private HashSet<String> selectionStyleTag = new HashSet<>(); //Tag是否支持整体删除效果
    private HashSet<String> editableTag = new HashSet<>(); //可以被编辑的Tag

    private boolean mIsSelected;
    private Range mLastSelectedRange; //最后一个被选择的Tag
    private List<Range> mRangeArrayList; //记录所有Tag的位置信息
    private HashMap<String, List<String>> tagOfTexts = new HashMap<>(); // 存储对应Tag的文本 #hello
    private HashMap<String, List<String>> tagOfTextsNoFlag = new HashMap<>(); // 存储对应Tag的文本，不带tag hello

    private int maxLength = -1;

    private List<String> atUserNames;

    private OnMentionInputListener mOnMentionInputListener;

    private Handler handler;

    private Runnable colorMentionRunnable;

    public MentionEditText(Context context) {
        super(context);
        init();
    }

    public MentionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MentionEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mRangeArrayList = new ArrayList<>();
        atUserNames = new ArrayList<>();
        setPattern(AT_USER_MENTION_TAG, AT_USER_MENTION_PATTERN);
        setPattern(HASH_TAG_MENTION_TAG, HASH_TAG_MENTION_PATTERN);
        // 设置默认颜色
        setMentionTagColor(AT_USER_MENTION_TAG, getResources().getColor(R.color.design_default_color_error));
        setMentionTagColor(HASH_TAG_MENTION_TAG, getResources().getColor(R.color.design_default_color_on_primary));
        //设置可以整体删除的tag
        selectionStyleTag.add(AT_USER_MENTION_TAG);
        //设置可以编辑的Tag
        editableTag.add(HASH_TAG_MENTION_TAG);
        handler = new Handler(Looper.getMainLooper());
        colorMentionRunnable = new Runnable() {
            @Override
            public void run() {
                colorMentionString();
            }
        };
        //setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        addTextChangedListener(new MentionTextWatcher());
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (mLastSelectedRange != null && mLastSelectedRange.isEqual(selStart, selEnd)) {
            return;
        }

        //如果用户点击取消了选中效果，重置选中状态
        Range closestRange = getRangeOfClosestMentionString(selStart, selEnd);
        if (closestRange != null && closestRange.to == selEnd) {
            mIsSelected = false;
        }

        //这里获取选中位置是否包含了Tag
        Range nearbyRange = getRangeOfNearbyMentionString(selStart, selEnd);
        if (nearbyRange == null) {
            return;
        }

        //selStart==selEnd 说明光标在Tag之中，如果禁止编辑，将光标移动到Tag的头部
        if (selStart == selEnd && !editableTag.contains(nearbyRange.tag)) {
            setSelection(nearbyRange.getAnchorPosition(selStart));
        }
    }

    /**
     * 设置Tag的正则表达式
     *
     * @param tag Tag
     * @param pattern 正则表达式
     */
    public void setPattern(String tag, String pattern) {
        mPatternMap.put(tag, Pattern.compile(pattern));
    }

    /**
     * 设置Tag对应Span的颜色
     *
     * @param tag Tag
     * @param color 颜色
     */
    public void setMentionTagColor(String tag, int color) {
        mentionTagColor.put(tag, color);
    }

    /**
     * 获取对应Tag的文本
     *
     * @param tag tag
     */
    public List<String> getMentionList(String tag) {
        if (tagOfTexts.get(tag) == null) {
            return new ArrayList<>();
        }
        return tagOfTexts.get(tag);
    }
    /**
     * 获取对应Tag的文本，不带Tag
     *
     * @param tag tag
     */
    public List<String> getMentionListNoTag(String tag) {
        if (tagOfTextsNoFlag.get(tag) == null) {
            return new ArrayList<>();
        }
        return tagOfTextsNoFlag.get(tag);
    }


    /**
     * 刷新text的状态
     */
    public void refreshMention() {
        colorMentionString();
    }

    /**
     * 当内容改变时，重新计算各个Tag的位置信息
     */
    private void colorMentionString() {
        //重置状态
        mIsSelected = false;
        if (mRangeArrayList != null) {
            mRangeArrayList.clear();
        }

        Editable spannableText = getText();
        if (spannableText == null || TextUtils.isEmpty(spannableText.toString())) {
            return;
        }

        //删除老的Span
        ForegroundColorSpan[] oldSpans = spannableText.getSpans(0, spannableText.length(), ForegroundColorSpan.class);
        for (ForegroundColorSpan oldSpan : oldSpans) {
            spannableText.removeSpan(oldSpan);
        }

        //重新计算各个Tag的位置信息
        String text = spannableText.toString();
        for (Map.Entry<String, Pattern> entry : mPatternMap.entrySet()) {
            int lastMentionIndex = -1;
            Matcher matcher = entry.getValue().matcher(text);
            String tag = entry.getKey();
            int spanColor = mentionTagColor.get(tag);
            List<String> tagOfText = new ArrayList<>();
            tagOfTexts.put(tag, tagOfText);
            List<String> tagOfTextNoFlag = new ArrayList<>();
            tagOfTextsNoFlag.put(tag, tagOfTextNoFlag);
            // 通过正则表达式匹配各个Tag
            while (matcher.find()) {
                String mentionText = matcher.group();
                tagOfText.add(mentionText);
                if (mentionText.length() > 1) {
                    tagOfTextNoFlag.add(mentionText.substring(1));
                }
                int start;
                if (lastMentionIndex != -1) {
                    start = text.indexOf(mentionText, lastMentionIndex);
                } else {
                    start = text.indexOf(mentionText);
                }
                int end = start + mentionText.length();
                if (AT_USER_MENTION_TAG.equals(tag) && start > -1) {
                    //处理@用户名称存在空格的情况
                    for (String atUserName : atUserNames) {
                        end = start + atUserName.length() + 1;
                        if (end > text.length()) {
                            //这里防止匹配用户名称时导致长度溢出的问题
                            end = start + mentionText.length();
                            continue;
                        }
                        if (atUserName.equals(text.substring(start + 1, end))) {
                            break;
                        } else {
                            end = start + mentionText.length();
                        }
                    }
                }

                // 这里解决名称内含有@符号的情况
                if (start > -1) {
                    StyleSpan tagSpan = new StyleSpan(android.graphics.Typeface.BOLD);
                    spannableText.setSpan(tagSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(spanColor);
                    spannableText.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mRangeArrayList.add(new Range(start, end, tag));
                }
                lastMentionIndex = end;
            }
        }
    }

    private Range getRangeOfClosestMentionString(int selStart, int selEnd) {
        if (mRangeArrayList == null) {
            return null;
        }
        for (Range range : mRangeArrayList) {
            if (range.contains(selStart, selEnd)) {
                return range;
            }
        }
        return null;
    }

    private Range getRangeOfNearbyMentionString(int selStart, int selEnd) {
        if (mRangeArrayList == null) {
            return null;
        }
        for (Range range : mRangeArrayList) {
            if (range.isWrappedBy(selStart, selEnd)) {
                return range;
            }
        }
        return null;
    }

    private class MentionTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int index, int i1, int count) {
            if (count == 1 && !TextUtils.isEmpty(charSequence)) {
                char tagChar = charSequence.toString().charAt(index);
                for (Map.Entry<String, Pattern> entry : mPatternMap.entrySet()) {
                    if (entry.getKey().equals(String.valueOf(tagChar)) && mOnMentionInputListener != null) {
                        mOnMentionInputListener.onMentionTagInput(entry.getKey());
                        break;
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            handler.removeCallbacks(colorMentionRunnable);
            handler.postDelayed(colorMentionRunnable, 300);
        }
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        return new MentionInputConnection(super.onCreateInputConnection(outAttrs), true, this);
    }

    private class MentionInputConnection extends InputConnectionWrapper {
        private EditText editText;

        MentionInputConnection(InputConnection target, boolean mutable, MentionEditText editText) {
            super(target, mutable);
            this.editText = editText;
        }

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            if (maxLength != -1) {
                CharSequence curText = getText();
                int curTextLen = MentionEditText.this.getText().length();
                int inputTextLen = text.length();
                if (curTextLen + inputTextLen > maxLength) {
                    if (curTextLen > maxLength) {
                        setText(curText.subSequence(0, maxLength));
                        text = "";
                    } else {
                        text = text.subSequence(0, curTextLen + inputTextLen - maxLength);
                    }
                }
            }
            return super.commitText(text, newCursorPosition);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                int selectionStart = editText.getSelectionStart();
                int selectionEnd = editText.getSelectionEnd();
                Range closestRange = getRangeOfClosestMentionString(selectionStart, selectionEnd);
                if (closestRange == null) {
                    mIsSelected = false;
                    return super.sendKeyEvent(event);
                }
                //直接删除字符
                if (mIsSelected || selectionStart == closestRange.from) {
                    mIsSelected = false;
                    return super.sendKeyEvent(event);
                } else {
                    //这里加一个字符串选中效果，实现整体删除功能
                    mIsSelected = true;
                    mLastSelectedRange = closestRange;
                    if (selectionStyleTag.contains(closestRange.tag)) {
                        setSelection(closestRange.to, closestRange.from);
                    } else {
                        //直接删除字符
                        return super.sendKeyEvent(event);
                    }
                }
                return true;
            }
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    /**
     * 用于记录各个Tag字符串的位置信息以及Tag标志
     */
    private class Range {
        int from;
        int to;
        String tag;

        Range(int from, int to, String tag) {
            this.from = from;
            this.to = to;
            this.tag = tag;
        }

        /**
         * 是否在包含了字符串
         *
         * @param start 开始位置
         * @param end 结束位置
         * @return true 包含 false 不包含
         */
        boolean isWrappedBy(int start, int end) {
            return (start > from && start < to) || (end > from && end < to);
        }

        /**
         * 是否在字符串之中
         *
         * @param start 开始位置
         * @param end 结束位置
         * @return true 是 false 不是
         */
        boolean contains(int start, int end) {
            return from <= start && to >= end;
        }

        boolean isEqual(int start, int end) {
            return (from == start && to == end) || (from == end && to == start);
        }

        int getAnchorPosition(int value) {
            if ((value - from) - (to - value) >= 0) {
                return to;
            } else {
                return from;
            }
        }
    }

    public void addAtUserName(String atUserName) {
        atUserNames.clear();
        this.atUserNames.add(atUserName);
    }

    public void setMaxLength(int maxLength) {
        if (maxLength < 0) {
            throw new IllegalArgumentException("max length must big than zero!");
        }
        this.maxLength = maxLength;
    }

    /**
     * 设置Tag输入回调
     *
     * @param onMentionInputListener MentionEditText.OnMentionInputListener
     */
    public void setOnMentionInputListener(OnMentionInputListener onMentionInputListener) {
        mOnMentionInputListener = onMentionInputListener;
    }

    public interface OnMentionInputListener {
        /**
         * 当用户输入包含Tag的字符时的回调
         */
        void onMentionTagInput(String tag);
    }
}

