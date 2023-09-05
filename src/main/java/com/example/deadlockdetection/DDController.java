package com.example.deadlockdetection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import com.google.common.eventbus.Subscribe;
import com.google.common.eventbus.EventBus;

import java.io.IOException;

public class DDController {

    private final String OFF="off";
    private final String ADD_RESOURCE="addResource";
    private final String ADD_PROCESS="addProcess";
    private final String ADD_EDGE="addEdge";
    private final String DELETE="delete";
    private final String MOVE="move";
    private String stutus=OFF;//记录当前状态

    private BorderPane root;
    public EventBus eventBus;//事件总线

    double mouseX;
    double mouseY;

    @FXML
    private MenuItem addResource;

    @FXML
    private Canvas canvas;

    @FXML
    private MenuBar menuBar;

    public DDController(){//set Event and register
        eventBus=new EventBus();
        eventBus.register(this);
    }

    public void setRoot(BorderPane root) {
        this.root = root;
    }
    @FXML
    void OnAddResource(ActionEvent event) {
        stutus=ADD_RESOURCE;
    }

    @FXML
    void cavasOnDragDected(MouseEvent event) {//鼠标拖拽检测

    }

    @FXML
    void cavasOnDragDone(DragEvent event) {//鼠标拖拽完成

    }

    @FXML
    void cavasOnDragDropped(DragEvent event) {//鼠标拖拽释放

    }

    @FXML
    void cavasOnDragEntered(DragEvent event) {

    }

    @FXML
    void cavasOnDragExited(DragEvent event) {

    }

    @FXML
    void cavasOnDragOver(DragEvent event) {//鼠标拖拽在画布上


    }

    @FXML
    void cavasOnMouseDragEntered(MouseDragEvent event) {//鼠标拖拽进入画布

    }

    @FXML
    void cavasOnMouseDragExited(MouseDragEvent event) {//鼠标拖拽离开画布


    }

    @FXML
    void cavasOnMouseDragOver(MouseDragEvent event) {

    }

    @FXML
    void cavasOnMouseDragReleased(MouseDragEvent event) {

    }
    @FXML
    void canvasOnMouseClicked(MouseEvent event) throws IOException {
        Bounds ofParent = root.getBoundsInParent();
        //获取鼠标点击点的坐标
        mouseX=event.getX();
        mouseY=event.getY();
        //调整mouseX和mouseY在子组件中的坐标 not work
        mouseX=mouseX-ofParent.getMinX();
        mouseY=mouseY-ofParent.getMinY();
        if(stutus.equals(ADD_RESOURCE)){
            FXMLLoader fxmlLoader = new FXMLLoader(DDMain.class.getResource("addResourceDialog.fxml"));
            AnchorPane root = fxmlLoader.load();

            //set subStage controller
            AddResourceDialogController addResourceDialogController=fxmlLoader.getController();
            addResourceDialogController.setEventBus(eventBus);

            //set scene
            Scene scene = new Scene(root);
            Stage dialog = new Stage();
            dialog.setScene(scene);
            dialog.show();
        }
        System.out.println("canvasOnMouseClicked");
        stutus=OFF;
    }
    @Subscribe
    public void addResourceBtnYOnAction(MyEvent event){
        if(event.getType().equals("addResourceBtnYOnAction")){
            System.out.println("addResourceBtnYOnAction");
            System.out.println(event.getData());

            //在鼠标点击点画一个圆
            //在圆上写上资源名
            //在圆下写上资源数
            Circle circle=new Circle();
            circle.setCenterX(mouseX);
            circle.setCenterY(mouseY);
            circle.setRadius(20);
            root.getChildren().add(circle);
        }

    }

}
