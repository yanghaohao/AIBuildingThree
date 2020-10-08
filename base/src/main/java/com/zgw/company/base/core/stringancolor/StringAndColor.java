package com.zgw.company.base.core.stringancolor;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

public class StringAndColor {
    private static SpannableString spannableString;
    private static StringAndColor stringAndColor;

    private StringAndColor(String s){
        spannableString = new SpannableString(s);
    }

    public static StringAndColor getInstance(String s){
        if (stringAndColor == null){
            synchronized (StringAndColor.class){
                if (stringAndColor == null){
                    stringAndColor =  new StringAndColor(s);
                }
            }
        }

        return stringAndColor;
    }

    public SpannableString foregroundColor(int start,int end,int color){
        spannableString.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public SpannableString color(int start,int end,int color){
        spannableString.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
