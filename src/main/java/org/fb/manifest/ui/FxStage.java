package org.fb.manifest.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.fb.manifest.StageReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
class FxStage implements ApplicationListener<StageReadyEvent> {
    private final ApplicationContext context;
    private Stage stage;

    FxStage(ApplicationContext context) {
        this.context = context;
    }

    public void onApplicationEvent(StageReadyEvent event) {
        try {

            stage = event.getStage();
            FXMLLoader fxmlLoader = new FXMLLoader(new ClassPathResource("manifest.fxml").getURL());
            fxmlLoader.setControllerFactory(param -> context.getBean(param));

            Parent parent = (Parent) fxmlLoader.load();
            stage.setScene(new Scene(parent));
            stage.setTitle("Folder diff");
            stage.show();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}