# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Android\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#----------------------------------------------------------------------------------------#
# andframe                                                                               #
#----------------------------------------------------------------------------------------#
#常用混淆标记
-ignorewarnings
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep class * extends java.lang.annotation.Annotation { *; }
-keepclasseswithmembernames class * {
    native <methods>;
}

#retrolambda混淆标记
-dontwarn java.lang.invoke.*

#andframe混淆标记
-keep enum com.andframe.model.** {*;}
-keep class com.andframe.model.** {<fields>;}

-keepclassmembers class * implements com.andframe.api.viewer.ViewModuler {
   <init>();
}
-keepclassmembers class * {
    @com.andframe.annotation.** *;
}
#----------------------------------------------------------------------------------------#

