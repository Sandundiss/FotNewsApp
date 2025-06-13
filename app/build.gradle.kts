import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.fotnewsapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fotnewsapp"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
implementation(libs.activity)
implementation(libs.constraintlayout)
    dependencies {
        implementation (libs.firebase.dynamic.module.support)
        implementation ("com.google.firebase:firebase-auth:21.2.0")
        implementation ("com.google.firebase:firebase-firestore:24.7.1")

    }
        // Firebase Authentication
    implementation("com.google.firebase:firebase-auth:23.2.1")
        // Firebase Realtime Database
        implementation ("com.google.firebase:firebase-database:20.2.2")


// Add these additional implementations
implementation("com.google.android.material:material:1.12.0")
implementation("androidx.core:core-ktx:1.12.0")
implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
implementation("androidx.navigation:navigation-ui-ktx:2.9.0")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.1")
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("com.google.android.material:material:1.12.0")
        implementation ("androidx.fragment:fragment:1.5.5")
        implementation ("androidx.appcompat:appcompat:1.6.1")

    implementation(libs.firebase.database)
    implementation(libs.google.firebase.auth)
    implementation(libs.google.firebase.firestore)
    testImplementation(libs.junit)
androidTestImplementation(libs.ext.junit)
androidTestImplementation(libs.espresso.core)
}
