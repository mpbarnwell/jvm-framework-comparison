import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val grpcVersion by extra { "1.16.1" }

plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm") version "1.4.10"
    id("com.google.protobuf").version("0.8.13")
}

repositories {
    jcenter()
}

dependencies {
    // Use the Kotlin JDK 8 standard library
    implementation("io.ratpack:ratpack-guice:1.6.0")
    implementation("io.grpc:grpc-netty:$grpcVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.netty:netty-transport-native-epoll:4.1.32.Final")
    implementation("io.netty:netty-transport-native-kqueue:4.1.32.Final")
    // need this for JsonFormat
    implementation("com.google.protobuf:protobuf-java-util:3.6.1")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    testImplementation("io.grpc:grpc-testing:$grpcVersion")
    testImplementation("io.ratpack:ratpack-test:1.6.0")

    // TODO testing example in kotlin: https://github.com/junit-team/junit5-samples/blob/r5.3.2/junit5-jupiter-starter-gradle-kotlin/src/test/kotlin/com/example/project/CalculatorTests.kt

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")

    testImplementation("ch.qos.logback:logback-classic:1.2.3")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

group = "com.bumble.ratpack"
version = "1.0"

publishing {
    publications {
        create<MavenPublication>("maven")
    }
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
        }
        compileTestKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xopt-in=kotlin.time.ExperimentalTime")
            }
        }
    }
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("PASSED","SKIPPED", "FAILED")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.6.1"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.16.1"
        }
    }
    generateProtoTasks {
        ofSourceSet("test").forEach {
            it.plugins {
                id("grpc") {
                    outputSubDir = "java"
                }
            }
        }
    }
    // TODO hack so that tests can find generated class files
    generatedFilesBaseDir = "src"
}
