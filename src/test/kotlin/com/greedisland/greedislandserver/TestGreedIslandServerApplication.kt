package com.greedisland.greedislandserver

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<GreedIslandServerApplication>().with(TestcontainersConfiguration::class).run(*args)
}
