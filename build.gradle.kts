plugins {
    id("org.springframework.boot") version "2.2.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("java")
}

java {
    group = "hu.dhajdukis"
    version = "0.0.1-SNAPSHOT"
    sourceCompatibility = JavaVersion.VERSION_11

}
configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.hibernate.validator:hibernate-validator:6.0.2.Final")
    implementation("org.hibernate.validator:hibernate-validator-annotation-processor:6.0.2.Final")

    implementation("org.modelmapper:modelmapper:2.3.6")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    runtimeOnly("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("org.junit.vintage", "junit-vintage-engine")
    }
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
