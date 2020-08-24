plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "farasense.mobile"
        minSdkVersion(22)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        dataBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    //    Android Default
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    //    Kotlin Library
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.72")

    //    Android Architecture
    implementation("androidx.core:core-ktx:1.3.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.navigation:navigation-ui:2.3.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.0")
    implementation("androidx.navigation:navigation-fragment:2.3.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.1.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")

    // Dependency Injection (Koin)
    implementation("org.koin:koin-android:2.0.1")
    implementation("org.koin:koin-androidx-scope:2.0.1")
    implementation("org.koin:koin-androidx-viewmodel:2.0.1")

    // Persistence
    implementation("androidx.room:room-runtime:2.2.5")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.android.material:material:1.2.0")
    kapt("androidx.room:room-compiler:2.2.5")
    implementation("androidx.room:room-ktx:2.2.5")

    //    HTTP Request
    implementation("com.android.volley:volley:1.1.1")

    // Gson
    implementation("com.google.code.gson:gson:2.8.6")

    //    Time Library
    implementation("net.danlew:android.joda:2.9.9.4")

    //    Components Views Library
    implementation("com.miguelcatalan:materialsearchview:1.4.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.0.3")
    implementation("io.github.dvegasa:arcpointer:1.0.2")

    // AWS SDK
    implementation("com.amazonaws:aws-android-sdk-core:2.6.31")
    implementation("com.amazonaws:aws-android-sdk-auth-core:2.6.31@aar")
    implementation("com.amazonaws:aws-android-sdk-pinpoint:2.6.0")
    implementation("com.amazonaws:aws-android-sdk-auth-ui:2.6.0@aar")
    implementation("com.amazonaws:aws-android-sdk-auth-userpools:2.6.0@aar")
    implementation("com.amazonaws:aws-android-sdk-cognitoidentityprovider:2.6.0")

    //    Test Library
    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}