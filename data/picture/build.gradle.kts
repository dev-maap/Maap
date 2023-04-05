@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.dev.maap.data.picture"
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
    implementation(project(":database"))

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