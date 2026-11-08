plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.test.virtual4k"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.test.virtual4k"
        minSdk = 34
        targetSdk = 37
        versionCode = 2
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // 中英两个包：cn = 中文版（原包名），en = 英文版（独立包名，可与中文版共存）
    flavorDimensions += "lang"

    productFlavors {
        create("cn") {
            dimension = "lang"
            applicationId = "com.test.virtual4k"
        }
        create("en") {
            dimension = "lang"
            applicationId = "com.test.virtual4k.en"
        }
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation("dev.rikka.shizuku:api:13.1.0")
    implementation("dev.rikka.shizuku:provider:13.1.0")
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}