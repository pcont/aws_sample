package org.fb.manifest;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class KdiffApplication extends Application {

    private ConfigurableApplicationContext context;

    public void init() {
        context = new SpringApplicationBuilder()
                .sources(Main.class)
                .run();
    }

    public void start(Stage primaryStage) {
        context.publishEvent(new StageReadyEvent(primaryStage));
    }

    public void stop() {
        context.close();
        Platform.exit();
    }
}