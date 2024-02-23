plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //Hilt
    kotlin("kapt")
    id("com.google.dagger.hilt.android")

    // for ksp
    id("com.google.devtools.ksp")

    id("androidx.navigation.safeargs")//for safeargs
}

android {
    namespace = "com.example.homework26_maps"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.homework26_maps"
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
            buildConfigField("String","BASE_URL_POST","\"https://rameRealuri\"")
            buildConfigField("String","BASE_URL_STORY","\"https://run.mocky.io/v3/\"")
        }
        debug {
            buildConfigField("String","BASE_URL_POST","\"https://run.mocky.io/v3/\"")
            buildConfigField("String","BASE_URL_STORY","\"https://run.mocky.io/v3/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
}
//Hilt
// Allow references to generated code
kapt {
    correctErrorTypes = true
}

dependencies {

    implementation("com.google.android.gms:play-services-maps:18.2.0")// for google maps
    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation ("com.google.android.libraries.places:places:3.3.0")// for places search

    implementation("androidx.work:work-runtime-ktx:2.9.0")//for workManager
    implementation("androidx.hilt:hilt-work:1.2.0")

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    ksp("androidx.room:room-compiler:2.6.1")// for room

    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")// for okHttp logger interceptor


    //Datastore
    implementation("androidx.datastore:datastore-preferences-core:1.0.0")// for DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.49")
    kapt("com.google.dagger:hilt-android-compiler:2.47")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0") // Retrofit

    implementation ("com.squareup.moshi:moshi:1.9.3") // Moshi
    implementation ("com.squareup.moshi:moshi-kotlin:1.9.3") // For Kotlin support

    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")// Retrofit Converter for Moshi



    implementation("com.github.bumptech.glide:glide:4.16.0") // for glide


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1") // coroutines


    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0") // For ViewModel
    implementation ("androidx.activity:activity-ktx:1.8.2") // For ViewModel

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7") // for navigation
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")// for navigation

    implementation("androidx.fragment:fragment-ktx:1.6.2")//for fragment

    implementation("androidx.recyclerview:recyclerview-selection:1.1.0") // for recycler selection



    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation("junit:junit:4.13.2")
    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2") // visible only for runtime (depandancy)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation("io.mockk:mockk:1.13.2")
    // For Testing Kotlin Flows
    testImplementation("app.cash.turbine:turbine:0.9.0")
    // fort Truth assertion library
    testImplementation("com.google.truth:truth:1.1.3")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}