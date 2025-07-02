# Keep Compose essentials
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.collection.** { *; }

# Keep Kotlin essentials
-keep class kotlinx.coroutines.** { *; }
-keep class kotlinx.serialization.** { *; }

# Keep Ktor
-keep class io.ktor.** { *; }

# Keep Realm
-keep class io.realm.** { *; }

# Suppress all Android-related warnings
-dontwarn android.**
-dontwarn androidx.**
-dontwarn sun.**
-dontwarn org.slf4j.**
-dontwarn org.xmlpull.**
-dontwarn kotlinx.io.**

# Explicitly keep navigation components
-keep class androidx.navigation.** { *; }
-dontwarn androidx.navigation.**

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# Keep Koin components
-keep class org.koin.** { *; }