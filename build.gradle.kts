// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
}
buildscript {
    repositories {
        google()  // Thêm Google's Maven repository vào đây
        mavenCentral()  // Thêm Maven Central repository vào đây
    }
}

allprojects {
    repositories {
        google()  // Thêm Google's Maven repository vào đây
        mavenCentral()  // Thêm Maven Central repository vào đây
    }
}