import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    // tikxml 라이브러리에서 ksp 지원 안함
    // 현재 gradle 버전 기준으로 alias를 사용하여 kapt를 가져오면 버전을 인식하지 못하는 문제가 있음.
    // 그래서 alias가 아닌 id 형태로 가져옴
    id(libs.plugins.jetbrains.kotlin.kapt.get().pluginId)
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.project.ibooku.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "NARU_API_KEY", properties.getProperty("NARU_API_KEY"))
        buildConfigField("String", "CENTRAL_API_KEY", properties.getProperty("CENTRAL_API_KEY"))
        buildConfigField("String", "TMAP_CLIENT_API_KEY", properties.getProperty("TMAP_CLIENT_API_KEY"))
    }

    buildFeatures {
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    kapt {
        correctErrorTypes = true
    }

    hilt{
        enableAggregatingTask = false
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // retrofit2
    implementation(libs.retrofit2)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.okhttp3.logging.interceptor)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // timber
    implementation(libs.timber)

    // sandwich
    implementation(libs.sandwich)
    implementation(libs.sandwich.retrofit)
}