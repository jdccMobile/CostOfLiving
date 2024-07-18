plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("com.google.devtools.ksp")
    id("io.gitlab.arturbosch.detekt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":domain"))
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    // Room
    implementation("androidx.room:room-common:2.7.0-alpha04")
    implementation("androidx.room:room-runtime:2.7.0-alpha04")
    ksp("androidx.room:room-compiler:2.7.0-alpha04")
    // Data store preferences
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Either
    implementation("io.arrow-kt:arrow-core:1.1.2")
}
