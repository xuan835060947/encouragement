# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/meizu/Library/Android/sdk/tools/proguard/proguard-android.txt
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



-keep class com.person.xuan.encouragement.entity.** {
*;
}

-keep class com.google.gson.** {
*;
}

-keep public class * implements java.lang.reflect.Type

-keep class java.**,javax.**,com.sun.**,android.** {
   static final %                *;
   static final java.lang.String *;
  *;
}

#不混淆输入的类文件
-dontobfuscate
#不优化输入的类文件
-dontoptimize

# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }