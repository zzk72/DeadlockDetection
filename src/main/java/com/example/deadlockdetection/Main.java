package com.example.deadlockdetection;

import com.example.deadlockdetection.Config.BusMsg;
import com.example.deadlockdetection.Config.MyEvent;
import com.example.deadlockdetection.edge.Edge;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static java.lang.Thread.sleep;

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

        scene = new Scene(root);
        setSceneEvent(scene);
        eventBus=homeController.getEventBus();
        eventBus.register(this);
    }

    @Subscribe
    public void handleEvent(MyEvent event) throws InterruptedException {
        System.out.println("main:"+event.getType());
        JSONObject data=event.getData();
        if(event.getType().equals(BusMsg.DELETE_EDGE)){
            System.out.println("Main receive delete edge message");
            deleteEdge();
        }
    }
    private void deleteEdge() throws InterruptedException {
        for(Edge edge:homeController.getDeleteEdgeList()){
            System.out.println("delete edge:"+edge.getStartNodeName()+"->"+edge.getEndNodeName());
            edge.setVisibility(false);
            sleep(1000);
        }
    }
    public static void main(String[] args) {
        launch();
    }
}