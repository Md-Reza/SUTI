plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.chuti"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.chuti"
        minSdk = 24
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.chip.navigation.bar)

    //retrofit
    implementation ("com.squareup.retrofit2:converter-gson:2.4.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")

    implementation ("com.squareup.okhttp3:okhttp:3.14.9")
    implementation ("com.squareup.okhttp3:logging-interceptor:3.2.0")

    //Spots progress dialog
    implementation ("com.github.d-max:spots-dialog:0.7@aar")

    //biometric
    implementation ("androidx.biometric:biometric:1.1.0")

    //runtime permission
    implementation ("com.karumi:dexter:6.2.3")

    implementation (libs.circleimageview)
    implementation ("com.airbnb.android:lottie:6.0.0")

}