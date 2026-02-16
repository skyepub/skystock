package com.skytree.skystock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SkyStockApplication

fun main(args: Array<String>) {
    runApplication<SkyStockApplication>(*args)
}
