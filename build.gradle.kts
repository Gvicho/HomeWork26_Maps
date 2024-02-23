// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.dagger.hilt.android") version "2.47" apply false // for Hilt
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false // for KSP  (here I had a problem with versions)
}

// safeargs
buildscript {
    repositories {
        google()
        mavenCentral() // for glide
    }

    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
    }
}