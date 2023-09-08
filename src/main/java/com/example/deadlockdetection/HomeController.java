package com.example.deadlockdetection;

import com.example.deadlockdetection.Config.BusMsg;
import com.example.deadlockdetection.Config.MyEvent;
import com.example.deadlockdetection.Config.Point;
import com.example.deadlockdetection.ProcessNode.AddProcessDialogController;
import com.example.deadlockdetection.ProcessNode.ProcessNodeShape;
import com.example.deadlockdetection.ResourceNode.AddResourceDialogController;
import com.example.deadlockdetection.ResourceNode.ResourceNodeShape;
import com.example.deadlockdetection.edge.EdgeArrowShape;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import com.google.common.eventbus.Subscribe;
import com.google.common.eventbus.EventBus;
import lombok.Data;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class HomeController {


    private BorderPane root;
    public EventBus eventBus;//事件总线

    private double mouseX;
    private double mouseY;
    private ArrayList<JSONObject> node2node=new ArrayList<>();
    private  Map<String,List<ProcessNodeShape> > res_to_Process_map=new HashMap<>();
    private Map<String,List<ResourceNodeShape>> process_to_res_map=new HashMap<>();
    private  Map<String,ResourceNodeShape> res_map=new HashMap<>();
    private  Map<String,ProcessNodeShape> process_map=new HashMap<>();

    @FXML
    private MenuItem addResource;
    @FXML
    private MenuBar menuBar;
    @FXML
    public MenuItem addEdge;



    @FXML
    void OnAddResource(ActionEvent event) {
        state = ADD_RESOURCE_STATE;
    }
    @FXML
    void OnAddProcess(ActionEvent actionEvent) {
        state = ADD_PROCESS_STATE;
    }
    @FXML
    public void OnAddEdge(ActionEvent actionEvent) {
        state = ADD_EDGE_STATE1;
        node2node.clear();
    }
    @FXML
    void rootOnMouseClicked(MouseEvent event) throws IOException {
        Bounds ofParent=root.getBoundsInParent();
        //获取canvas在root中的坐标
       // Bounds ofParent = canvas.localToScene(canvas.getBoundsInLocal());
        //获取鼠标点击点的坐标
        mouseX=event.getX();
        mouseY=event.getY();
        //调整mouseX和mouseY在子组件中的坐标 (not work 因为获取的是canvas的坐标,而不是root的坐标减去的是root的坐标)
        mouseX=mouseX-ofParent.getMinX();
        mouseY=mouseY-ofParent.getMinY();

        if(state.equals(ADD_RESOURCE_STATE)){
            addResource();
            state = OFF_STATE;
        } else if (state.equals(ADD_PROCESS_STATE)) {
            addProcess();
            state = OFF_STATE;
        }
    }
    public HomeController(){//set Event and register
        eventBus=new EventBus();
        eventBus.register(this);
        res_map=new HashMap<>();
        process_map=new HashMap<>();
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
        else if(event.getType().equals(BusMsg.ADD_EDGE)){
            paintEdgeNode(data);
           System.out.println(data);
        }
        else{
            System.out.println("未知消息");
        }

    }
    public void paintResourceNode(JSONObject data){//监听消息，并绘制一个资源节点
        //绘制资源节点
        String resName=data.getString("resName");
        int resNum=data.getInt("resNum");
        ResourceNodeShape resourceNodeShape = new ResourceNodeShape(mouseX, mouseY, resName, resNum,eventBus); // 创建一个资源节点

        //更新资源图
        res_to_Process_map.put(resName,new ArrayList<>());
        res_map.put(resName,resourceNodeShape);
        root.getChildren().add(resourceNodeShape);
    }
    public void paintProcessNode(JSONObject data){
        //绘制进程节点
        String processName=data.getString("processName");
        ProcessNodeShape processNodeShape = new ProcessNodeShape(mouseX, mouseY, processName,eventBus); // 创建一个进程节点

        //更新进程图
        process_to_res_map.put(processName,new ArrayList<>());
        process_map.put(processName,processNodeShape);
        root.getChildren().add(processNodeShape);

    }
    public void paintEdgeNode(JSONObject data){
        System.out.println("paintEdgeNode");
        //绘制边
        if(state==ADD_EDGE_STATE1){
            System.out.println("paintEdgeNode1");
            node2node.add(data);
            state=ADD_EDGE_STATE2;
        }else if(state==ADD_EDGE_STATE2){
            System.out.println("paintEdgeNode2");
            state=OFF_STATE;
            ResourceNodeShape resourceNodeShape=null;
            ProcessNodeShape processNodeShape=null;
            Point startP=null;
            Point endP=null;
            //获取边缘上的点
            Point startMarginPoint=null;
            Point endMarginPoint=null;
            String startNodeType=node2node.get(0).getString("nodeType");
            String endNodeType=data.getString("nodeType");

            //获取startNode和endNode的相关信息
            if(startNodeType.equals("resource")&&endNodeType.equals("process")) {
                resourceNodeShape = res_map.get(node2node.get(0).getString("resName"));
                processNodeShape = process_map.get(data.getString("processName"));
                res_to_Process_map.get(node2node.get(0).getString("resName")).add(processNodeShape);

                //get shape center point
                startP=new Point(resourceNodeShape.getTrueX(),resourceNodeShape.getTrueY());
                endP=new Point(processNodeShape.getTrueX(),processNodeShape.getTrueY());

                //获取边缘上的点
                startMarginPoint=resourceNodeShape.getNearestPoint(endP);
                endMarginPoint=processNodeShape.getNearestPoint(startP);
            } else if (startNodeType.equals("process")&&endNodeType.equals("resource")) {
                resourceNodeShape = res_map.get(data.getString("resName"));
                processNodeShape = process_map.get(node2node.get(0).getString("processName"));
                process_to_res_map.get(node2node.get(0).getString("processName")).add(resourceNodeShape);
                startP=new Point(processNodeShape.getTrueX(),processNodeShape.getTrueY());
                endP=new Point(resourceNodeShape.getTrueX(),resourceNodeShape.getTrueY());
                //获取边缘上的点
                startMarginPoint=processNodeShape.getNearestPoint(endP);
                endMarginPoint=resourceNodeShape.getNearestPoint(startP);
            } else {
                System.out.println("错误的边");
                return;
            }
            EdgeArrowShape edgeArrowShape=new EdgeArrowShape(startMarginPoint,endMarginPoint,"apply",node2node.get(0).getString("nodeType"),data.getString("nodeType"),root);
            //paintArrow(startMarginPoint,endMarginPoint);
            node2node.clear();
        }else {
            System.out.println("错误的状态 "+state);
        }

    }

    private final String OFF_STATE ="off";
    private final String ADD_RESOURCE_STATE ="addResourceState";
    private final String ADD_PROCESS_STATE ="addProcessState";
    private final String ADD_EDGE_STATE1="addEdgeState1";
    private final String ADD_EDGE_STATE2="addEdgeState2";
    private final String DELETE_STATE="deleteState";
    private final String MOVE_STATE="moveState";
    private String state = OFF_STATE;//记录当前状态
}
