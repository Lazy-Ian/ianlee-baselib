plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.ianlee.lazy.base.lib"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        viewBinding = true
        buildConfig = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    sourceSets{
        getByName("main"){
            jniLibs.srcDirs("jniLibs")
            res.srcDirs(arrayOf("src/main/res/xpopup","src/main/res/picker","src/main/res"))
        }
    }


}

dependencies {
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)
    implementation(libs.androidx.viewbinding)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    api(libs.mmkv)
    api(libs.okgo)
    api(libs.retrofit2)
    api(libs.retrofit2.converter.gson)
    api(libs.github.glide)

    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.androidx.lifecycle.livedata.ktx)
    api(libs.androidx.lifecycle.common.java8)
    api(libs.androidx.lifecycle.runtime.ktx.v20)
    api(libs.androidx.lifecycle.extensions)
    api(libs.androidx.recyclerview)
    api(libs.androidx.constraintlayout)
    api(libs.easypermissions)
    api(libs.subsampling.scale.image.view.androidx)
//    api(files("libs/autosize.aar"))
    annotationProcessor(libs.androidx.room.room.compiler)
    api (libs.androidx.room.room.runtime)
    api(libs.live.event.bus.x)
    api(libs.flexbox)
    api(project(":autosize"))


}