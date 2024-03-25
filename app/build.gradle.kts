plugins {
    id("com.android.application")
   // id("com.chaquo.python")
}

android {
    namespace = "com.example.retrievision"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.retrievision"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
/*
        ndk {
            abiFilters += listOf("arm64-v8a, x86_64")
        }
*/
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
    buildFeatures {
        mlModelBinding = true
    }
    /*
    flavorDimensions+="pyVersion"
    productFlavors {
        create("py310") {dimension = "pyVersion"}
        create("py311") {dimension="pyVersion"}
    } */
    /*
    chaquopy {
        defaultConfig{
            version="3.10"
            buildPython("C:/Users/Spencer/Documents/Cambal_Julia_V_LE1/algo2/algo2/env/Scripts/python.exe")
        }
        productFlavors{
            getByName("py310") {version="3.10"}
            getByName("py311") {version="3.11"}
        }
        sourceSets{

        }
    } */
}



dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("com.github.bumptech.glide:glide:5.0.0-rc01")
    implementation("org.tensorflow:tensorflow-lite-support:0.1.0")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}