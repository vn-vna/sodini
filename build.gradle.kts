plugins {
    id("java")
}

group = "tk.vnvna"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // JUnit Test
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    // JDA
    implementation("net.dv8tion:JDA:5.0.0-beta.3")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    testCompileOnly("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")

    // Reflections
    implementation("org.reflections:reflections:0.10.2")

    // Json
    implementation("org.json:json:20220924")

    // SLF4J
    implementation(group = "org.apache.logging.log4j", name = "log4j-api", version = "2.1")
    implementation(group = "org.apache.logging.log4j", name = "log4j-core", version = "2.3.2")
    implementation(group = "org.apache.logging.log4j", name = "log4j-slf4j-impl", version = "2.1")

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}