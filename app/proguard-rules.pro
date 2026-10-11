# Proguard rules for Jinlite Workers
-keepattributes *Annotation*
-keepclassmembers class * {
    @androidx.room.* *;
}
