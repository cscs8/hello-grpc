val kotlinVersion by extra("1.3.61")
val grpcVersion by extra("1.25.0")   // CURRENT_GRPC_VERSION

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    kotlin("jvm") version "1.3.61" apply false

    idea

    id("org.owasp.dependencycheck") version "5.2.4"

    id("de.fayard.refreshVersions") version "0.8.6"

    application

    id("com.google.protobuf") version "0.8.11"
}
tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<AbstractCompile> {
        sourceCompatibility = JavaVersion.VERSION_1_8.majorVersion
        targetCompatibility = JavaVersion.VERSION_1_8.majorVersion
    }

    named<DefaultTask>("check") {
        dependsOn(dependencyCheckAggregate)
        dependsOn(rootProject.tasks.refreshVersions)
    }

    wrapper {
        gradleVersion = "6.0.1"
        distributionType = Wrapper.DistributionType.ALL
    }


    dependencyCheck {
        analyzers(delegateClosureOf<org.owasp.dependencycheck.gradle.extension.AnalyzerExtension> {
            assemblyEnabled = false
            nugetconfEnabled = false
            nuspecEnabled = false
            nodeEnabled = false
            nodeAuditEnabled = false
            swiftEnabled = false
            bundleAuditEnabled = false
            rubygemsEnabled = false
            golangDepEnabled = false
            golangModEnabled = false
            pyDistributionEnabled = false
            pyPackageEnabled = false
        })
    }
}


dependencies {

    //    val kotlinVersion: String by rootProject.extra
//    val grpcVersion: String by rootProject.extra   // CURRENT_GRPC_VERSION
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib-jdk8"))

    implementation(platform("io.grpc:grpc-bom"))
    implementation("io.grpc:grpc-netty-shaded")
    implementation("io.grpc:grpc-protobuf")
    implementation("io.grpc:grpc-stub")
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")

    testImplementation("io.grpc:grpc-testing")  // gRCP testing utilities
    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:3.2.0")
}
apply(from = "build-grpc.gradle.kts")
