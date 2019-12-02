package org.fb.kdiff.ui

import javafx.fxml.FXML
import javafx.scene.control.Label
import org.fb.kdiff.api.manifest.ManifestService
import org.springframework.stereotype.Component


@Component
class ManifestController(private val manifestService: ManifestService) {

    @FXML
    private lateinit var manifest: Label

    @FXML
    fun initialize() {

        manifest.text = manifestService.manifest()
//        val jarName = currentJar()
//        val other = "${jarName}!/META-INF/MANIFEST.MF"
//
//        val candidates = candidates()
//        val filter = candidates.firstOrNull() { it.path.contains(other) }
//
//        if (filter != null) {
//            val urlConnection: URLConnection = filter.openConnection()
//            val iss: InputStream = urlConnection.getInputStream()
//
//            manifest.text = Manifest(iss).mainAttributes
//                    .map { (key, value) -> "${key}:\t$value" }
//                    .joinToString(prefix = "Manifest\n", separator = "\n")
//        } else {
//            manifest.text = "No Manifest found"
//        }
    }

//    private fun candidates(): List<URL> {
//        val urls1 = Thread.currentThread().contextClassLoader.getResources("/META-INF/MANIFEST.MF").toList()
//        val urls2 = Thread.currentThread().contextClassLoader.getResources("META-INF/MANIFEST.MF").toList()
//
//        return urls2 - urls1
//    }
//
//    fun currentJar(): String {
//        val path = this::javaClass.javaClass.protectionDomain.codeSource.location.path
//        val sub = path.substring(1, path.indexOf(".jar") + ".jar".length)
//        return sub.substring(sub.lastIndexOf("/") + 1)
//    }
}