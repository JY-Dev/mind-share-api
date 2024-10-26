dependencies {

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

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
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    testImplementation("org.springframework.security:spring-security-test")
}