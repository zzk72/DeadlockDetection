package com.example.deadlockdetection;

import com.example.deadlockdetection.Config.BusMsg;
import com.example.deadlockdetection.Config.MyEvent;
import com.example.deadlockdetection.Config.Point;
import com.example.deadlockdetection.ProcessNode.AddProcessDialogController;
import com.example.deadlockdetection.ProcessNode.ProcessNodeShape;
import com.example.deadlockdetection.ResourceNode.AddResourceDialogController;
import com.example.deadlockdetection.ResourceNode.ResourceNodeShape;
import com.example.deadlockdetection.edge.Edge;
import com.example.deadlockdetection.edge.EdgeArrowShape;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import com.google.common.eventbus.Subscribe;
import com.google.common.eventbus.EventBus;
import javafx.util.Duration;
import lombok.Data;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.*;


@Data
public class HomeController {


    public BorderPane borderPane;
    private BorderPane root;
    public EventBus eventBus;//事件总线
    private boolean hasDeleteEdge=false;

    private double mouseX;
    private double mouseY;
    private JSONObject node2;
    private  Map<String,List<Edge> > res_graph =new HashMap<>();//资源分配图
    private Map<String,List<Edge>> process_graph =new HashMap<>();//进程请求图
    private  Map<String,ResourceNodeShape> res_map=new HashMap<>();//记录资源节点
    private  Map<String,ProcessNodeShape> process_map=new HashMap<>();//记录进程节点
    private  Map<String,Edge> edge_map=new HashMap<>();//记录边
    private List<Edge> deleteEdgeList=new ArrayList<>();//记录要删除的边

    @FXML
    private MenuItem addResource;
    @FXML
    private MenuBar menuBar;
    @FXML
    public MenuItem addEdge;
    @FXML
    public MenuItem execute;
    @FXML
    public Label statusLabel;

    public HomeController(){//set Event and register
        eventBus=new EventBus();
        eventBus.register(this);
        res_map=new HashMap<>();
        process_map=new HashMap<>();
    }

    public void updateStatusLabel(String status){
        statusLabel.setText(status);
        System.out.println(status);
    }

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
        node2=new JSONObject();
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
        }else if(state.equals(DELETE_STATE)) {
            deleteNode();
            state = OFF_STATE;
        }
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
    private void deleteNode(){

    }
    @Subscribe
    public void handleMsg(MyEvent event){//监听消息并分发
        JSONObject data=event.getData();
        if(event.getType().equals(BusMsg.ADD_RESOURCE)){
            paintResourceNode(data,mouseX,mouseY);
        }else if(event.getType().equals(BusMsg.ADD_PROCESS)){
            paintProcessNode(data,mouseX,mouseY);
        }
        else if(event.getType().equals(BusMsg.ADD_EDGE)){
            paintEdgeNode(data);
            updateStatusLabel(data.toString());
        }
        else{
            updateStatusLabel("未知消息");
        }
    }

    public void paintResourceNode(JSONObject data,double mx,double my){//监听消息，并绘制一个资源节点
        //绘制资源节点
        String resName=data.getString("resName");
        int resNum=data.getInt("resNum");
        ResourceNodeShape resourceNodeShape = new ResourceNodeShape(mx, my, resName, resNum,eventBus); // 创建一个资源节点

        //更新资源图
        res_graph.put(resName,new ArrayList<>());
        res_map.put(resName,resourceNodeShape);
        root.getChildren().add(resourceNodeShape);
    }

    public void paintProcessNode(JSONObject data,double mx,double my){
        //绘制进程节点
        String processName=data.getString("processName");
        ProcessNodeShape processNodeShape = new ProcessNodeShape(mx,my, processName,eventBus); // 创建一个进程节点

        //更新进程图
        process_graph.put(processName,new ArrayList<>());
        process_map.put(processName,processNodeShape);
        root.getChildren().add(processNodeShape);

    }

    public void paintEdgeNode(JSONObject data){
        updateStatusLabel("paintEdgeNode");
        //绘制边
        if(state.equals(ADD_EDGE_STATE1)){
            updateStatusLabel("paintEdgeNode1");
            node2=data;
            state=ADD_EDGE_STATE2;
        }else if(Objects.equals(state, ADD_EDGE_STATE2)){
            updateStatusLabel("paintEdgeNode2");
            state=OFF_STATE;
            ResourceNodeShape resourceNodeShape=null;
            ProcessNodeShape processNodeShape=null;
            Point startP=null;
            Point endP=null;
            //获取边缘上的点
            Point startMarginPoint=null;
            Point endMarginPoint=null;
            String startNodeType=node2.getString("nodeType");
            String endNodeType=data.getString("nodeType");
            //获取startNode和endNode的相关信息
            if(startNodeType.equals("resource")&&endNodeType.equals("process")) {//分配边
                resourceNodeShape = res_map.get(node2.getString("resName"));
                processNodeShape = process_map.get(data.getString("processName"));

                //get shape center point
                startP=new Point(resourceNodeShape.getTrueX(),resourceNodeShape.getTrueY());
                endP=new Point(processNodeShape.getTrueX(),processNodeShape.getTrueY());

                //获取边缘上的点
                startMarginPoint=resourceNodeShape.getNearestPoint(endP);
                endMarginPoint=processNodeShape.getNearestPoint(startP);

                //调整箭头弧度
                EdgeArrowShape edgeArrowShape=new EdgeArrowShape(startMarginPoint,endMarginPoint,false,root);
                Edge edge=new Edge(node2.getString("resName"),data.getString("processName"),edgeArrowShape,resourceNodeShape,processNodeShape);
                //store edge
                edge_map.put(node2.getString("resName")+data.getString("processName"),edge);
                res_graph.get(node2.getString("resName")).add(edge);
                process_graph.get(data.getString("processName")).add(edge);

            } else if (startNodeType.equals("process")&&endNodeType.equals("resource")) {//申请边
                resourceNodeShape = res_map.get(data.getString("resName"));
                processNodeShape = process_map.get(node2.getString("processName"));

                startP=new Point(processNodeShape.getTrueX(),processNodeShape.getTrueY());
                endP=new Point(resourceNodeShape.getTrueX(),resourceNodeShape.getTrueY());

                //获取边缘上的点
                startMarginPoint=processNodeShape.getNearestPoint(endP);
                endMarginPoint=resourceNodeShape.getNearestPoint(startP);

                //调整箭头弧度
                EdgeArrowShape edgeArrowShape=new EdgeArrowShape(startMarginPoint,endMarginPoint,true,root);
//                eventBus.register(edgeArrowShape);
                Edge edge=new Edge(node2.getString("processName"),data.getString("resName"),edgeArrowShape,resourceNodeShape,processNodeShape);
                //store edge
                edge_map.put(node2.getString("processName")+data.getString("resName"),edge);
                process_graph.get(node2.getString("processName")).add(edge);
                res_graph.get(data.getString("resName")).add(edge);

            } else {
                updateStatusLabel("错误的边");
                return;
            }
            //paintArrow(startMarginPoint,endMarginPoint);
            node2=new JSONObject();
        }else {
            updateStatusLabel("待扩展状态 "+state);
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

    @FXML
    public void OnDeleteEdge(ActionEvent actionEvent) {
        state = DELETE_STATE;
    }

    private void getDeleteEdge(){
        updateStatusLabel("约简资源分配图 getDeleteEdge");
        if(hasDeleteEdge){
            deleteEdgeList.clear();
        }
        int visitNum=1;
        boolean hasFree=true;//上轮循环中检测到新free的节点
        while(hasFree){
            updateStatusLabel("hasFree");
            hasFree=false;
            //找到一个可满足的进程节点
            for(ProcessNodeShape processNodeShape:process_map.values()){
                updateStatusLabel("getting delete edge "+processNodeShape.getProcessName());
                if((!processNodeShape.isVisited())&&checkProcessNode(processNodeShape)){
                    hasFree=true;
                    updateStatusLabel(processNodeShape.getProcessName());
                    processNodeShape.setVisited(true);
                    visitNum++;

                    //删除该进程节点所有边
                    for(Edge edge:process_graph.get(processNodeShape.getProcessName())){
                        edge.setTimeStep(visitNum);
                        deleteEdgeList.add(edge);
                        updateStatusLabel("add delete edge "+edge.getStartNodeName()+" "+edge.getEndNodeName()+"to deleteEdgeList");

                        //返还资源
                        if(!edge.isApplyEdge()){
                            updateStatusLabel("return res");
                            ResourceNodeShape resourceNodeShape=edge.getResourceNodeShape();
                            resourceNodeShape.setResNum(resourceNodeShape.getResNum()+1);
                        }
                    }
                }
            }
        }
        hasDeleteEdge=true;

    }

    @FXML
    //执行资源分配图约简算法
    public void OnExecute(ActionEvent actionEvent) throws InterruptedException {
        //约简资源分配图
        if(!hasDeleteEdge){
            getDeleteEdge();
        }
        //执行删除边动画
        playDeleteEdgeList();
    }

    private void playDeleteEdgeList() throws InterruptedException {//设置时间轴，每隔1秒删除一条边
        Timeline timeline = new Timeline();
        Duration duration = Duration.millis(2000);
        int i=1;
        for(Edge edge:deleteEdgeList){
            updateStatusLabel("delete edge:"+edge.getStartNodeName()+"->"+edge.getEndNodeName());
            KeyFrame keyFrame = new KeyFrame(duration.multiply(edge.getTimeStep())
                    , e -> {
                try {
                    edge.setVisibility(false);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            });
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.play();

    }
    //检查一个进程节点是否可满足
    private boolean checkProcessNode(ProcessNodeShape processNodeShape){
        updateStatusLabel("checkProcessNode");
        String processName=processNodeShape.getProcessName();
        List<Edge> edges=process_graph.get(processName);
        for(Edge edge:edges){

            if(edge.isApplyEdge()&&edge.isShow()) {//检查该点所有申请边
                String resName = edge.getEndNodeName();
                ResourceNodeShape resourceNodeShape = res_map.get(resName);
                List<Edge> resEdges = res_graph.get(resName);
                int total = resourceNodeShape.getResNum();
                int distribute = 0;
//                int distribute = 1;
                for (Edge resEdge : resEdges) {
                    if (resEdge.isApplyEdge()&&resEdge.isShow()) {//分配边
//                        distribute+=resEdge.getProcessNodeShape().getResNum();
                        distribute += 1;
                    }
                }
                if (total - distribute < 0) {//资源不足 应<0，因为该进程节点本身也有申请边
                    return false;
                }
            }
        }
        return true;
    }

    @FXML
    public void OnExeStep(ActionEvent actionEvent) throws InterruptedException {
        if(!hasDeleteEdge){
            getDeleteEdge();
        }
        if(deleteEdgeList.size()>0){
            Edge edge=deleteEdgeList.get(0);
            deleteEdgeList.remove(0);
            edge.setVisibility(false);
        }else{
            updateStatusLabel("执行完毕");
        }

    }

    @FXML
    public  void OnClear(ActionEvent actionEvent) {
        //清空所有数据
        res_graph.clear();
        process_graph.clear();
        res_map.clear();
        process_map.clear();
        edge_map.clear();
        deleteEdgeList.clear();
        hasDeleteEdge=false;
        //清空所有节点 保留菜单栏
        root.getChildren().removeIf(node -> node instanceof ResourceNodeShape || node instanceof ProcessNodeShape || node instanceof EdgeArrowShape);

        //清空所有状态
        state = OFF_STATE;
    }
    @FXML
    public void OnInit(ActionEvent actionEvent) throws IOException {
        OnClear(actionEvent);

        //初始化一个资源分配图，包含3个资源节点和3个进程节点

        //绘制资源节点
        for(int i=0;i<3;i++){
            JSONObject data=new JSONObject();
            data.put("resName","R"+i);
            data.put("resNum",(i+1));
            paintResourceNode(data,100+i*150,100);
        }
        //绘制进程节点
        for(int i=0;i<3;i++){
            JSONObject data=new JSONObject();
            data.put("processName","P"+i);
            paintProcessNode(data,100+i*150,400);
        }


    }
}



