plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.google.gms.google.services)
//    id("com.google.gms.google-services")
}

android {
    namespace = "com.app.kahoji"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.app.kahoji"
        minSdk = 28
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(project(":core-ui"))
    implementation(project(":core-navigation"))
    implementation(project(":core-common"))
    implementation(project(":core-network"))
    implementation(project(":core-socket"))
    implementation(project(":core-agora"))
    implementation(project(":core-fcm"))
    implementation(project(":feature-splash"))
    implementation(project(":feature-onboarding"))
    implementation(project(":feature-auth-user"))
    implementation(project(":feature-auth-host"))
    implementation(project(":feature-bottom-nav"))
    implementation(project(":feature-home"))
    implementation(project(":feature-home-host"))
    implementation(project(":feature-story"))
    implementation(project(":feature-chat"))
    implementation(project(":feature-call"))
    implementation(project(":feature-wallet"))
    implementation(project(":feature-referral"))
    implementation(project(":feature-gift"))
    implementation(project(":feature-withdraw"))
    implementation(project(":feature-block"))
    implementation(project(":feature-change-password"))
    implementation(project(":feature-notification"))
    implementation(project(":feature-review"))
    implementation(project(":feature-leaderboard"))
    implementation(project(":feature-cms"))
    implementation(project(":feature-help-support"))

    //Navigation 3
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material3.adaptive.navigation3)
    implementation(libs.kotlinx.serialization.core)


    //DAGGER-HILT
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    //Coil
    implementation(libs.coil.compose)
//    implementation(libs.coil.network.okhttp)

    //Socket
    implementation("io.socket:socket.io-client:2.1.2")


//AWS S3
    implementation(libs.aws.android.sdk.s3) // Use the latest version
    implementation(libs.core)

    //Retrofit
    implementation(libs.retrofit)
    //noinspection UseTomlInstead
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    //Okhttp Logging Interceptor
    implementation(libs.logging.interceptor)


//Datastore
    implementation(libs.androidx.datastore.preferences)

    //GSON
    implementation("com.google.code.gson:gson:2.14.0")

    //Constraint-Layout
    implementation(libs.androidx.constraintlayout.compose)

    implementation(libs.material3)
    implementation(libs.material.icons.core)
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.androidx.hilt.navigation.compose)


    implementation(libs.firebase.messaging)





}