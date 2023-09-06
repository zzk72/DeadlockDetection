package com.example.deadlockdetection;

import com.example.deadlockdetection.Config.BusMsg;
import com.example.deadlockdetection.Config.MyEvent;
import com.example.deadlockdetection.ResourceNode.AddResourceDialogController;
import com.example.deadlockdetection.ResourceNode.ResourceNodeShape;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import com.google.common.eventbus.Subscribe;
import com.google.common.eventbus.EventBus;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class HomeController {


    private List<ResourceNodeShape> resourceNodeList;
    private BorderPane root;
    public EventBus eventBus;//事件总线

    double mouseX;
    double mouseY;

    @FXML
    private MenuItem addResource;

    @FXML
    private MenuBar menuBar;

    public HomeController(){//set Event and register
        eventBus=new EventBus();
        eventBus.register(this);
    }

    public void setRoot(BorderPane root) {
        this.root = root;
    }
    @FXML
    void OnAddResource(ActionEvent event) {
        state = ADD_RESOURCE_STATE;
    }
    public void OnAddProcess(ActionEvent actionEvent) {
        state = ADD_PROCESS_STATE;
    }

    @FXML
    void rootOnMouseClicked(MouseEvent event) throws IOException {
        Bounds ofParent=root.getBoundsInParent();
        //获取canvas在root中的坐标
       // Bounds ofParent = canvas.localToScene(canvas.getBoundsInLocal());
        //获取鼠标点击点的坐标
        mouseX=event.getX();
        mouseY=event.getY();
        //调整mouseX和mouseY在子组件中的坐标 not work 因为获取的是canvas的坐标,而不是root的坐标减去的是root的坐标
        mouseX=mouseX-ofParent.getMinX();
        mouseY=mouseY-ofParent.getMinY();

        switch (state){
            case ADD_RESOURCE_STATE:
                addResource();
                break;
            case ADD_PROCESS_STATE:
                addProcess();
        }

//        if(state.equals(ADD_RESOURCE_STATE)){
//            addResource();
//        } else if (state.equals(ADD_PROCESS_STATE)) {
//            addProcess();
//
//        }
        state = OFF_STATE;
    }
    private void addResource() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("addResourceDialog.fxml"));
        AnchorPane root = fxmlLoader.load();

        //set subStage controller
        AddResourceDialogController addResourceDialogController=fxmlLoader.getController();
        addResourceDialogController.setEventBus(eventBus);

        //set scene
        Scene scene = new Scene(root);
        Stage dialog = new Stage();
        addResourceDialogController.setStage(dialog);
        dialog.setScene(scene);
        dialog.show();
    }
    private void addProcess() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("addProcessDialog.fxml"));
        AnchorPane root = fxmlLoader.load();

        //set subStage controller
        AddResourceDialogController addResourceDialogController=fxmlLoader.getController();
        addResourceDialogController.setEventBus(eventBus);

        //set scene
        Scene scene = new Scene(root);
        Stage dialog = new Stage();
        addResourceDialogController.setStage(dialog);
        dialog.setScene(scene);
        dialog.show();
    }
    @Subscribe
    public void bindAndPaintResourceNode(MyEvent event){//监听消息，并绘制一个资源节点
        if(event.getType().equals(BusMsg.ADD_RESOURCE)){
            JSONObject data=event.getData();
            //绘制资源节点
            String resName=data.getString("resName");
            int resNum=data.getInt("resNum");
            ResourceNodeShape resourceNodeShape = new ResourceNodeShape(mouseX, mouseY, resName, resNum); // 创建一个资源节点
            resourceNodeList.add(resourceNodeShape);
            root.getChildren().add(resourceNodeShape);
        }
    }
    @Subscribe
    public void bindAndPaintProcessNode(MyEvent event){
        if(event.getType().equals(BusMsg.ADD_PROCESS)){
            System.out.println(BusMsg.ADD_PROCESS);
            System.out.println(event.getData());
        }
    }


    private final String OFF_STATE ="off";
    private final String ADD_RESOURCE_STATE ="addResourceState";
    private final String ADD_PROCESS_STATE ="addProcessState";
    private final String ADD_EDGE_STATE="addEdgeState";
    private final String DELETE_STATE="deleteState";
    private final String MOVE_STATE="moveState";
    private String state = OFF_STATE;//记录当前状态

}
