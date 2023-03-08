// for testing
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

// for testing
android {
    namespace = "com.dev.maap.domain"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}

dependencies {
    // Do not implement other modules except model in domain layer.
    implementation(project(":model"))

    /**
     * dependency inject
     * (Domain layer uses javax.inject instead of android hilt.)
     */
    implementation(libs.javax.inject)

    /**
     * coroutines
     * (Do not implementation coroutines.android from domain layer.)
     */
    implementation(libs.kotlinx.coroutines.core)

    /**
     * datetime
     */
    implementation(libs.kotlinx.datetime)

    /**
     * test
     */
    testImplementation(project(":testing"))
}