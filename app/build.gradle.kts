import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")

    //Firebase
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.compose.compiler)

    alias(libs.plugins.hilt)
}

android {
    namespace = "com.pmgdev.pulse"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.pmgdev.pulse"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        val props = Properties()

        val propFile = file("../local.properties")

        if (propFile.exists()) {
            props.load(propFile.inputStream())
            val apiKey = props.getProperty("apiKey", "KEY_NOT_SET")
            buildConfigField("String", "API_KEY", "\"$apiKey\"")
        } else {
            buildConfigField("String", "API_KEY", "\"KEY_NOT_SET\"")
            println("local.properties NO encontrado en ${propFile.absolutePath}")
        }
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.lottie.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.security.crypto)



    //Hilt
    implementation(libs.hilt.library)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation)


    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.restore.ktx)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.appcheck.debug)
    implementation(libs.firebase.messaging)

    //Coil
    implementation(libs.coil.compose)

    //retrofit
    implementation(libs.retrofit)

    implementation(libs.converter.gson)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)


    //IA
    implementation(libs.generative.ai.client)

    //Google Fits
    implementation(libs.google.fit)
    implementation(libs.google.auth)

    //DataStore
    implementation(libs.datastore.preferences)

    implementation("com.onesignal:OneSignal:[5.1.6, 5.1.99]")
}