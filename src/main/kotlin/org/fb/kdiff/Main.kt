package org.fb.kdiff

import javafx.application.Application
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Main

fun main(args: Array<String>) {
    Application.launch(KdiffApplication::class.java, *args)
}
