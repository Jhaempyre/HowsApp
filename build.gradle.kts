// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    //Remove these lines as they shouldn't be at the top level with apply false above
     kotlin("jvm") version "2.1.20"
     kotlin("plugin.serialization") version "2.1.20"
}