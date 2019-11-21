package org.fb.kdiff.ui

import javafx.fxml.FXML
import javafx.scene.control.Label
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.util.jar.Manifest

@Component
class KdiffController {

    @FXML
    private lateinit var manifest: Label

    @FXML
    fun initialize() {
//        val r = ClassPathResource("/META-INF/frbo.mf")
        val r = ClassPathResource("/META-INF/MANIFEST.MF")
        val m = Manifest(r.inputStream)

        manifest.text = m.mainAttributes
                .map { (key, value) -> "${key}:\t$value" }
                .joinToString(prefix = "Manifest\n", separator = "\n")
    }
}