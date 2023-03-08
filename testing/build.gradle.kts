@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.dev.maap.testing"
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
    implementation(project(":domain"))
    implementation(project(":model"))

    /**
     * composeBom
     */
    val composeBom = platform(libs.androidx.compose.bom)
    api(composeBom)
    api(libs.androidx.compose.ui.test.junit)
    debugApi(libs.androidx.compose.ui.test.manifest)
    debugApi(libs.androidx.compose.ui.tooling)

    /**
     * dependency inject
     */
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.android.compiler)
    api(libs.hilt.android.test)

    /**
     * datetime
     */
    implementation(libs.kotlinx.datetime)

    /**
     * test libraries
     * (Use api instead of implementation.
     * Because when testing, to import test library dependencies from other modules.)
     */
    api(libs.androidx.test.core.ktx)
    api(libs.androidx.test.espresso.core)
    api(libs.androidx.test.ext.junit)
    api(libs.androidx.test.runner)
    api(libs.junit)
    api(libs.kotlin.coroutines.test)
    api(libs.kotlin.test)
    api(libs.room.testing)
    api(libs.turbine)
}