package org.fb.kdiff.ui

import javafx.fxml.FXML
import javafx.scene.control.Label
import org.springframework.stereotype.Component
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import java.util.jar.Manifest


@Component
class KdiffController {

    @FXML
    private lateinit var manifest: Label

    @FXML
    fun initialize() {

        val jarName = currentJar()
        val other = "${jarName}!/META-INF/MANIFEST.MF"

        val candidates = candidates()
        val filter = candidates.first { it.path.contains(other) }

        val urlConnection: URLConnection = filter.openConnection()
        val iss: InputStream = urlConnection.getInputStream()

        manifest.text = Manifest(iss).mainAttributes
                .map { (key, value) -> "${key}:\t$value" }
                .joinToString(prefix = "Manifest\n", separator = "\n")
    }

    private fun candidates(): List<URL> {
        val urls1 = Thread.currentThread().contextClassLoader.getResources("/META-INF/MANIFEST.MF").toList()
        val urls2 = Thread.currentThread().contextClassLoader.getResources("META-INF/MANIFEST.MF").toList()

        return urls2 - urls1
    }

    private fun currentJar(): String {
        val path = this::javaClass.javaClass.protectionDomain.codeSource.location.path
        val sub = path.substring(1, path.indexOf(".jar") + ".jar".length)
        return sub.substring(sub.lastIndexOf("/") + 1)
    }
}