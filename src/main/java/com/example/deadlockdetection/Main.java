package com.example.deadlockdetection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
public class Main extends Application {
    private double mouseX;//相对于root的坐标
    private double mouseY;//相对于root的坐标
    private Scene scene;
    private BorderPane root;

    private Bounds rootBounds;

    @Override
    public void start(Stage primaryStage) throws IOException {

        //Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        setObject();
        primaryStage.setTitle("DDA");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setSceneEvent(Scene scene){//设置场景事件
        scene.setOnMouseDragged(event -> {
            mouseX=event.getX();
            mouseY=event.getY();
        });
        scene.setOnMouseMoved(event -> {
            mouseX=event.getX();
            mouseY=event.getY();
        });
        scene.setOnMouseClicked(event -> {
            mouseX=event.getX();
            mouseY=event.getY();
        });
    }


    private void setObject() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("homeView.fxml"));
        //设置根节点
         root = fxmlLoader.load();
         rootBounds=root.getBoundsInParent();
        //设置子组件的controller
        HomeController controller = fxmlLoader.getController();
        controller.setRoot(root);

        scene = new Scene(root);
        setSceneEvent(scene);
    }



    public static void main(String[] args) {
        launch();
    }
}