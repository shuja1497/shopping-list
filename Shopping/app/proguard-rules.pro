# ============================================================
# Production ProGuard/R8 rules for Shopping List App
# ============================================================

# ----------------------------------------------------------
# 1. Crash Reporting: Preserve line numbers, hide source files
# ----------------------------------------------------------
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ----------------------------------------------------------
# 2. Room Database
# ----------------------------------------------------------
# Keep Room entities (fields are mapped to DB columns)
-keep class dev.zenolabs.groceryshopping.data.local.ShoppingItem { *; }
-keep class dev.zenolabs.groceryshopping.data.local.Category { *; }
-keep class dev.zenolabs.groceryshopping.data.local.CategoryConverter { *; }

# Keep all classes annotated with Room annotations
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }
-keep @androidx.room.Database class * { *; }
-keep class * extends androidx.room.RoomDatabase { *; }

# Room generated code
-keep class * implements androidx.room.RoomDatabase$Callback { *; }

# ----------------------------------------------------------
# 3. Kotlin
# ----------------------------------------------------------
-keep class kotlin.Metadata { *; }
-keepattributes RuntimeVisibleAnnotations

# Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# ----------------------------------------------------------
# 4. Hilt / Dagger
# ----------------------------------------------------------
# Hilt generates code that uses reflection; keep entry points
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }
-keepclasseswithmembers class * {
    @dagger.* <methods>;
}
-keepclasseswithmembers class * {
    @javax.inject.* <fields>;
}
-keepclasseswithmembers class * {
    @javax.inject.* <init>(...);
}

# ----------------------------------------------------------
# 5. Gson (used by Retrofit/Gson converter)
# ----------------------------------------------------------
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# ----------------------------------------------------------
# 6. Retrofit
# ----------------------------------------------------------
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keep,allowobfuscation interface * extends retrofit2.Call

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# ----------------------------------------------------------
# 7. Compose + Lifecycle
# ----------------------------------------------------------
# Prevent R8 from merging/rewriting Compose UI interface hierarchy.
# R8 vertical class merging causes IncompatibleClassChangeError on
# ModifierLocalProvider.all() at runtime (API 36+).
-keep,allowobfuscation,allowshrinking interface androidx.compose.ui.** { *; }
-keep,allowobfuscation,allowshrinking class androidx.compose.ui.** { *; }


# ----------------------------------------------------------
# 8. Timber
# ----------------------------------------------------------
-dontwarn org.jetbrains.annotations.**

# ----------------------------------------------------------
# 9. SQLCipher (for encrypted Room DB)
# ----------------------------------------------------------
-keep class net.sqlcipher.** { *; }
-dontwarn net.sqlcipher.**

# ----------------------------------------------------------
# 9b. Google Tink / Security-Crypto (Error Prone annotations)
# ----------------------------------------------------------
-dontwarn com.google.errorprone.annotations.CanIgnoreReturnValue
-dontwarn com.google.errorprone.annotations.CheckReturnValue
-dontwarn com.google.errorprone.annotations.Immutable
-dontwarn com.google.errorprone.annotations.RestrictedApi

# ----------------------------------------------------------
# 10. General Android
# ----------------------------------------------------------
# Keep enums (used by Category, SortOption, etc.)
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Parcelable implementations
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep Serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
