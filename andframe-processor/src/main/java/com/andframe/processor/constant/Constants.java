package com.andframe.processor.constant;

import com.andframe.annotation.BindLayout;
import com.andframe.annotation.listener.BindOnCheckedChanged;
import com.andframe.annotation.listener.BindOnClick;
import com.andframe.annotation.listener.BindOnEditorAction;
import com.andframe.annotation.listener.BindOnFocusChange;
import com.andframe.annotation.listener.BindOnItemClick;
import com.andframe.annotation.listener.BindOnItemLongClick;
import com.andframe.annotation.listener.BindOnItemSelected;
import com.andframe.annotation.listener.BindOnLongClick;
import com.andframe.annotation.listener.BindOnPageChange;
import com.andframe.annotation.listener.BindOnTextChanged;
import com.andframe.annotation.listener.BindOnTouch;
import com.andframe.annotation.resource.BindAnim;
import com.andframe.annotation.resource.BindArray;
import com.andframe.annotation.resource.BindBitmap;
import com.andframe.annotation.resource.BindBool;
import com.andframe.annotation.resource.BindColor;
import com.andframe.annotation.resource.BindDimen;
import com.andframe.annotation.resource.BindDrawable;
import com.andframe.annotation.resource.BindFloat;
import com.andframe.annotation.resource.BindFont;
import com.andframe.annotation.resource.BindInt;
import com.andframe.annotation.resource.BindString;
import com.andframe.annotation.view.BindView;
import com.andframe.annotation.view.BindViews;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * 常量集合
 * Created by SCWANG on 2017/8/7.
 */

public class Constants {

    public static final String OPTION_SDK_INT = "andframe.minSdk";
    public static final String OPTION_DEBUGGABLE = "andframe.debug";
    public static final String COLOR_STATE_LIST_TYPE = "android.content.res.ColorStateList";
    public static final String BITMAP_TYPE = "android.graphics.Bitmap";
    public static final String ANIMATION_TYPE = "android.view.animation.Animation";
    public static final String DRAWABLE_TYPE = "android.graphics.drawable.Drawable";
    public static final String TYPED_ARRAY_TYPE = "android.content.res.TypedArray";
    public static final String TYPEFACE_TYPE = "android.graphics.Typeface";
    public static final String NULLABLE_ANNOTATION_NAME = "Nullable";
    public static final String STRING_TYPE = "java.lang.String";
    public static final String LIST_TYPE = List.class.getCanonicalName();

    public static final List<String> RESOURCE_TYPES = Arrays.asList(
            "layout", "array", "attr", "bool", "color", "dimen", "drawable", "id", "integer", "string"
    );

    public static final List<Class<? extends Annotation>> BINDINGS = Arrays.asList(
            BindView.class,
            BindViews.class,
            BindLayout.class
    );

    public static final List<Class<? extends Annotation>> RESOURCES = Arrays.asList(
            BindAnim.class,
            BindArray.class,
            BindBitmap.class,
            BindBool.class,
            BindColor.class,
            BindDimen.class,
            BindDrawable.class,
            BindFloat.class,
            BindFont.class,
            BindInt.class,
            BindString.class
    );

    public static final List<Class<? extends Annotation>> LISTENERS = Arrays.asList(
            BindOnCheckedChanged.class,
            BindOnClick.class,
            BindOnEditorAction.class,
            BindOnFocusChange.class,
            BindOnItemClick.class,
            BindOnItemLongClick.class,
            BindOnItemSelected.class,
            BindOnLongClick.class,
            BindOnPageChange.class,
            BindOnTextChanged.class,
            BindOnTouch.class
    );
}
