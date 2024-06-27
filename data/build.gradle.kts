plugins {
    id("java-library")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.jvm")
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
//    implementation("androidx.room:room-common:2.6.1")
//    implementation("androidx.room:room-runtime:2.6.1")
//    implementation("androidx.room:room-ktx:2.6.1")
//    kapt("androidx.room:room-compiler:2.6.1")
    // Data store preferences
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}
