package com.example.deadlockdetection.ProcessNode;
import com.example.deadlockdetection.Config.BusMsg;
import com.example.deadlockdetection.Config.MyEvent;
import com.example.deadlockdetection.Config.Point;
import com.google.common.eventbus.EventBus;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import lombok.Data;
import net.sf.json.JSONObject;
@Data
public class ProcessNodeShape extends Group{
    double centerX;
    double centerY;
    double transX=0;
    double transY=0;
    private double scale=1.1;//缩放比例
    private double circleRadius=20;//圆的半径
    private String processName;
    private EventBus eventBus;//事件总线
    private Color commonColor=Color.rgb(140, 80, 210, 0.5);
    private Color mouseEnteredColor=Color.rgb(90, 20, 255, 0.8);
    private boolean visited=false;
    public double getTrueX(){
        return centerX+transX;
    }
    public  double getTrueY(){
        return centerY+transY;
    }
    //传入一个点，返回距离该点最近的this Group的边界上的点
    public Point getNearestPoint(Point point){
        double x=point.getX();
        double y=point.getY();
        double x1=this.getTrueX();
        double y1=this.getTrueY();
        double r=circleRadius;
        double x2=x1+r*Math.cos(Math.atan2(y-y1,x-x1));
        double y2=y1+r*Math.sin(Math.atan2(y-y1,x-x1));

        return new Point(x2,y2);
    }
    public ProcessNodeShape(double centerX, double centerY, String processName, EventBus eventBus) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.processName = processName;

        createProcessNode();

        //鼠标拖拽时移动图形
        this.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                transLocation(event.getSceneX(), event.getSceneY());//传入鼠标在scene中的坐标
            }
        });
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                JSONObject data=new JSONObject();
                data.put("nodeType","process");
                data.put("processName",processName);
                eventBus.post(new MyEvent(BusMsg.ADD_EDGE,data));
                System.out.println("processNodeShape clicked and post a message");
            }
        });

        // 鼠标停止拖拽时，更新图形的中心点坐标
//        this.setOnMouseReleased(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                updateTransXY(event.getSceneX(), event.getSceneY());
//            }
//        });
        // 鼠标经过时更改图形的颜色并显示进程名
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                changeColor();
                changeSize(scale);
            }
        });
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                changeColor();
                changeSize(1.0);
            }
        });
    }

    private void transLocation(double sceneX, double sceneY) {
        // 计算图形的新位置
        transX = sceneX - centerX;
        transY = sceneY - centerY;

        // 更新图形的位置,setTranslateX和setTranslateY是相对于原来的位置进行移动,仍然保留原来的位置信息，而setLayoutX和setLayoutY则是直接设置图形的位置
        this.setTranslateX(transX);
        this.setTranslateY(transY);
        System.out.println("transX:"+transX+" transY:"+transY);
    }
//    private void updateTransXY(double sceneX, double sceneY) {
//        // 更新图形的中心点坐标
//        centerX = sceneX;
//        centerY = sceneY;
//        this.setLayoutX(centerX);
//        this.setLayoutY(centerY);
//    }
    private void createProcessNode() {
        // 创建进程的主体部分（圆形）
        Circle processBody = new Circle(centerX, centerY, 20);
        processBody.setFill(commonColor);
        processBody.setStroke(commonColor);

        // 在进程主体上方显示进程名
        Text processNameText = new Text(centerX - 20, centerY - 25, processName);

        this.getChildren().addAll(processBody, processNameText);
    }

    private void changeSize(double s) {//按比例缩放
        this.setScaleX(s);
        this.setScaleY(s);
    }
    private void changeColor() {
        // 更改图形的颜色
        Circle processBody = (Circle) this.getChildren().get(0);
        if(processBody.getFill()==commonColor) {
            processBody.setFill(mouseEnteredColor);
        }else {
            processBody.setFill(commonColor);
        }
    }


}
