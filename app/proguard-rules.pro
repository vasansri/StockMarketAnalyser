# StockVision ProGuard Rules

# Hilt / Dagger
-keep class dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }
-keep class * extends androidx.lifecycle.ViewModel { *; }

# Retrofit / OkHttp
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Serialization (Gson)
-keep class com.stockvision.data.remote.dto.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }

# Compose
-keep class androidx.compose.ui.platform.** { *; }

# Firebase
-keep class com.google.firebase.** { *; }

# Custom Models (Keep domain models to avoid obfuscation issues in calculations)
-keep class com.stockvision.domain.model.** { *; }
