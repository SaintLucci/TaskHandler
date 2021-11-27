package com.example.taskhandler;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class TaskHandlerApplication extends Application {
    @Override
    public void start(Stage stage){
        Region SceneRoot = new GuiBuilder().build();
        Scene scene = new Scene(SceneRoot);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Task Handler");
        stage.setHeight(400*1.2);
        stage.setWidth(600*1.2);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
