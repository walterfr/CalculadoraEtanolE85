import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.play.publisher)
}

// Signing credentials live in local.properties (not in version control) or environment variables.
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}

fun secret(name: String): String? = localProperties.getProperty(name) ?: System.getenv(name)

android {
    namespace = "com.example.calculadorae85"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.walterfr.calculadorae85"
        minSdk = 24
        targetSdk = 36
        versionCode = 2
        versionName = "1.1"
    }

    signingConfigs {
        val storePass = secret("RELEASE_STORE_PASSWORD")
        if (storePass != null && file("release.keystore").exists()) {
            create("release") {
                storeFile = file("release.keystore")
                storePassword = storePass
                keyAlias = secret("RELEASE_KEY_ALIAS") ?: "upload"
                keyPassword = secret("RELEASE_KEY_PASSWORD") ?: storePass
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.findByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
      compose = true
      aidl = false
      buildConfig = false
      shaders = false
    }

    packaging {
      resources {
        excludes += "/META-INF/{AL2.0,LGPL2.1}"
      }
    }
}

kotlin {
    jvmToolchain(17)
}

// Google Play publishing (Gradle Play Publisher).
// The service account JSON grants publishing rights: it is a secret and stays out of
// version control. Default path is app/play-service-account.json; override with
// PLAY_SERVICE_ACCOUNT_JSON in local.properties or as an environment variable.
// Nothing is sent to Play unless a publish task is invoked explicitly.
play {
    serviceAccountCredentials.set(
        file(secret("PLAY_SERVICE_ACCOUNT_JSON") ?: "play-service-account.json")
    )
    // Closed test track currently used by the app on Play.
    track.set("alpha")
    defaultToAppBundles.set(true)
}

dependencies {
  val composeBom = platform(libs.androidx.compose.bom)
  implementation(composeBom)
  androidTestImplementation(composeBom)

  // Core Android dependencies
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)

  // Arch Components
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.viewmodel.compose)

  // Compose
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.compose.material3)
  // Tooling
  debugImplementation(libs.androidx.compose.ui.tooling)
  // Instrumented tests
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  debugImplementation(libs.androidx.compose.ui.test.manifest)

  // Local tests: jUnit, coroutines, Android runner
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)

  // Instrumented tests: jUnit rules and runners
  androidTestImplementation(libs.androidx.test.core)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.androidx.test.runner)
  androidTestImplementation(libs.androidx.test.espresso.core)
}
