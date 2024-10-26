package com.mindshare

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class MindShareApplication

fun main(args: Array<String>) {
    runApplication<MindShareApplication>(*args)
}
