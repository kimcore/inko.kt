group = "com.github.kimcore"
version = "1.0"

repositories {
    jcenter()
}

plugins {
    kotlin("jvm") version "1.3.70"
}

dependencies {
    implementation(kotlin("stdlib"))
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}
