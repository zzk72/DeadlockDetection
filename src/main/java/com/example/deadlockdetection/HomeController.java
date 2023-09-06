package com.example.deadlockdetection;

import com.example.deadlockdetection.Config.BusMsg;
import com.example.deadlockdetection.Config.MyEvent;
import com.example.deadlockdetection.ProcessNode.AddProcessDialogController;
import com.example.deadlockdetection.ProcessNode.ProcessNodeShape;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeController {

    private  Map<String,List<ResourceNodeShape> > res_to_Process_map=new HashMap<>();
    private Map<String,List<ProcessNodeShape>> process_to_res_map=new HashMap<>();

//    private List<ResourceNodeShape> resourceNodeList=null;
//    private Map<String,ResourceNodeShape> resourceNodeMap=null;
    private BorderPane root;
    public EventBus eventBus;//事件总线

    double mouseX;
    double mouseY;

    @FXML
    private MenuItem addResource;

    @FXML
    private MenuBar menuBar;

    public HomeController(){//set Event and register
//        resourceNodeMap=new HashMap<>();
//        resourceNodeList=new ArrayList<>();
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

        if(state.equals(ADD_RESOURCE_STATE)){
            addResource();
        } else if (state.equals(ADD_PROCESS_STATE)) {
            addProcess();
        }
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
    @FXML
    private void addProcess() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("addProcessDialog.fxml"));
        AnchorPane root = fxmlLoader.load();

        //set subStage controller
        AddProcessDialogController addProcessDialogController=fxmlLoader.getController();
        addProcessDialogController.setEventBus(eventBus);

        //set scene
        Scene scene = new Scene(root);
        Stage dialog = new Stage();
        addProcessDialogController.setStage(dialog);
        dialog.setScene(scene);
        dialog.show();
    }
    @Subscribe
    public void handleMsg(MyEvent event){//监听消息并分发
        JSONObject data=event.getData();
        if(event.getType().equals(BusMsg.ADD_RESOURCE)){
            paintResourceNode(data);
        }else if(event.getType().equals(BusMsg.ADD_PROCESS)){
            paintProcessNode(data);
        }
//        else if(event.getType().equals(BusMsg.ADD_EDGE)){
//            bindAndPaintEdge(event);
//        }else if(event.getType().equals(BusMsg.DELETE)){
//            bindAndDelete(event);
//        }else if(event.getType().equals(BusMsg.MOVE)){
//            bindAndMove(event);
//        }

    }
    public void paintResourceNode(JSONObject data){//监听消息，并绘制一个资源节点
        //绘制资源节点
        String resName=data.getString("resName");
        int resNum=data.getInt("resNum");
        ResourceNodeShape resourceNodeShape = new ResourceNodeShape(mouseX, mouseY, resName, resNum); // 创建一个资源节点

        //更新资源图
        res_to_Process_map.put(resName,new ArrayList<>());
        root.getChildren().add(resourceNodeShape);
    }
    public void paintProcessNode(JSONObject data){
        //绘制进程节点
        String processName=data.getString("processName");
        ProcessNodeShape processNodeShape = new ProcessNodeShape(mouseX, mouseY, processName); // 创建一个进程节点

        //更新进程图
        process_to_res_map.put(processName,new ArrayList<>());
        root.getChildren().add(processNodeShape);

    }


    private final String OFF_STATE ="off";
    private final String ADD_RESOURCE_STATE ="addResourceState";
    private final String ADD_PROCESS_STATE ="addProcessState";
    private final String ADD_EDGE_STATE="addEdgeState";
    private final String DELETE_STATE="deleteState";
    private final String MOVE_STATE="moveState";
    private String state = OFF_STATE;//记录当前状态

}