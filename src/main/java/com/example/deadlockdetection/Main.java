package com.example.deadlockdetection;

import com.example.deadlockdetection.Config.BusMsg;
import com.example.deadlockdetection.Config.MyEvent;
import com.example.deadlockdetection.edge.Edge;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.util.Duration;

public class Main extends Application {
    private double mouseX;//相对于root的坐标
    private double mouseY;//相对于root的坐标
    private Scene scene;
    private BorderPane root;
    HomeController homeController;
    EventBus eventBus;
    private Bounds rootBounds;

    @Override
    public void start(Stage primaryStage) throws IOException {

        //Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        setObject();
        primaryStage.setTitle("DDA");

        primaryStage.setScene(scene);
        primaryStage.show();

    }
//    public void start(Stage primaryStage) throws IOException {
//        Group root = new Group();
//        Scene scene = new Scene(root, 400, 400);
//
//        // 创建多个线段作为edge，并将它们添加到root中
//        Line edge1 = new Line(50, 50, 200, 50);
//        Line edge2 = new Line(50, 100, 200, 100);
//        Line edge3 = new Line(50, 150, 200, 150);
//        root.getChildren().addAll(edge1, edge2, edge3);
//
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
//        // 创建一个Timeline对象
//        Timeline timeline = new Timeline();
//
//        // 创建多个KeyFrame，每隔0.5秒将一个edge的可见性设置为false
//        Duration duration = Duration.millis(500);
//        KeyFrame keyFrame1 = new KeyFrame(duration, e -> edge1.setVisible(false));
//        KeyFrame keyFrame2 = new KeyFrame(duration.multiply(2), e -> edge2.setVisible(false));
//        KeyFrame keyFrame3 = new KeyFrame(duration.multiply(3), e -> edge3.setVisible(false));
//
//        timeline.getKeyFrames().addAll(keyFrame1, keyFrame2, keyFrame3);
//
//        // 设置循环次数，这里设置为Timeline.INDEFINITE表示无限次循环
//        timeline.setCycleCount(Timeline.INDEFINITE);
//
//        // 启动动画
//        timeline.play();
//    }

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
        homeController = fxmlLoader.getController();
        homeController.setRoot(root);
        //改成手动按钮的形式？
        scene = new Scene(root);
        setSceneEvent(scene);
        eventBus=homeController.getEventBus();
        eventBus.register(this);
    }


    public static void main(String[] args) {
        launch();
    }
}