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
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = "5.8.1")
    testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = "5.8.1")

    // JDA
    implementation(group = "net.dv8tion", name = "JDA", version = "5.0.0-beta.3")

    // Lombok
    compileOnly(group = "org.projectlombok", name = "lombok", version = "1.18.24")
    annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.24")
    testCompileOnly(group = "org.projectlombok", name = "lombok", version = "1.18.24")
    testAnnotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.24")

    // Reflections
    implementation(group = "org.reflections", name = "reflections", version = "0.10.2")

    // Json
    implementation(group = "javax.json", name = "javax.json-api", version = "1.1")
    implementation(group = "org.glassfish", name = "javax.json", version = "1.1")
    implementation(group = "com.google.code.gson", name = "gson", version = "2.10.1")

    // Socket server
    implementation(group = "org.glassfish.tyrus", name = "tyrus-server", version = "2.1.3")
    implementation(group = "org.glassfish.tyrus", name = "tyrus-client", version = "2.1.3")
    implementation(group = "org.glassfish.tyrus", name = "tyrus-container-grizzly-server", version = "2.1.3")

    // SLF4J
    implementation(group = "org.apache.logging.log4j", name = "log4j-api", version = "2.1")
    implementation(group = "org.apache.logging.log4j", name = "log4j-core", version = "2.3.2")
    implementation(group = "org.apache.logging.log4j", name = "log4j-slf4j-impl", version = "2.1")

    // Glassfish Jersey
    implementation(group = "org.glassfish.jersey.inject", name = "jersey-hk2", version = "3.1.0")
    implementation(group = "org.glassfish.jersey.core", name = "jersey-client", version = "3.1.0")
    implementation(group = "org.glassfish.jersey.media", name = "jersey-media-json-jackson", version = "3.1.0")

    // Manifold EXT
    compileOnly(group = "systems.manifold", name = "manifold-ext", version = "2022.1.38")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.getByName<JavaCompile>("compileJava") {
    options.compilerArgs.add("-parameters")
}
