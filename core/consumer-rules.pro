# Preserve kotlinx.serialization serializers and companion objects for SDK and shared-foundation models
-if @kotlinx.serialization.Serializable class io.github.mudrichenkoevgeny.**
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

-if @kotlinx.serialization.Serializable class io.github.mudrichenkoevgeny.**
-keepclassmembers class <1>$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class io.github.mudrichenkoevgeny.**$$serializer { *; }
