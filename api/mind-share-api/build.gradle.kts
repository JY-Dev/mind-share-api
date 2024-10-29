dependencies {

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Domain
    implementation(project(":core:jpa"))
    implementation(project(":domain:auth"))
    implementation(project(":domain:user"))
    implementation(project(":domain:post"))

    //db
    runtimeOnly("com.mysql:mysql-connector-j")

    // jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    //flyway
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    // queryDsl
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")

    //OpenApi
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly ("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly ("io.jsonwebtoken:jjwt-jackson:0.12.6")

    implementation("org.redisson:redisson-spring-boot-starter:3.37.0")

    implementation("commons-codec:commons-codec:1.17.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // 코루틴 테스트 라이브러리
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:testcontainers:1.19.0")
}