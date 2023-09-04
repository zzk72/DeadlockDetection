package com.example.deadlockdetection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    public EventBus eventBus;

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

    @FXML
    void OnAddResource(ActionEvent event) {
        stutus=ADD_RESOURCE;
    }

    @FXML
    void cavasOnDragDected(MouseEvent event) {

    }

    @FXML
    void cavasOnDragDone(DragEvent event) {

    }

    @FXML
    void cavasOnDragDropped(DragEvent event) {

    }

    @FXML
    void cavasOnDragEntered(DragEvent event) {

    }

    @FXML
    void cavasOnDragExited(DragEvent event) {

    }

    @FXML
    void cavasOnDragOver(DragEvent event) {

    }

    @FXML
    void cavasOnMouseDragEntered(MouseDragEvent event) {

    }

    @FXML
    void cavasOnMouseDragExited(MouseDragEvent event) {

    }

    @FXML
    void cavasOnMouseDragOver(MouseDragEvent event) {

    }

    @FXML
    void cavasOnMouseDragReleased(MouseDragEvent event) {

    }
    @FXML
    void canvasOnMouseClicked(MouseEvent event) throws IOException {
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
        }
    }

}
