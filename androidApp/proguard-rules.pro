-keepattributes RuntimeVisibleAnnotations,RuntimeInvisibleAnnotations,Signature,InnerClasses,EnclosingMethod

-keep class **$$serializer { *; }
-keepclassmembers class ** {
    public static ** serializer(...);
}
-keepclassmembers class **$Companion {
    public final kotlinx.serialization.KSerializer serializer(...);
}

-keep @kotlinx.serialization.Serializable class ru.tanexc.hemoanalysis.** { *; }

-keep class io.ktor.** { *; }
-keep class kotlinx.coroutines.** { *; }
-keep class kotlinx.serialization.** { *; }
-keepnames class io.ktor.** { *; }

-keep class ai.onnxruntime.** { *; }

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn java.lang.management.**
