package com.zgw.company.base.core.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Layout
import android.text.SpannableString
import android.text.Spanned
import android.text.style.*
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.safframework.ext.TAG
import java.lang.reflect.Type
import java.time.format.TextStyle

class MySpinnerString private constructor(private val s: String) {

    private val spannableString: SpannableString = SpannableString(s)
    //不包含左右两端(a,b)
    val notIncludeRightAndLeft = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE

    //(a,b]
    val includeRightAndNotIncludeLeft = Spanned.SPAN_EXCLUSIVE_INCLUSIVE

    //[a,b)
    val notIncludeRightAndIncludeLeft = Spanned.SPAN_INCLUSIVE_EXCLUSIVE

    //[a,b]
    val includeRightAndLeft = Spanned.SPAN_INCLUSIVE_INCLUSIVE

    //粗体
    val bold = Typeface.BOLD
    //正常
    val normal = Typeface.NORMAL
    //斜体
    val italic = Typeface.ITALIC
    //加粗斜体
    val boldItalic = Typeface.BOLD_ITALIC

    //下方
    val bottom = ImageSpan.ALIGN_BOTTOM
    //和baselin对齐
    val baseline = ImageSpan.ALIGN_BASELINE
    //中间
    val center = ImageSpan.ALIGN_CENTER

    val monospace = "monospace"
    val serif = "serif"
    val sansSerif = "sans-serif"

    val alignNormal = Layout.Alignment.ALIGN_NORMAL
    val alignOpposite = Layout.Alignment.ALIGN_OPPOSITE
    val alignCenter = Layout.Alignment.ALIGN_CENTER

    /**
     * 设置文字前景色
     * @param context 上下文
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param color 颜色(只支持资源颜色，例如：R.color.aaa)
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun foregroundColor(
        context: Context,
        start: Any,
        end: Any,
        @ColorRes color: Int,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)
        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }
        spannableString.setSpan(ForegroundColorSpan(context.getColor(color)), starts, ends, flags)
        return this
    }

    /**
     * 设置文字背景色
     * @param context 上下文
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param color 颜色(只支持资源颜色，例如：R.color.aaa)
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun backgroundColor(
        context: Context,
        start: Any,
        end: Any,
        @ColorRes color: Int,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(BackgroundColorSpan(context.getColor(color)), starts, ends, flags)
        return this
    }

    /**
     * 为文本设置相对大小(在原来的文字大小基础上，相对设置文字大小(等比缩放))
     *
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     *
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun relativeSize(
        start: Any,
        end: Any,
        size: Float,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(RelativeSizeSpan(size), starts, ends, flags)
        return this
    }

    /**
     * 设置中划线(删除线)
     *
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     *
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun strikeThrough(
        start: Any,
        end: Any,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(StrikethroughSpan(), starts, ends, flags)
        return this
    }

    /**
     * 设置下划线
     *
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     *
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun underline(
        start: Any,
        end: Any,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(UnderlineSpan(), starts, ends, flags)
        return this
    }

    /**
     * 为文本设置脚标
     *
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     *
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun subscript(
        start: Any,
        end: Any,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(SubscriptSpan(), starts, ends, flags)
        return this
    }

    /**
     * 设置上标
     *
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     *
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun superScript(
        start: Any,
        end: Any,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(SuperscriptSpan(), starts, ends, flags)
        return this
    }

    /**
     * 文字设置样式（正常、粗体、斜体、粗斜体）
     *
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param style 字体样式，此类已实现
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun textStyle(
        start: Any,
        end: Any,
        style:Int,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(StyleSpan(style), starts, ends, flags)
        return this
    }

    /**
     * 文本插入图片
     * @param context
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param picture 图片
     * @param vertical 图片位置，此类已实现
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun image(
        context: Context,
        start: Any,
        end: Any,
        picture:Any,
        vertical:Int,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        var image:ImageSpan? = null
        if (vertical != -1){
            if (picture is Bitmap) ImageSpan(context,picture,vertical)

            if (picture is Drawable) ImageSpan(picture,vertical)

            if (picture is Uri) ImageSpan(context,picture,vertical)

            if (picture is Int) ImageSpan(context,picture,vertical)

        }else{
            if (picture is Bitmap) ImageSpan(context,picture)

            if (picture is Drawable) ImageSpan(picture)

            if (picture is Uri) ImageSpan(context,picture)

            if (picture is Int) ImageSpan(context,picture)

        }
        spannableString.setSpan(image, starts, ends, flags)
        return this
    }

    /**
     * 文字可点击
     *
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param clickable 实现了ClickableSpan的类
     *
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun textStyle(
        start: Any,
        end: Any,
        clickable:ClickableSpan,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(clickable, starts, ends, flags)
        return this
    }

    /**
     * 点击文字，可以打开一个URL
     *
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param url 需要打开的url
     *
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun url(
        start: Any,
        end: Any,
        url:String,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(URLSpan(url), starts, ends, flags)
        return this
    }

    /**
     * 设置文字字体
     *
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param typeface 字体，此类已实现
     *
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun typeface(
        start: Any,
        end: Any,
        typeface:String,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(TypefaceSpan(typeface), starts, ends, flags)
        return this
    }

    /**
     * 每行的MarginLeft的偏移量（跟 \t 和 \n 有关系）
     *
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param standard 偏移量
     *
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun tabStop(
        start: Any,
        end: Any,
        standard:Int,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(TabStopSpan.Standard(standard), starts, ends, flags)
        return this
    }

    /**
     * 文字横向缩放
     *
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param scale 缩放比例
     *
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun scaleX(
        start: Any,
        end: Any,
        scale:Float,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(ScaleXSpan(scale), starts, ends, flags)
        return this
    }

    /**
     * 设置文字左侧显示引用样式（一条竖线）
     * @param context
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param color 左划线的颜色
     *
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun quote(
        context: Context,
        start: Any,
        end: Any,
        @ColorRes color:Int,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(QuoteSpan(context.getColor(color)), starts, ends, flags)
        return this
    }

    /**
     * 设置文字模糊效果和浮雕效果
     *
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param filter 效果，请实现，有两种，一种是BlurMaskFilter:模糊效果,一种是EmbossMaskFilter:浮雕效果
     *
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun textStyle(
        start: Any,
        end: Any,
        filter:MaskFilter,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(MaskFilterSpan(filter), starts, ends, flags)
        return this
    }

    /**
     * 设置文本缩进
     *
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param first 首行缩进的偏移量
     * @param rest 其他行的偏移量
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun leadingMargin(
        start: Any,
        end: Any,
        first:Int,
        rest:Int,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(LeadingMarginSpan.Standard(first,rest), starts, ends, flags)
        return this
    }

    /**
     * 文本插入图片+Margin
     *
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param bitmap 图片
     * @param padding 和图片的间距
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun iconMargin(
        start: Int,
        end: Int,
        bitmap:Bitmap,
        padding:Int,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(IconMarginSpan(bitmap,padding), starts, ends, flags)
        return this
    }

    /**
     * 文本插入图片+Margin
     * @param context
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param bitmap 图片资源文件
     * @param padding 和图片的间距
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun drawableMargin(
        context: Context,
        start: Any,
        end: Any,
        @DrawableRes bitmap:Int,
        padding:Int,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(DrawableMarginSpan(context.resources.getDrawable(bitmap,null),padding), starts, ends, flags)
        return this
    }

    /**
     * 类似于HTML中的<li>标签的圆点效果
     *
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param gapWidth 圆点与文本的间距
     * @param color 圆点颜色
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun drawableMargin(
        start: Any,
        end: Any,
        gapWidth:Int,
        color:Int,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(BulletSpan(gapWidth,color), starts, ends, flags)
        return this
    }

    /**
     * 设置文字对齐方式
     *
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param align 对齐方式，此类已实现
     *
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun alignment(
        start: Any,
        end: Any,
        align:Layout.Alignment,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(AlignmentSpan.Standard(align), starts, ends, flags)
        return this
    }

    /**
     * 设置文字绝对大小
     *
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param size 默认单位为px
     * @param dip true为size的单位是dip，false为px
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun absoluteSize(
        start: Any,
        end: Any,
        size:Int,
        dip:Boolean,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(AbsoluteSizeSpan(size,dip), starts, ends, flags)
        return this
    }

    /**
     * 设置文字字体、文字样式（粗体、斜体、等等）、文字颜色状态、文字下划线颜色状态等等
     * @param context
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param appearance 样式
     * @param colorList 颜色列表
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun textAppearance(
        context: Context,
        appearance:Int,
        colorList:Int,
        start: Any,
        end: Any,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(TextAppearanceSpan(context,appearance,colorList), starts, ends, flags)
        return this
    }

    /**
     * 设置文字字体、文字样式（粗体、斜体、等等）、文字颜色状态、文字下划线颜色状态等等
     * @param family 字体，此类已实现详见monospace serif sans-serif变量
     * @param start 开始的位置(此处支持char，string，int)
     * @param end 结束位置(同上)
     * @param style 样式，详见，bold，normal，italic，boldItalic变量，主要有Typeface.NORMAL Typeface.BOLD Typeface.ITALIC Typeface.BOLD_ITALIC
     * @param size 字体大小
     * @param color 字体颜色状态
     * @param linkColor 链接字体颜色状态
     * @param flags 前后包含关系，此类已经实现直接调用
     */
    fun drawableMargin(
        start: Any,
        end: Any,
        family:String,
        style:Int,
        size:Int,
        color: ColorStateList,
        linkColor:ColorStateList,
        flags: Int
    ): MySpinnerString {
        val starts:Int = getStartOrEndLocation(start)
        val ends:Int = getStartOrEndLocation(end)

        if (starts == -1){
            return this
        }

        if (ends == -1){
            return this
        }

        spannableString.setSpan(TextAppearanceSpan(family,style, size, color, linkColor), starts, ends, flags)
        return this
    }

    private fun getStartOrEndLocation(startOrEnd : Any) : Int{
        if (startOrEnd is Int){
            return startOrEnd
        }

        if (startOrEnd is Char){
            return s.indexOf(startOrEnd)
        }

        if (startOrEnd is String){
            return s.indexOf(startOrEnd)
        }

        return -1
    }

    companion object{

        private var mySpinnerString:MySpinnerString? = null
        fun getInstance(s:String):MySpinnerString?{
            if (mySpinnerString == null){
                synchronized(MySpinnerString::class){
                    if (mySpinnerString == null){
                        mySpinnerString = MySpinnerString(s)
                    }
                }
            }

            return mySpinnerString
        }
    }
}