package org.fb.kdiff.ui

import javafx.fxml.FXML
import javafx.scene.control.Label
import org.springframework.stereotype.Component
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import java.util.*
import java.util.jar.Manifest


@Component
class KdiffController {

    @FXML
    private lateinit var manifest: Label

    @FXML
    fun initialize() {
        val resources: Enumeration<URL> = Thread.currentThread().contextClassLoader
                .getResources("META-INF/MANIFEST.MF")


        val protectionDomain = this::javaClass.javaClass.protectionDomain
        val codeSource = protectionDomain.codeSource
        val location = codeSource.location
        val path = location.path
        println("path $path")
        var sub = path.substring(1, path.indexOf(".jar") + 4)
        println("sub $sub")

        sub = sub.substring(sub.lastIndexOf("/") + 1)
        println("sub $sub")

        val other = "${sub}!/META-INF/MANIFEST.MF"
        println(other)
        val rls = mutableListOf<URL>()
        for (rr in resources) {
            if (rr.path.contains(other))
                rls.add(rr)
        }

        for (rl in rls)
            println(rl)

        val urlConnection: URLConnection = rls[0].openConnection()
        val iss: InputStream = urlConnection.getInputStream()

        val m = Manifest(iss)

        manifest.text = m.mainAttributes
                .map { (key, value) -> "${key}:\t$value" }
                .joinToString(prefix = "Manifest\n", separator = "\n")
    }
}