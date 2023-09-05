package com.example.deadlockdetection;

import atlantafx.base.theme.PrimerLight;
import atlantafx.base.theme.NordDark;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
public class DDMain extends Application {
    private double mouseX;
    private double mouseY;
    private Scene scene;
    private Canvas canvas;
    private GraphicsContext gc;

    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        setObject();
        update(gc);
        stage.setTitle("DDA");
        stage.setScene(scene);
        stage.show();
    }

    private void setSceneEvent(Scene scene){//设置场景事件
        scene.setOnMouseDragged(event -> {
            mouseX=event.getX();
            mouseY=event.getY();
            //调整mouseX和mouseY在子组件中的坐标 work
            Bounds bounds=canvas.localToScene(canvas.getBoundsInLocal());
            mouseX-=bounds.getMinX();
            mouseY-=bounds.getMinY();

        });
    }
    private void paint(GraphicsContext gc) {//param: GraphicsContext is 画笔
        gc.setFill(javafx.scene.paint.Color.RED);//设置填充颜色
        gc.fillRect(0,0,mouseX,mouseY);//填充
    }
    private void update(GraphicsContext gc){//更新画布
        AnimationTimer timer=new AnimationTimer() {
            @Override
            public void handle(long now) {//重写handle方法，屏幕每刷新一次就会调用一次该方法
                gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
                paint(gc);
            }
        };
        timer.start();
    }

    private void setObject() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(DDMain.class.getResource("homeView.fxml"));
        BorderPane root = fxmlLoader.load();
        DDController controller = fxmlLoader.getController();
        controller.setRoot(root);
        canvas=(Canvas)root.lookup("#canvas");
        gc=canvas.getGraphicsContext2D();
        scene = new Scene(root);
        setSceneEvent(scene);
    }



    public static void main(String[] args) {
        launch();
    }
}