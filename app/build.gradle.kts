plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.c011_mad_assignment_02"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.c011_mad_assignment_02"
        minSdk = 27
        targetSdk = 36
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

    // Core AndroidX Dependencies (Using Version Catalog)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)


    // UI Components
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4") // Use the latest stable version



// Firebase - Bill of Materials (manages versions for all Firebase libraries)
    implementation(platform("com.google.firebase:firebase-bom:34.0.0"))

    // Cloud Firestore SDK
    implementation("com.google.firebase:firebase-firestore")

    // FirebaseUI for Firestore (for RecyclerView adapter)
    implementation("com.firebaseui:firebase-ui-firestore:8.0.2")

    // Testing Dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // --- REMOVED REDUNDANT AND CONFLICTING DEPENDENCIES ---
    // implementation(libs.firebase.database) // Removed: Your app uses Firestore, not Realtime Database.
    // implementation("com.google.android.material:material:1.9.0") // Removed: Covered by libs.material
    // implementation("androidx.appcompat:appcompat:1.6.1") // Removed: Covered by libs.appcompat
    // implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Removed: Covered by libs.constraintlayout
}
