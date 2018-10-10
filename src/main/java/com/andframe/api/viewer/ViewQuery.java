package com.andframe.api.viewer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.andframe.impl.viewer.AfViewQuery;

import java.io.File;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

/**
 * 安卓版 JQuery 接口
 * Created by SCWANG on 2016/8/18.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface ViewQuery<T extends ViewQuery<T>> {

    //<editor-fold desc="选择器">

    //<editor-fold desc="基本选择">
    /**
     * 根据 控件Id 选择一个或多个 View
     */
    T query(@IdRes Integer id, @IdRes int... ids);
    /**
     * 根据 控件Id名 选择一个或多个 View
     */
    T query(String idValue, String... idValues);
    /**
     * 根据 控件类型 选择一个或多个 View
     */
    T query(Class<? extends View> type);
    /**
     * 根据 控件类型 选择一个或多个 View
     */
    T query(Class<? extends View>[] types);
    /**
     * 直接制定选择的目标 View
     */
    T with(View... views);
    /**
     * 直接制定选择的目标 View
     */
    T with(Collection<View> views);
    //</editor-fold>

    //<editor-fold desc="选择变换">

    /**
     * 如果当前选中 View ，并且 View 存在于父容器中，toPrev() 将会把选择对象转移到前一个 View
     * 如果 View 已经是第一个 View， 将会丢失选择对象（没有选择对象）
     */
    T toPrev();
    /**
     * 如果当前选中 View ，并且 View 存在于父容器中，toNext() 将会把选择对象转移到下一个 View
     * 如果 View 已经是最后的 View， 将会丢失选择对象（没有选择对象）
     */
    T toNext();
    /**
     * 如果当前选中 View ，并且 View 存在子 View，toChild() 将会把选择对象按照指定 index 转向子View
     * 如果 View 没有子View 或者 index 越界，将会丢失选择对象（没有选择对象）
     * @param index 如果不传 默认0 如果 传多个只去第一个
     */
    T toChild(int... index);
    /**
     * 如果当前选中 View ，并且 View 存在子 View，toChildren() 将会把选择对象转向一个或者多个子 View
     * 如果 View 没有子View 或者 index 越界，将会丢失选择对象（没有选择对象）
     */
    T toChildren();
    /**
     * 递归树
     * 如果当前选中 View ，并且 View 存在子 View，toChildrenTree() 将会把选择对象转向一个或者多个子 View 及其递归子 View
     * 如果 View 没有子View 或者 index 越界，将会丢失选择对象（没有选择对象）
     */
    T toChildrenTree();
    /**
     * 如果当前选中 View ，存在与父容器中， toParent() 将会把选择对象转向父容器
     * 如果 View 没有父容器，将会丢失选择对象（没有选择对象）
     */
    T toParent();
    /**
     * 直接把选择对象转向查询器持有的最根（root）视图 View
     */
    T toRoot();
    //</editor-fold>

    //<editor-fold desc="合并选择">
    /**
     * 将新的 views 添加到选择对象中
     */
    T mixView(View... views);
    /**
     * 将当前选择 View 的前一个View 添加到选择对象中
     * @see ViewQuery#toPrev()
     */
    T mixPrev();
    /**
     * 将当前选择 View 的下一个View 添加到选择对象中
     * @see ViewQuery#toNext()
     */
    T mixNext();
    /**
     * 将当前选择 View 指定子View 添加到选择对象中
     * @see ViewQuery#toChild(int...)
     */
    T mixChild(int... index);
    /**
     * 将当前选择 View 所有子View 添加到选择对象中
     * @see ViewQuery#toChildren()
     */
    T mixChildren();
    /**
     * 将当前选择 View 所有子View(递归) 添加到选择对象中
     * @see ViewQuery#toChildren()
     */
    T mixChildrenTree();
    /**
     * 将当前选择 View 的父容器 添加到选择对象中
     * @see ViewQuery#toParent()
     */
    T mixParent();

    //</editor-fold>

    //<editor-fold desc="获取选择">
    /**
     * 判断 $ 选择器是否成选择了 View
     */
    boolean exist();
    /**
     * 获取查询结果的个数
     */
    int queryCount();
    /**
     * 获取所有选择的 View
     */
    View[] views();
    /**
     * 获取选择的 View （默认第一个）
     * @param index 可以指定选择的索引
     */
    View getView(int... index);

    /**
     * 获取选择其的 root Viewer
     */
    Viewer rootViewer();

    /**
     * 设置新的 root Viewer
     * @param viewer new Viewer
     * @return old Viewer
     */
    Viewer rootViewer(@NonNull Viewer viewer);

    /**
     * 获取选择的 View （默认第一个） （模板返回）
     * @param index 可以指定选择的索引
     */
    @Nullable
    <TT extends View> TT view(int... index);
    /**
     * 根据类型 获取选择的 View （默认第一个） （模板返回）
     */
    <TT extends View> TT[] views(Class<TT> clazz, TT[] tt);
    /**
     * 根据类型 获取选择的 View （默认第一个） （模板返回）
     */
    @Nullable
    <TT extends View> TT view(Class<TT> clazz ,int... index);
    //</editor-fold>

    //<editor-fold desc="选择遍历">
    T foreach(ViewIterator<View> iterator);
    <TT> T foreach(Class<TT> clazz, ViewIterator<TT> iterator);
    <TTT> TTT foreach(ViewReturnIterator<View,TTT> iterator);
    <TT,TTT> TTT foreach(Class<TT> clazz, ViewReturnIterator<TT,TTT> iterator);
    <TT,TTT> TTT foreach(Class<TT> clazz, ViewReturnIterator<TT,TTT> iterator, TTT defValue);
    //</editor-fold>

    //<editor-fold desc="缓存设置">
    /**
     * 设置是否开启缓存
     */
    T cacheIdEnable(boolean enable);
    /**
     * 清除缓存
     */
    T clearIdCache();
    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="所有View">

    //<editor-fold desc="基本设置">
    /**
     * 设置当前选中 View 的 Id
     */
    T id(int id);

    /**
     * 设置当前选中 View 的 tag.
     */
    T tag(Object tag);

    /**
     * 设置当前选中 View 的 key tag.
     */
    T tag(int key, Object tag);

    /**
     * 设置当前选中 View 的显示状态（visibility）, 例如 View.VISIBLE.
     */
    T visibility(int visibility);

    /**
     * @param enabled 状态
     * 设置当前选中 View 的 enabled 状态.
     */
    T enabled(boolean enabled);

    /**
     * @param focusable 是否可获得焦点
     * 设置当前选中 View 的 focusable 是否可获得焦点状态.
     */
    T focusable(boolean focusable);

    /**
     * @param focusable 是否可获得焦点
     * 设置当前选中 View 的 focusable 是否可获得焦点状态.
     */
    T focusable(int focusable);
    /**
     * Convenience for {@link android.widget.EditText#requestFocus}.
     */
    T requestFocus();
    /**
     * Convenience for {@link android.widget.EditText#clearFocus}.
     */
    T clearFocus();
    /**
     * 设置当前选中 View 的 可点击（clickable） 状态.
     */
    T clickable(boolean clickable);

    /**
     * 设置当前选中 View 的背景（background）.
     * @param id 背景资源Id
     */
    T background(@DrawableRes int id);

    /**
     * 设置当前选中 View 的背景（background）.
     * @param drawable Drawable背景资源对象
     */
    T background(Drawable drawable);

    /**
     * 设置当前选中 View 的背景颜色（backgroundColor）.
     * @param color ARGB格式的颜色值
     */
    T backgroundColor(int color);

    /**
     * 设置当前选中 View 动画
     */
    T animation(Animation animation);

    /**
     * 设置当前选中 View 动画 (并启动)
     */
    T startAnimation(Animation animation);

    /**
     * 获取当前选中 View 的 LayoutParams
     */
    T layoutParams(LayoutParams params);

    /**
     * 设置布局的 layout_gravity
     */
    T layoutGravity(int gravity);

    /**
     * 设置布局的 按键监听器
     */
    T onKey(View.OnKeyListener listener);

    /**
     * 设置阴影
     */
    T elevation(float dp);
    //</editor-fold>

    //<editor-fold desc="基本获取">

    /**
     * 获取当前选中 View 的 Id
     */
    int id();

    /**
     * 获取当前选中 View 的 tag
     */
    Object tag();

    /**
     * 获取当前选中 View 的 tag. 根据 key 获取
     * @param key setTag 时指定的 key
     */
    Object tag(int key);

    /**
     * 获取当前选中 View 的 LayoutParams
     */
    LayoutParams layoutParams();

    /**
     * 获取当前选中 View 的 阴影
     */
    float elevation();

    /**
     * 获取当前选中 View 的 可点击（clickable） 状态.
     */
    boolean clickable();

    /**
     * 获取当前选中 View 的 背景
     */
    Drawable background();

    //</editor-fold>

    //<editor-fold desc="扩展设置">

    /**
     * 设置当前选中 View 的显示状态（visibility）为 关闭（View.GONE）.
     */
    T gone();

    /**
     * 设置当前选中 View 的显示状态（visibility）为 隐藏（View.INVISIBLE）.
     */
    T invisible();

    /**
     * 设置当前选中 View 的显示状态（visibility）为 显示（View.VISIBLE）.
     */
    T visible();

    /**
     * 使用 boolean 设置当前选中 View 的显示
     * @param gone true->GONE false->VISIBLE
     */
    T gone(boolean gone);

    /**
     * 使用 boolean 设置当前选中 View 的显示
     * @param isvisibe true->VISIBLE false->GONE
     */
    T visible(boolean isvisibe);

    /**
     * 使用 boolean 设置当前选中 View 的显示
     * @param invisible true->INVISIBLE false->VISIBLE
     */
    T invisible(boolean invisible);

    //</editor-fold>

    //<editor-fold desc="扩展获取">

    /**
     * 获取当前选中 View 是否显示
     */
    boolean isVisible();

    /**
     * 测量试图的尺寸
     */
    Point measure();

    /**
     * 获取当前选中 View 的外边距（margin）
     */
    Rect margin();

    /**
     * 获取当前选中 View 的内边距（padding）
     */
    Rect padding();

    /**
     * 获取当前选中View 的屏幕截图
     */
    Bitmap screenshot();

    //</editor-fold>

    //<editor-fold desc="边距尺寸">

    //<editor-fold desc="尺寸设置">
    /**
     * 设置当前选中 View 的宽度.
     * 也可以使用 FILL_PARENT , WRAP_CONTENT , MATCH_PARENT.
     */
    T width(int px);
    T width(float dp);
    /**
     * 设置当前选中 View 的最小宽度.
     */
    T minWidth(int px);
    T minWidth(float dp);
    /**
     * 设置当前选中 View 的高度.
     * 也可以使用 FILL_PARENT , WRAP_CONTENT , MATCH_PARENT.
     */
    T height(int px);
    T height(float dp);
    /**
     * 设置当前选中 View 的最小高度.
     */
    T minHeight(int px);
    T minHeight(float dp);
    /**
     * 设置当前选中 View 的尺寸（宽和高）. 以 px 为单位.
     * 也可以使用 FILL_PARENT , WRAP_CONTENT , MATCH_PARENT.
     */
    T size(int widthPx, int heightPx);
    T size(float widthDp, float heightDp);

    //</editor-fold>

    //<editor-fold desc="尺寸获取">
    /**
     * 获取当前选中 View 的宽度.
     */
    int width();
    /**
     * 获取当前选中 View 的高度.
     */
    int height();
    /**
     * 获取当前选中 View 的最小宽度.
     */
    @TargetApi(16)
    int minWidth();
    /**
     * 获取当前选中 View 的最小高度.
     */
    @TargetApi(16)
    int minHeight();

    //</editor-fold>

    //<editor-fold desc="内边距写">

    T padding(int px);
    T padding(float dp);
    T paddingRes(@DimenRes int dimen);

    T padding(Rect rectPx);
    T padding(RectF rectDp);

    T paddingLeft(int px) ;
    T paddingRight(int px);
    T paddingTop(int px);
    T paddingBottom(int px);

    T paddingLeftRes(@DimenRes int dimen);
    T paddingRightRes(@DimenRes int dimen);
    T paddingTopRes(@DimenRes int dimen);
    T paddingBottomRes(@DimenRes int dimen);

    T paddingLeft(float dp);
    T paddingRight(float dp);
    T paddingTop(float dp);
    T paddingBottom(float dp);

    T padding(int leftPx, int topPx, int rightPx, int bottomPx);
    T padding(float leftDp, float topDp, float rightDp, float bottomDp);
    T paddingRes(@DimenRes int left, @DimenRes int top, @DimenRes int right, @DimenRes int bottom);

    //</editor-fold>

    //<editor-fold desc="内边距读">

    int paddingLeft() ;
    int paddingRight();
    int paddingTop();
    int paddingBottom();

    //</editor-fold>

    //<editor-fold desc="外边距写">

    T margin(int px);
    T margin(float dp);

    T margin(Rect rectPx);
    T margin(RectF rectDp);

    T marginLeft(int px);
    T marginRight(int px);
    T marginTop(int px);
    T marginBottom(int px);

    T marginLeft(float dp);
    T marginRight(float dp);
    T marginTop(float dp);
    T marginBottom(float dp);

    T margin(int leftPx, int topPx, int rightPx, int bottomPx);
    T margin(float leftDp, float topDp, float rightDp, float bottomDp);

    //</editor-fold>

    //<editor-fold desc="外边距读">

    int marginLeft() ;
    int marginRight();
    int marginTop();
    int marginBottom();

    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="视图变换">

    //<editor-fold desc="获取属性">
    /**
     * @see View#getAlpha()
     */
    float alpha();
    /**
     * @see View#getScaleX()
     */
    float scaleX();
    /**
     * @see View#getScaleY()
     */
    float getScaleY();
    /**
     * @see View#getRotationX()
     */
    float rotationX();
    /**
     * @see View#getRotationY
     */
    float rotationY();
    /**
     * @see View#getRotation()
     */
    float rotation();

    int x();
    int y();
    int z();
    int scrollX();
    int scrollY();
    int translationX();
    int translationY();
    int translationZ();
    int pivotX();
    int pivotY();

    //</editor-fold>

    //<editor-fold desc="设置属性">
    /**
     * @see View#setAlpha(float)
     */
    T alpha(float a);
    /**
     * @see View#setScaleX(float)
     */
    T scaleX(float scale);
    /**
     * @see View#setScaleY(float)
     */
    T scaleY(float scale);
    /**
     * @see View#setRotationX(float)
     */
    T rotationX(float rotation);
    /**
     * @see View#setRotationY(float)
     */
    T rotationY(float rotation);
    /**
     * @see View#setRotation(float)
     */
    T rotation(float rotation);

    T x(int px);
    T y(int px);
    T z(int px);
    T scrollX(int px);
    T scrollY(int px);
    T translationX(int px);
    T translationY(int px);
    T translationZ(int px);
    T pivotX(int px);
    T pivotY(int px);

    T x(float dp);
    T y(float dp);
    T z(float dp);
    T scrollX(float dp);
    T scrollY(float dp);
    T translationX(float dp);
    T translationY(float dp);
    T translationZ(float dp);
    T pivotX(float dp);
    T pivotY(float dp);
    //</editor-fold>

    T requestLayout();

    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="共用方法">
    /**
     * 获取当前选择 View 的 Gravity
     * 支持 LinearLayout,TextView,RelativeLayout
     */
    int gravity();
    /**
     * 设置当前选择 View 的 Gravity
     * 支持 LinearLayout,TextView,RelativeLayout
     */
    T gravity(int gravity);

    //</editor-fold>

    //<editor-fold desc="TextView">

    //<editor-fold desc="基本设置">
    /**
     * 设置当前选中 TextView 的 文本（text）.
     * @param resId 字符串资源
     */
    T text(@StringRes int resId);

    /**
     * 设置当前选中 TextView 的 文本（text）（带有格式化符号）
     * @param resId 字符串资源 （带有格式化符号）
     * @param formatArgs 格式化参数
     * @see Context#getString(int, Object...)
     */
    T text(@StringRes int resId, Object... formatArgs);

    /**
     * 设置当前选中 TextView 的 文本（text）.
     * @param text 文本内容
     */
    T text(CharSequence text);

    /**
     * 设置当前选中 TextView 的 提示（hint） .
     * @param resId 字符串资源
     */
    T hint(@StringRes int resId);

    /**
     * 设置当前选中 TextView 的 提示（hint）.
     * @param hint 提示内容
     */
    T hint(CharSequence hint);

    /**
     * 设置当前选中 TextView 的字体颜色（TextColor）. ARGB格式的颜色值，注意不是颜色Id
     * @param color ARGB格式的颜色值，注意不是颜色Id
     */
    T textColor(@ColorInt int color);

    /**
     * 设置当前选中 TextView 的字体颜色（TextColor）. 颜色资源Id
     * @param id 颜色资源Id
     */
    T textColorId(@ColorRes int id);

    /**
     * 设置当前选中 TextView 的字体颜色（TextColor）. 颜色状态列表 ColorStateList
     */
    T textColor(ColorStateList color);

    /**
     * 设置当前选中 TextView 的字体颜色（TextColor）. 颜色状态列表 ColorStateList 资源Id
     */
    T textColorListId(@ColorRes int id);

    /**
     * 设置当前选中 TextView 的字体大小（TextSize）. sp 单位
     */
    T textSize(float sp);

    /**
     * 设置当前选中 TextView 的字体大小（TextSize）. 指定资源Id
     * @param id 距离资源Id
     */
    T textSizeId(@DimenRes int id);

    /**
     * 设置当前选中 TextView 的字体大小（TextSize）. 指定资源Id
     * @param type @see {@link android.util.TypedValue}
     */
    T textSize(int type, float size);

    /**
     * 设置当前选中 TextView 的最大行数（MaxLines）.
     */
    T maxLines(int lines);

    /**
     * 设置当前选中 TextView 是否单行显示（SingleLine）.
     * @param singleLine 不传时默认true
     */
    T singleLine(boolean... singleLine);

    /**
     * 设置当前选中 TextView 的输入类型.
     * @see android.text.InputType
     */
    T inputType(int type);

    /**
     * 设置当前选中 TextView 的字体.
     * @param typeface 字体
     */
    T typeface(Typeface typeface);

    /**
     * 设置当前选中 TextView 的阴影.
     * @see android.widget.TextView#setShadowLayer(float, float, float, int)
     */
    T shadowLayer(float radius, float dx, float dy, int color);

    //</editor-fold>

    //<editor-fold desc="扩展设置">
    /**
     * 转换当前选中 TextView 的 文本（text）
     * @param converter 文本转换器
     */
    T text(TextConverter converter);

    /**
     * 设置当前选中 TextView 的 文本（text） 如果内容为空将隐藏 View
     * @param text 文本内容
     */

    T textGoneIfEmpty(CharSequence text);

    /**
     * 设置当前选中 TextView 的 文本（text） 为 格式化的结果值.
     * @see String#format(Locale, String, Object...)
     * @param format 格式化字符串
     * @param args 格式化参数
     */
    T text(CharSequence format, Object... args);

    /**
     * 设置当前选中 TextView 的 文本（text） 为 textGoneIfEmpty 否则如果TextUtils.isEmpty(textGoneIfEmpty)使用 默认值defValue.
     * @see android.text.TextUtils#isEmpty(CharSequence)
     * @param text 文本内容
     * @param defValue 默认值，如果 TextUtils.isEmpty(text)
     */
    T textElse(CharSequence text, CharSequence defValue);

    /**
     * 为当前选中 TextView 设置格式化字符串
     * 例如 当前值为 “小明”
     * 执行 textFormat("你好 %s")
     * 结果变为 “你好 小明”
     * @param format 格式化 （只能含有一个 %s，如果没有%s，效果将和 text(format) 一样）
     */
    T textFormat(String format);
    /**
     * 设置当前选中 TextView 的 提示（hint） 为 格式化的结果值.
     * @see String#format(Locale, String, Object...)
     * @param format 格式化字符串
     * @param args 格式化参数
     */
    T hint(CharSequence format, Object... args);
    /**
     * 设置当前选中 TextView 的 文本（text） 为 格式化的结果值（可以有HTML标签效果）.
     * 例如：html("一共有（<font color='#%s'><big>%d</big></font>）个结果",R.color.red,5);
     * 结果："一共有（5）个结果" （其中 5 的字体会变大,并且为红色）
     * @see String#format(Locale, String, Object...)
     * @param format 格式化字符串
     * @param args 格式化参数
     */
    T html(String format, Object... args);

    //</editor-fold>

    //<editor-fold desc="时间设置">
    /**
     * 设置当前选中 TextView 的 文本（text） 为 time 的时间格式化 （HH:mm:ss）.
     */
    T time(Date time);
    /**
     * 设置当前选中 TextView 的 文本（text） 为 time 的指定时间格式化 .
     */
    T time(@NonNull String format, Date time);
    /**
     * 设置当前选中 TextView 的 文本（text） 为 time 的指定时间格式化 .
     */
    T time(@NonNull DateFormat format, Date time);
    /**
     * 设置当前选中 TextView 的 文本（text） 为 time 的天格式化（M-d）.
     */
    T timeDay(Date time);
    /**
     * 设置当前选中 TextView 的 文本（text） 为 time 的日期格式化（y-M-d）.
     */
    T timeDate(Date time);
    /**
     * 设置当前选中 TextView 的 文本（text） 为 time 的日期时间格式化（yyyy年MM月dd日 HH时mm分ss秒）.
     */
    T timeFull(Date time);
    /**
     * 设置当前选中 TextView 的 文本（text） 为 time 的日期时间格式化（yyyy年MM月dd日 HH时mm分）.
     */
    T timeSimple(Date time);
    /**
     * 设置当前选中 TextView 的 文本（text） 为 time 的标准格式化（yyyy-MM-dd HH:mm:ss）.
     */
    T timeStandard(Date time);
    /**
     * 设置当前选中 TextView 的 文本（text） 为 time 的动态格式化（例如：3分钟前，昨天）.
     */
    T timeDynamic(Date time);
    /**
     * 设置当前选中 TextView 的 文本（text） 为 time 的动态格式化（例如：MM-dd 或 yyyy-MM-dd）.
     */
    T timeDynamicDate(Date time);
    /**
     * 设置当前选中 TextView 的 文本（text） 为 time 的动态格式化（例如：MM-dd HH:mm 或 yyyy-MM-dd HH:mm）.
     */
    T timeDynamicDateTime(Date time);
    /**
     * 设置当前选中 TextView 的 文本（text） 为 start-close 开始和结束
     */
    T timeSpan(Date start, Date close, @NonNull String split, String... formats);

    /**
     * 设置当前选中 TextView 的字体.
     * @param typefaceFile 字体文件
     */
    T typeface(File typefaceFile);
    /**
     * 设置当前选中 TextView 的字体.
     * @param typefacePath 字体文件
     */
    T typeface(String typefacePath);

    //</editor-fold>

    //<editor-fold desc="基本获取">
    /**
     * 获取 TextView 的 文本（text）.
     */
    String text();
    /**
     获取 TextView 的 提示（hint）.
     */
    String hint();

    //</editor-fold>

    //<editor-fold desc="复合图片">
    T drawablePadding(int px);
    T drawablePadding(float dp);
    T drawableLeft(@DrawableRes int id);
    T drawableTop(@DrawableRes int id);
    T drawableRight(@DrawableRes int id);
    T drawableBottom(@DrawableRes int id);
    T drawableLeft(@Nullable Drawable drawable);
    T drawableTop(@Nullable Drawable drawable);
    T drawableRight(@Nullable Drawable drawable);
    T drawableBottom(@Nullable Drawable drawable);
    T drawables(@Nullable Drawable left, @Nullable Drawable top,
                @Nullable Drawable right, @Nullable Drawable bottom);
    @Nullable Drawable drawableLeft();
    @Nullable Drawable drawableTop();
    @Nullable Drawable drawableRight();
    @Nullable Drawable drawableBottom();
    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="RatingBar">
    /**
     * 设置当前选中 RatingBar 的值（rating）
     */
    T rating(float rating);

    /**
     * 获取当前选中 RatingBar 的值（rating）
     */
    float rating();
    //</editor-fold>

    //<editor-fold desc="列表控件">

    //<editor-fold desc="基本设置">
    /**
     * 设置当前选中 AdapterView 的适配器.
     * @param adapter 适配器 adapter
     */
    T adapter(Adapter adapter);

    /**
     * 设置当前选中 ExpandableListView 的适配器.
     * @param adapter 适配器 adapter
     */
    T adapter(ExpandableListAdapter adapter);

    /**
     * 通知当前选中适配器控件（AdapterView 如 ListView）数据改变
     */
    T dataChanged();

    /**
     * 设置当前选中 AdapterView 的选择索引.
     */
    T setSelection(int position);
    
    //</editor-fold>

    //<editor-fold desc="基本获取">
    /**
     * 获取当前选择 AdapterView 的选择项.
     * @return 选择对象
     */
    Object getSelectedItem();

    /**
     * 获取当前选择 AdapterView 的选择索引.
     * @return 如果没选择将反回 AdapterView.INVALID_POSITION
     */
    int getSelectedItemPosition();

    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="ImageView">

    /**
     * 设置当前选中 ImageView 的图片.
     * @param resId 图片资源Id
     */
    T image(int resId);

    /**
     * 设置当前选中 ImageView 的图片.
     * @param drawable Drawabled图片对象
     */
    T image(Drawable drawable);

    /**
     * 设置当前选中 ImageView 的图片.
     * @param bitmap Bitmap 位图对象
     */
    T image(Bitmap bitmap);

    /**
     * 设置当前选中 ImageView 的图片.
     * 要自行重写本方法实现网络获取并缓存
     * 继承 {@link AfViewQuery} 重写本方法
     * 并在 {@link com.andframe.application.AfApp#newViewQuery(Viewer)} 中返回新的子类
     * @param url 图片Url（不限于本地url，也可以是网络url，但是需要重写实现）.
     */
    T image(String url);

    /**
     * 设置当前选中 ImageView 的图片. 并制定下载图片的尺寸（需要自行重写方法实现，仅作为扩展接口使用）
     * 要自行重写本方法实现网络获取并缓存
     * 继承 {@link AfViewQuery} 重写本方法
     * 并在 {@link com.andframe.application.AfApp#newViewQuery(Viewer)} 中返回新的子类
     * @param url 图片Url（不限于本地url，也可以是网络yrl，但是要自行重写本方法实现网络获取并缓存）.
     */
    T image(String url, int widthPx, int heightPx);

    /**
     * 设置当前选中 ImageView 的图片. 否则设置默认资源图片
     * @param url 图片Url（不限于本地url，也可以是网络yrl，但是要自行重写本方法实现网络获取并缓存）.
     * @param resId 默认资源图片
     */
    T image(String url, int resId);

    /**
     * 设置当前选中 ImageView 的图片 为头像
     * @param url 图片Url（不限于本地url，也可以是网络yrl，但是要自行重写本方法实现网络获取并缓存）.
     */
    T avatar(String url);

    /**
     * 设置当前选中 ImageView 的 ScaleType
     * @see ImageView#setScaleType(ImageView.ScaleType)
     */
    T scaleType(ImageView.ScaleType scaleType);

    /**
     * 设置 TintColor
     */
    T imageTintColor(int color);

    /**
     * 设置 TintColorId
     */
    T imageTintColorId(@ColorRes int colorId);

    //</editor-fold>

    //<editor-fold desc="EditText">

    //<editor-fold desc="基本设置">
    /**
     * Convenience for {@link android.widget.EditText#setSelection(int, int)}.
     */
    T selection(int start, int stop);

    /**
     * Convenience for {@link android.widget.EditText#setSelection(int)}.
     */
    T selection(int index);

    /**
     * Convenience for {@link android.widget.EditText#selectAll}.
     */
    T selectAll();

    /**
     * Convenience for {@link android.widget.EditText#extendSelection}.
     */
    T extendSelection(int index);

    /**
     * 移动 EditText 光标到末尾
     */
    T selectionToEnd();
    //</editor-fold>

    //<editor-fold desc="基本获取">
    /**
     * 获取当前选中 EditText 的 内容.
     * @see EditText#getEditableText()
     */
    Editable editable();
    /**
     * 获取当前选中 EditText 的 内容.
     * @see EditText#getSelectionStart()
     */
    int selectionStart();
    /**
     * 获取当前选中 EditText 的 内容.
     * @see EditText#getSelectionEnd()
     */
    int selectionEnd();
    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="CompoundButton">
    /**
     * 设置当前选中 CompoundButton 的 checked状态.
     */
    T checked(boolean checked);

    /**
     * 获取当前选中 CompoundButton 的 checked状态.
     */
    boolean isChecked();

    /**
     * 设置当前选中 CompoundButton checked 值为自反（开关触发器）
     */
    T toggle();

    //</editor-fold>

    //<editor-fold desc="ProgressBar">
    int max();
    int progress() ;
    int secondaryProgress();
    T max(int max);
    T progress(int progress);
    T secondaryProgress(int secondaryProgress);
    //</editor-fold>

    //<editor-fold desc="SeekBar">
    //</editor-fold>

    //<editor-fold desc="LinearLayout">
    int orientation();
    T orientation(/*@LinearLayoutCompat.OrientationMode */int orientation);
    //</editor-fold>

    //<editor-fold desc="ViewPager">
    /**
     * 设置当前选中 ViewPager 的 PagerAdapter.
     * @see ViewPager#setAdapter(PagerAdapter)
     */
    T adapter(PagerAdapter adapter);
    T currentItem(int item);
    T currentItem(int item, boolean smoothScroll);
    /**
     * 添加当前选中 ViewPager 的 OnPageChangeListener.
     * @see ViewPager#addOnPageChangeListener(ViewPager.OnPageChangeListener)
     */
    T pageChanged(ViewPager.OnPageChangeListener listener);
    /**
     * 添加当前选中 ViewPager 的 OnPageChangeListener.
     * @see OnPageSelectedListener 简单版的监听器
     * @see ViewPager#addOnPageChangeListener(ViewPager.OnPageChangeListener)
     */
    T pageSelected(OnPageSelectedListener listener);
    int currentItem();
    //</editor-fold>

    //<editor-fold desc="容器布局">

    //<editor-fold desc="基本操作">

    T removeView(View view);
    T removeViewAt(int index);
    T removeViewInLayout(View view);
    T removeViewsInLayout(int start, int count);
    T removeViews(int start, int count);
    T removeAllViews();
    T removeAllViewsInLayout();

    T clipToPadding(boolean clip);
    T clipChildren(boolean clip);

    //</editor-fold>

    //<editor-fold desc="添加子控件">
    T addView(View... views);
    T addView(View view, int index);
    T addView(View view, int widthPx, int heightPx);
    T addView(View view, float widthDp, float heightDp);
    T addView(View view, LayoutParams params);
    T addView(View view, int index, LayoutParams params);
    //</editor-fold>

    //<editor-fold desc="获取子控件">
    int childCount();
    View childAt(int index);
    View[] children();
    View[] childrenTree();
    //</editor-fold>

    //<editor-fold desc="分离操作">
    /**
     * 把当前选中 View 从其父容器中删除，并返回 （第一个选中的 View）
     */
    @Nullable
    View breakView();
    /**
     * 把当前选中 View 从其父容器中删除，并返回 （所有选中的View）
     */
    View[] breakViews();
    /**
     * 把当前选中 View 删除其所有子空间并，并返回
     */
    View[] breakChildren();

    //</editor-fold>

    //<editor-fold desc="替换操作">
    /**
     * 将当前选择的 View 从其父容器中删除，并使用新的 View 添加到 父容器中，实现替换
     * @param view 新的 View（如果新的 View 已经存在父容器，将会被分离出来）
     */
    T replace(View view);

    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="事件绑定">
    /**
     * 注册点击事件（intervalTime = 1000）
     * @param listener 点击时间监听器
     */
    T clicked(View.OnClickListener listener);
    /**
     * 注册点击事件
     * @param intervalTime 时间间隔（防止屏幕接触不良导致的点击抖动和多次点击事件触发）
     * @param listener 点击时间监听器
     */
    T clicked(View.OnClickListener listener, int intervalTime);

    /**
     * 注册长按事件
     */
    T longClicked(View.OnLongClickListener listener);

    /**
     * 列表控件 Item 点击事件
     */
    T itemClicked(AdapterView.OnItemClickListener listener);

    /**
     * 列表控件 Item 长按事件
     */
    T itemLongClicked(AdapterView.OnItemLongClickListener listener);

    /**
     * 注册当前选择 Textview 的 文本改变监听器 TextWatcher
     */
    T textChanged(TextWatcher watcher);

    /**
     * 注册当前选择 CompoundButton 的 选中改变监听器 OnCheckedChangeListener
     */
    T checkChanged(CompoundButton.OnCheckedChangeListener listener);

    /**
     * 注册当前选择 RadioGroup 的 选中改变监听器 OnCheckedChangeListener
     */
    T radioChanged(RadioGroup.OnCheckedChangeListener listener);

    //</editor-fold>

    //<editor-fold desc="子接口定义">
    interface ViewIterator<TT> {
        void each(@NonNull TT view);
    }

    interface ViewReturnIterator<TT, TTT> {
        TTT each(@NonNull TT view);
    }

    interface Converter<F, T> {
        T convert(F f);
    }

    interface TextConverter extends Converter<String,String> {

    }

    interface OnPageSelectedListener {
        /**
         * This method will be invoked when a new page becomes selected. Animation is not
         * necessarily complete.
         *
         * @param position Position index of the new selected page.
         */
        void onPageSelected(int position);
    }
    //</editor-fold>

}
