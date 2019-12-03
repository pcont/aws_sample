package org.fb.kdiff.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.fb.kdiff.api.manifest.ManifestService;
import org.springframework.stereotype.Controller;

@Controller
public class ManifestController {

    private final ManifestService manifestService;

    @FXML
    private Label manifest;

    public ManifestController(ManifestService manifestService) {
        this.manifestService = manifestService;
    }

    @FXML
    void initialize() {
        manifest.setText(manifestService.manifest());
    }
}