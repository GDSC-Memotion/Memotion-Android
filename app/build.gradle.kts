plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlinx-serialization")

    // navigation-component's SafeArgs
    id("androidx.navigation.safeargs")

    // Hilt-Dagger
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "konkuk.gdsc.memotion"
    compileSdk = 34

    defaultConfig {
        applicationId = "konkuk.gdsc.memotion"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.compose.ui:ui-text-android:1.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    val nav_version = "2.5.3"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    val activity_version = "1.7.0"
    implementation("androidx.activity:activity-ktx:$activity_version")

    val glide_version = "4.16.0"
    implementation("com.github.bumptech.glide:glide:$glide_version")

    val balloon_version = "1.6.4"
    implementation("com.github.skydoves:balloon:$balloon_version")
    val progressview_version = "1.1.3"
    implementation("com.github.skydoves:progressview:$progressview_version")

    val retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofit_version")

    val okhttp_version = "4.12.0"
    implementation("com.squareup.okhttp3:okhttp:$okhttp_version")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp_version")

    val kotlinx_serialization_json = "1.6.0"
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinx_serialization_json")
    val kotlinx_serialization_converter = "1.0.0"
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:$kotlinx_serialization_converter")

    val lifecycle_version = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    val hilt_version = "2.48"
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")

    // Integration with activities
    implementation("androidx.activity:activity-compose:1.4.0")
    // Compose Material Design
    implementation("androidx.compose.material:material:1.4.3")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:1.4.3")
    // Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1")
    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.4.3")

    // When using a MDC theme
    implementation("com.google.android.material:compose-theme-adapter-3:1.1.1")

    // When using a AppCompat theme
    implementation("com.google.accompanist:accompanist-appcompat-theme:0.16.0")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}