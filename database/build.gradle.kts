@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.dev.maap.database"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "com.dev.maap.testing.HiltTestRunner"
    }
}

dependencies {
    /**
     * other modules
     */
    implementation(project(":model"))
    implementation(project(":domain"))

    /**
     * sqlite (compile with rtree)
     */
    implementation(files("libs/sqlite-android-3410000.aar"))

    /**
     * room
     */
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    /**
     * hilt
     */
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    /**
     * test
     */
    androidTestImplementation(project(":testing"))
    kaptAndroidTest(libs.hilt.android.compiler)
}