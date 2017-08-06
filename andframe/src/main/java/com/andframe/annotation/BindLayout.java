package com.andframe.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>
 * Should be used on {@link android.app.Activity} classes to enable usage of
 * AndroidAnnotations.
 * </p>
 * <p>
 * If the class is abstract, the enhanced activity will not be generated.
 * Otherwise, it will be generated as a final class. You can use
 * AndroidAnnotations to create Abstract classes that handle common code.
 * </p>
 * <p>
 * The annotation value should be one of R.layout.* fields. If not set, no
 * content view will be set, and you should call the
 * <code>setContentView()</code> method yourself, in <code>onCreate()</code>
 * </p>
 * <blockquote>
 *
 * Example :
 *
 * <pre>
 * &#064;BindLayout(R.layout.main)
 * public class MyActivity extends Activity {
 *
 * 	public void launchActivity() {
 * 		// Note the use of generated class instead of original one
 * 		MyActivityTwo_.intent(this).startActivity();
 * 	}
 * }
 *
 * &#064;BindLayout(R.layout.main)
 * public class MyActivityTwo extends Activity {
 * }
 * </pre>
 *
 * </blockquote>
 *
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface BindLayout {

    /**
     * The R.layout.* field which refer to the layout.
     *
     * @return the id of the layout
     */
    int value() default -1;

    /**
     * The resource name as a string which refer to the layout.
     *
     * @return the resource name of the layout
     */
    String[] idname() default {""};
}
